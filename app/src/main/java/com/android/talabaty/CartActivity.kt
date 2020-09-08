package com.android.talabaty


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
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

import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.title_toolbar.*
import kotlinx.android.synthetic.main.title_toolbar.imgArrowBack


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
            startActivity(Intent(applicationContext, CreditCardActivity::class.java))
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

            Log.e(TAG, "position: $position ")
            Log.e(TAG, "cart_product id : ${cart.product!!.id} ")

            viewModel.deleteFromCart(cart.product!!.id)
            carts.removeAt(position)
            adapter?.notifyDataSetChanged()

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

            setupObserverGetCart()
        }

    }

    private fun initTitleToolbar() {
        imgArrowBack.setOnClickListener {
            finish()
        }

        tvTitleToolbar.setText(R.string.shopping_cart)
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