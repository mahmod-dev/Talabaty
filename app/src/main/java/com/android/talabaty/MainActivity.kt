package com.android.talabaty

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.android.talabaty.dbUtil.Status
import com.android.talabaty.dbUtil.ViewModelFactory
import com.android.talabaty.retrofit.ApiHelperImpl
import com.android.talabaty.retrofit.RetrofitBuilder
import com.android.talabaty.util.CustomMaterialDialog
import com.android.talabaty.util.CustomMaterialDialog.getMaterialDialogInstance
import com.android.talabaty.util.Helper
import com.android.talabaty.util.MyPreferences
import com.android.talabaty.viewModel.CartViewModel
import kotlinx.android.synthetic.main.toolbar_location_cart.*


class MainActivity : BaseActivity() {
    override val TAG = "MainActivity"
    private lateinit var viewModel: CartViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MyPreferences.context = applicationContext
        handleCartNum()
        Log.e(
            TAG,
            "onCreate:${MyPreferences.getStr("userToken")} :: ${MyPreferences.getStr("userMobile")} "
        )

        imgCart.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
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
                        it.data?.let { users ->
                            if (users.cart.isEmpty()) {
                                tvCartNum.visibility = View.GONE

                            } else {
                                tvCartNum.visibility = View.VISIBLE
                                tvCartNum.text = users.cart.size.toString()

                            }
                        }
                    }
                    Status.LOADING -> {

                    }
                    Status.ERROR -> {

                        getMaterialDialogInstance(it.message!!)
                        Log.e(TAG, "setupObserver: " + it.message)

                    }
                }

            }
        )
    }

    private fun handleCartNum(){
        initViewModel()
        viewModel.getCart()
        setupObserverGetCart()
    }



    override fun onStart() {
        super.onStart()
        handleCartNum()
        Log.e(TAG, "onStart: " )

    }

}