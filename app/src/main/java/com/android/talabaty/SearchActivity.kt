package com.android.talabaty

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.talabaty.adapter.RecycleCategoryAdapter
import com.android.talabaty.dbUtil.Status
import com.android.talabaty.dbUtil.ViewModelFactory
import com.android.talabaty.model.Product
import com.android.talabaty.retrofit.ApiHelperImpl
import com.android.talabaty.retrofit.RetrofitBuilder
import com.android.talabaty.util.CustomMaterialDialog.getMaterialDialogInstance
import com.android.talabaty.viewModel.AllMainViewModel
import kotlinx.android.synthetic.main.activity_all_coupons.*
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.activity_search.swipeRefresh
import kotlinx.android.synthetic.main.activity_search.tvNotFound
import kotlinx.android.synthetic.main.search_view.*


class SearchActivity : AppCompatActivity() {
    val TAG = "SearchActivity"
    private lateinit var viewModel: AllMainViewModel
    private var text: String? = null
    private var data: ArrayList<Product>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        this.getWindow()
            .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        data = ArrayList()
        initViewModel()
        handleSearchProduct()
        setupObserver()
        swipeToRefresh()
    }

    private fun handleSearchProduct() {
        searchHome.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                text = searchHome.text.toString()
                if (text.isNullOrEmpty()) {
                    text = ""
                }
                viewModel.searchProducts(text!!)
                true
            } else {
                false
            }
        }
    }

    private fun initRecycleView() {

        val adapter = RecycleCategoryAdapter(this, data!!)
        rvProducts.layoutManager = LinearLayoutManager(this)
        rvProducts.adapter = adapter
        rvProducts.setHasFixedSize(true)

        adapter.setOnClickListener(object : RecycleCategoryAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                Log.e(TAG, "onItemClick: ")
            }

            override fun onItemLongClick(position: Int) {
                TODO("Not yet implemented")
            }
        })
    }


    private fun initViewModel() {

        viewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiHelperImpl(RetrofitBuilder.apiService), application)
        ).get(AllMainViewModel::class.java)
    }

    private fun setupObserver() {

        viewModel.getSearchProducts().observe(this,
            Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        swipeRefresh.isRefreshing = false

                        it.data?.let { users ->
                            if (users.products.isEmpty()) {
                                tvNotFound.visibility = View.VISIBLE
                            } else {
                                data = users.products
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

                        //Handle Error
                        Log.e(TAG, "setupObserver: " + it.message)

                    }
                }

            }
        )
    }


    private fun swipeToRefresh() {
        swipeRefresh.setOnRefreshListener {

            if (text != null)
                viewModel.searchProducts(text!!)
        }

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


}