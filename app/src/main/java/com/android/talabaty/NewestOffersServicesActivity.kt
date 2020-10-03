package com.android.talabaty

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.talabaty.adapter.NewestOffersAdapter
import com.android.talabaty.dbUtil.Status
import com.android.talabaty.dbUtil.ViewModelFactory
import com.android.talabaty.model.Offer
import com.android.talabaty.retrofit.ApiHelperImpl
import com.android.talabaty.retrofit.RetrofitBuilder
import com.android.talabaty.util.CustomMaterialDialog.getMaterialDialogInstance
import com.android.talabaty.util.MyPreferences
import com.android.talabaty.viewModel.AllMainViewModel
import kotlinx.android.synthetic.main.activity_newest_offers_services.*
import kotlinx.android.synthetic.main.activity_newest_offers_services.swipeRefresh
import kotlinx.android.synthetic.main.activity_newest_offers_services.tvNotFound
import kotlinx.android.synthetic.main.toolbar_location.*

class NewestOffersServicesActivity : AppCompatActivity() {
    val TAG = "NewestOffersActivity"
    private lateinit var viewModel: AllMainViewModel
    private lateinit var data: ArrayList<Offer>
    private var text: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_newest_offers_services)
        MyPreferences.context  =this
        data = ArrayList()
        handleToolbar ()
        initViewModel()
        viewModel.allOffers()
        setupObserver()
        swipeToRefresh()
        handleSearchProduct()
        setupObserverSearch()
    }

    private fun setupObserver() {
        tvNotFound?.visibility = View.GONE
        viewModel.getAllOffers().observe(this,
            Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        swipeRefresh?.isRefreshing = false
                        tvNotFound?.visibility = View.GONE
                        it.data?.let { users ->
                            data.clear()
                            data.addAll(users.offers)
                            if (users.offers.isEmpty()) {
                                tvNotFound?.visibility = View.VISIBLE
                            }
                            initRecycleView()
                        }
                    }
                    Status.LOADING -> {
                        tvNotFound?.visibility = View.GONE
                        data.clear()
                        swipeRefresh?.isRefreshing = true
                        initRecycleView()

                    }
                    Status.ERROR -> {
                        tvNotFound?.visibility = View.GONE
                        swipeRefresh?.isRefreshing = false
                        //  Helper.showFilterDialog(activity!!, it.message!!).show()
                        getMaterialDialogInstance(it.message!!)

                        Log.e(TAG, "setupObserver: " + it.message)
                    }
                }
            }
        )
    }

    private fun initViewModel() {

        viewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiHelperImpl(RetrofitBuilder.apiService), application)
        ).get(AllMainViewModel::class.java)
    }

    private fun swipeToRefresh() {
        swipeRefresh?.setOnRefreshListener {
            viewModel.allOffers()
        }

    }

    private fun initRecycleView() {
        val adapter = NewestOffersAdapter(this, data)
        rvNewestOffers.layoutManager = LinearLayoutManager(this)
        rvNewestOffers.adapter = adapter
        rvNewestOffers.setHasFixedSize(true)

    }

    private fun  handleToolbar (){
        imgArrowBack.setOnClickListener {
            finish()
        }

        imgCart.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))

        }

        imgFav.setOnClickListener {
            startActivity(Intent(this, FavoriteActivity::class.java))

        }

        tvHomeLocation.text = MyPreferences.getStr("city")

    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val v: View? = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    v.clearFocus()
                    val imm: InputMethodManager =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0)
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }

    private fun handleSearchProduct() {
        searchHomeNewest.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                text = searchHomeNewest.text.toString()
                if (text.isNullOrEmpty()) {
                    text = ""
                }
                viewModel.searchOffers(text!!)
                true
            } else {
                false
            }
        }
    }

    private fun setupObserverSearch() {

        viewModel.getSearchOffers().observe(this,
            Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        swipeRefresh.isRefreshing = false

                        it.data?.let { users ->
                            if (users.offers.isEmpty()) {
                                tvNotFound.visibility = View.VISIBLE
                            } else {
                                data = users.offers
                                initRecycleView()
                            }
                        }
                    }
                    Status.LOADING -> {
                        data?.clear()
                        swipeRefresh.isRefreshing = true
                        tvNotFound.visibility = View.GONE
                        initRecycleView()
                    }
                    Status.ERROR -> {
                        swipeRefresh.isRefreshing = false
                        tvNotFound.visibility = View.GONE
                        getMaterialDialogInstance(it.message!!)
                    }
                }

            }
        )
    }




}