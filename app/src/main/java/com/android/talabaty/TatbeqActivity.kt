package com.android.talabaty

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.android.talabaty.adapter.TatbeqPagerAdapter
import com.android.talabaty.dbUtil.Status
import com.android.talabaty.dbUtil.ViewModelFactory
import com.android.talabaty.retrofit.ApiHelperImpl
import com.android.talabaty.retrofit.RetrofitBuilder
import com.android.talabaty.util.CustomMaterialDialog.getMaterialDialogInstance
import com.android.talabaty.viewModel.CartViewModel
import com.android.talabaty.viewModel.StoresViewModel
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_tatbeq.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.toolbar.imgCart
import kotlinx.android.synthetic.main.toolbar.imgFav
import kotlinx.android.synthetic.main.toolbar.tvCartNum

class TatbeqActivity : AppCompatActivity() {
    val TAG = "TatbeqActivity"
    private lateinit var viewModel: StoresViewModel
    private lateinit var viewModelCart: CartViewModel

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tatbeq)
        initViewModel()
        setupObserver()
        handleToolbar()
        handleCartNum()
        viewModel.stores()
        viewpager?.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewModel.tatbeqProductById(tab!!.position)

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
        tabs?.setupWithViewPager(viewpager)


    }

    private fun initViewModel() {

        viewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiHelperImpl(RetrofitBuilder.apiService), application)
        ).get(StoresViewModel::class.java)
    }


    private fun setupObserver() {

        viewModel.getAllStores().observe(
            this,
            Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        it.data?.let { users ->
                            for (i in users.activities.indices) {
                                Log.e(TAG, "setupObserver: ${users.activities[i].name}")
                            }
                            viewpager?.adapter = TatbeqPagerAdapter(
                                this,
                                users.activities.size,
                                users.activities,
                                supportFragmentManager
                            )
                        }
                    }
                    Status.LOADING -> {

                    }
                    Status.ERROR -> {
                        getMaterialDialogInstance(it.message!!)
                    }
                }

            }
        )
    }

    private fun handleToolbar(){
        imgArrowBack.setOnClickListener {
            finish()

        }

        imgCart.setOnClickListener {
            startActivity(Intent(this,CartActivity::class.java))

        }


        imgFav.setOnClickListener {
            startActivity(Intent(this,FavoriteActivity::class.java))

        }
    }


    private fun initViewModelCart() {

        viewModelCart = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiHelperImpl(RetrofitBuilder.apiService), application)
        ).get(CartViewModel::class.java)
    }

    private fun setupObserverGetCart() {

        viewModelCart.getAllCart().observe(this,
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
        initViewModelCart()
        viewModelCart.getCart()
        setupObserverGetCart()
    }

    override fun onStart() {
        super.onStart()
        handleCartNum()
        Log.e(TAG, "onStart: " )

    }


}