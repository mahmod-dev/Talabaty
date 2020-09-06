package com.android.talabaty

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.talabaty.adapter.FavoriteCartAdapter
import com.android.talabaty.dbUtil.Status
import com.android.talabaty.dbUtil.ViewModelFactory
import com.android.talabaty.model.Product
import com.android.talabaty.retrofit.ApiHelperImpl
import com.android.talabaty.retrofit.RetrofitBuilder
import com.android.talabaty.util.CustomMaterialDialog
import com.android.talabaty.util.CustomMaterialDialog.getMaterialDialogInstance
import com.android.talabaty.util.Helper
import com.android.talabaty.viewModel.CartViewModel
import kotlinx.android.synthetic.main.activity_favorite.*
import kotlinx.android.synthetic.main.title_toolbar.*

class FavoriteActivity : AppCompatActivity() {
    val TAG = "FavoriteActivity"
    private lateinit var viewModel: CartViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)
        initViewModel()
        viewModel.getFavorite()
        setupObserverGetCart()
        swipeToRefresh()
        imgArrowBack.setOnClickListener {
            finish()
        }

        tvTitleToolbar.text = getString(R.string.favorite)


    }

    private fun initRecycleView(viewStores: ArrayList<Product>) {
        val adapter = FavoriteCartAdapter(this, viewStores)
        val linearLayoutManager = LinearLayoutManager(this)
        rvFav.layoutManager = linearLayoutManager
        rvFav.adapter = adapter
        rvFav.setHasFixedSize(true)
    }


    private fun initViewModel() {

        viewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiHelperImpl(RetrofitBuilder.apiService), application)
        ).get(CartViewModel::class.java)
    }

    private fun setupObserverGetCart() {

        viewModel.getAllFavorite().observe(this,
            Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        swipeRefresh?.isRefreshing = false

                        it.data?.let { users ->
                            if (users.products.isEmpty()) {
                                tvFavEmpty.visibility = View.VISIBLE
                            } else
                                initRecycleView(users.products)
                        }
                    }
                    Status.LOADING -> {
                        swipeRefresh?.isRefreshing = true
                        tvFavEmpty.visibility = View.GONE

                    }
                    Status.ERROR -> {
                        swipeRefresh?.isRefreshing = false
                        tvFavEmpty.visibility = View.GONE
                        getMaterialDialogInstance(it.message!!)
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

}