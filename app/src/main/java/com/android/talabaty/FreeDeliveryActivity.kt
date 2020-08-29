package com.android.talabaty

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.talabaty.adapter.CartAdapter
import com.android.talabaty.adapter.FreeDeliveryAdapter
import com.android.talabaty.dbUtil.Status
import com.android.talabaty.dbUtil.ViewModelFactory
import com.android.talabaty.model.Cart
import com.android.talabaty.model.FreeDelivery
import com.android.talabaty.model.HomePageCategory
import com.android.talabaty.retrofit.ApiHelperImpl
import com.android.talabaty.retrofit.RetrofitBuilder
import com.android.talabaty.util.Helper
import com.android.talabaty.viewModel.CartViewModel
import com.android.talabaty.viewModel.OrdersViewModel
import kotlinx.android.synthetic.main.activity_free_delivery.*
import kotlinx.android.synthetic.main.title_toolbar.*

class FreeDeliveryActivity : AppCompatActivity() {
    val TAG = "FreeDeliveryActivity"
    private lateinit var viewModel: OrdersViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_free_delivery)
        initViewModel()
        viewModel.storesFreeDelivery()
        setupObserverGetCart()
        imgArrowBack.setOnClickListener {
            Log.e("TAG", "onCreate: " )
            finish()
        }

    }


    private fun initViewModel() {

        viewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiHelperImpl(RetrofitBuilder.apiService), application)
        ).get(OrdersViewModel::class.java)
    }

    private fun setupObserverGetCart() {

        viewModel.getStoresFreeDelivery().observe(this,
            Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        progressBar.visibility = View.GONE
                        it.data?.let { users ->
                            if (users.storesFreeDelivery.isEmpty()){
                                tvNotFoundDelivery.visibility  = View.VISIBLE
                            }else
                                initRecycleView(users.storesFreeDelivery)
                        }
                    }
                    Status.LOADING -> {
                        progressBar.visibility = View.VISIBLE
                        tvNotFoundDelivery.visibility = View.GONE

                    }
                    Status.ERROR -> {
                        tvNotFoundDelivery.visibility = View.GONE
                        progressBar.visibility = View.GONE
                        Helper.showFilterDialog(this, it.message!!).show()
                        Log.e(TAG, "setupObserver: " + it.message)

                    }
                }

            }
        )
    }

    private fun initRecycleView(data:  List<FreeDelivery>) {

        val adapter = FreeDeliveryAdapter(this, data)
        val linearLayoutManager = LinearLayoutManager(this)
        rvFreeDelivery.layoutManager = linearLayoutManager
        rvFreeDelivery.adapter = adapter
        rvFreeDelivery.setHasFixedSize(true)


    }


}