package com.android.talabaty

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.talabaty.adapter.NearbyAdapter
import com.android.talabaty.dbUtil.Status
import com.android.talabaty.dbUtil.ViewModelFactory
import com.android.talabaty.model.Store
import com.android.talabaty.retrofit.ApiHelperImpl
import com.android.talabaty.retrofit.RetrofitBuilder
import com.android.talabaty.util.CustomMaterialDialog.getMaterialDialogInstance
import com.android.talabaty.util.MyPreferences
import com.android.talabaty.viewModel.StoresViewModel
import kotlinx.android.synthetic.main.activity_nearby.*
import kotlinx.android.synthetic.main.title_toolbar.*

class NearbyActivity : AppCompatActivity() {
    val TAG = "NearbyActivity"
    private lateinit var viewModel: StoresViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nearby)
        MyPreferences.context = this
       val lat = MyPreferences.getLong("lat")
       val lng = MyPreferences.getLong("lng")
        initViewModelNearby()
        if (lat!=0L&& lng !=0L ){
            viewModel.nearbyStores(lat,lng)
        }else{
            tvNotFound.visibility = View.VISIBLE
        }
        setupObserverNearby()
        swipeToRefresh()
        handleToolbar()
    }

    private fun initViewModelNearby() {

        viewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiHelperImpl(RetrofitBuilder.apiService), application)
        ).get(StoresViewModel::class.java)
    }

    private fun setupObserverNearby() {

        viewModel.getNearbyStores().observe(this,
            Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        swipeRefresh?.isRefreshing = false
                        it.data?.let { users ->
                            if (users.stores.isEmpty()) {
                                tvNotFound.visibility = View.VISIBLE
                            } else
                                initRecycleViewNearby(users.stores)
                        }
                    }
                    Status.LOADING -> {
                        swipeRefresh?.isRefreshing = true
                        tvNotFound.visibility = View.GONE

                    }
                    Status.ERROR -> {
                        tvNotFound.visibility = View.GONE
                        swipeRefresh?.isRefreshing = false
                        getMaterialDialogInstance(it.message!!)
                        Log.e(TAG, "setupObserver: " + it.message)

                    }
                }

            }
        )
    }


    private fun initRecycleViewNearby(carts: ArrayList<Store>) {
       val  adapter = NearbyAdapter(this, carts)
        val linearLayoutManager = LinearLayoutManager(this)
        rvNearby.layoutManager = linearLayoutManager
        rvNearby.adapter = adapter
        rvNearby.setHasFixedSize(true)

        adapter.onItemClick = { position ->

        }
    }

    private fun swipeToRefresh() {
        swipeRefresh?.setOnRefreshListener {
            val lat = MyPreferences.getLong("lat")
            val lng = MyPreferences.getLong("lng")
            if (lat!=0L&& lng !=0L ){
                viewModel.nearbyStores(lat,lng)
            }else{
                tvNotFound.visibility = View.VISIBLE
            }
        }

    }

    private fun handleToolbar() {
        imgArrowBack.setOnClickListener {
            finish()

        }

        tvTitleToolbar.text = getString(R.string.near_places)

    }


}