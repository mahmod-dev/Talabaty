package com.android.talabaty

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.talabaty.adapter.NewestOffersAdapter
import com.android.talabaty.adapter.RecycleStoresAdapter
import com.android.talabaty.dbUtil.Status
import com.android.talabaty.dbUtil.ViewModelFactory
import com.android.talabaty.model.Offer
import com.android.talabaty.model.Store
import com.android.talabaty.retrofit.ApiHelperImpl
import com.android.talabaty.retrofit.RetrofitBuilder
import com.android.talabaty.util.CustomMaterialDialog.getMaterialDialogInstance
import com.android.talabaty.viewModel.AllMainViewModel
import com.android.talabaty.viewModel.StoresViewModel
import kotlinx.android.synthetic.main.activity_newest_offers_services.*

class NewestOffersServicesActivity : AppCompatActivity() {
    val TAG = "NewestOffersActivity"
    private lateinit var viewModel: AllMainViewModel
    private lateinit var data: ArrayList<Offer>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_newest_offers_services)
        data = ArrayList()
        initViewModel()
        viewModel.allOffers()
        setupObserver()
        swipeToRefresh()
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

            setupObserver()
        }

    }

    private fun initRecycleView() {
        val adapter = NewestOffersAdapter(this, data)
        rvNewestOffers.layoutManager = LinearLayoutManager(this)
        rvNewestOffers.adapter = adapter
        rvNewestOffers.setHasFixedSize(true)

    }

}