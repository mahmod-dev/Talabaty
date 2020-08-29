package com.android.talabaty


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.talabaty.adapter.CartAdapter
import com.android.talabaty.dbUtil.ViewModelFactory
import com.android.talabaty.model.Cart
import com.android.talabaty.retrofit.ApiHelperImpl
import com.android.talabaty.retrofit.RetrofitBuilder
import com.android.talabaty.util.Helper
import com.android.talabaty.viewModel.CartViewModel
import com.android.talabaty.dbUtil.Status

import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.title_toolbar.*


class CartActivity : AppCompatActivity() {
    val TAG = "CartActivity"
    private lateinit var viewModel: CartViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)


        initViewModel()
        viewModel.getCart()
        btnCompleteOrder?.setOnClickListener {
            startActivity(Intent(applicationContext, CreditCardActivity::class.java))
        }

        imgArrowBackCart.setOnClickListener {
            finish()
        }

        setupObserverGetCart()
        swipeToRefresh()
    }


    private fun initRecycleView(viewStores: ArrayList<Cart>) {

        val adapter = CartAdapter(this, viewStores)
        val linearLayoutManager = LinearLayoutManager(this)
        rvCartDetails.layoutManager = linearLayoutManager
        rvCartDetails.adapter = adapter
        rvCartDetails.setHasFixedSize(true)


    }


    private fun initViewModel() {

        viewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiHelperImpl(RetrofitBuilder.apiService), application)
        ).get(CartViewModel::class.java)
    }

    private fun setupObserverGetCart() {

        viewModel.getAllCart().observe(this,
            Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        swipeRefresh?.isRefreshing = false
                        it.data?.let { users ->
                            if (users.cart.isEmpty()){
                                tvNotFound.visibility  = View.VISIBLE
                            }else
                            initRecycleView(users.cart)
                        }
                    }
                    Status.LOADING -> {
                        swipeRefresh?.isRefreshing = true
                        tvNotFound.visibility = View.GONE

                    }
                    Status.ERROR -> {
                        tvNotFound.visibility = View.GONE
                        swipeRefresh?.isRefreshing = false
                        Helper.showFilterDialog(this, it.message!!).show()
                        Log.e(TAG, "setupObserver: " + it.message)

                    }
                }

            }
        )
    }



    private fun swipeToRefresh(){
        swipeRefresh?.setOnRefreshListener {

            setupObserverGetCart()
        }

    }

    private fun initTitleToolbar() {
        imgArrowBack.setOnClickListener {
            finish()
        }

        tvTitleToolbar.setText(R.string.shopping_cart)
    }
}