package com.android.talabaty


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.talabaty.adapter.CartAdapter
import com.android.talabaty.dbUtil.ViewModelFactory
import com.android.talabaty.model.Cart
import com.android.talabaty.retrofit.ApiHelperImpl
import com.android.talabaty.retrofit.RetrofitBuilder
import com.android.talabaty.viewModel.CartViewModel
import com.android.talabaty.dbUtil.Status
import com.android.talabaty.util.CustomMaterialDialog.getMaterialDialogInstance
import com.android.talabaty.util.Helper

import kotlinx.android.synthetic.main.activity_cart.*


class CartActivity : AppCompatActivity() {
    val TAG = "CartActivity"
    private lateinit var viewModel: CartViewModel
    var adapter: CartAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)


        initViewModel()
        viewModel.getCart()
        btnCompleteOrder?.setOnClickListener {
            startActivity(Intent(applicationContext, PaymentMethodActivity::class.java))
        }

        imgArrowBackCart.setOnClickListener {
            finish()
        }

        setupObserverGetCart()
        setupObserverRemoveFromCart()
        swipeToRefresh()
    }


    private fun initRecycleView(carts: ArrayList<Cart>) {
        adapter = CartAdapter(this, carts)
        val linearLayoutManager = LinearLayoutManager(this)
        rvCartDetails.layoutManager = linearLayoutManager
        rvCartDetails.adapter = adapter
        rvCartDetails.setHasFixedSize(true)

        adapter!!.onItemClick = { cart, position ->
            Helper.dialogConfirm(this,getString(R.string.delete_question_cart))
            Helper.onItemClick= {
                viewModel.deleteFromCart(cart.product!!.id)
                carts.removeAt(position)
                adapter?.notifyDataSetChanged()
            }


        }
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
                            if (users.cart.isEmpty()) {
                                tvNotFound.visibility = View.VISIBLE
                            } else
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
                        getMaterialDialogInstance(it.message!!)
                        Log.e(TAG, "setupObserver: " + it.message)

                    }
                }

            }
        )
    }


    private fun swipeToRefresh() {
        swipeRefresh?.setOnRefreshListener {

            viewModel.getCart()
        }

    }

    private fun setupObserverRemoveFromCart() {

        viewModel.getDeleteToCart().observe(this,
            Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        // viewModel.getCart()
                        // progressBar.visibility = View.GONE
                        it.data?.let { users ->
                            Toast.makeText(this, users.message, Toast.LENGTH_SHORT).show()
                            viewModel.getCart()

                        }
                    }
                    Status.LOADING -> {
                        //progressBar.visibility = View.VISIBLE

                    }
                    Status.ERROR -> {
                        // progressBar.visibility = View.GONE
                        getMaterialDialogInstance(it.message!!)
                        Log.e(TAG, "setupObserver: " + it.message)

                    }
                }

            }
        )
    }

}