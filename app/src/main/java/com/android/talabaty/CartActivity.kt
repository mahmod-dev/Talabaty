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
import com.android.talabaty.model.Product
import com.android.talabaty.model.ProductEx
import com.android.talabaty.retrofit.ApiHelperImpl
import com.android.talabaty.retrofit.RetrofitBuilder
import com.android.talabaty.util.Helper
import com.android.talabaty.util.MyPreferences
import com.android.talabaty.viewModel.CartViewModel
import com.mahmoud.todoapp.util.dbUtil.Status

import kotlinx.android.synthetic.main.activity_cart.*


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

//        imgFav.setOnClickListener {
//
//        }



        setupObserverGetCart()
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
                         progressBar.visibility = View.GONE
                        it.data?.let { users ->
                            initRecycleView(users.cart)

                        }
                    }
                    Status.LOADING -> {
                        progressBar.visibility = View.VISIBLE

                    }
                    Status.ERROR -> {
                         progressBar.visibility = View.GONE
                        Helper.showFilterDialog(this, it.message!!).show()
                        Log.e(TAG, "setupObserver: " + it.message)

                    }
                }

            }
        )
    }

}