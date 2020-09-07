package com.android.talabaty

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.talabaty.adapter.FavoriteCartAdapter
import com.android.talabaty.dbUtil.Status
import com.android.talabaty.dbUtil.ViewModelFactory
import com.android.talabaty.model.Product
import com.android.talabaty.retrofit.ApiHelperImpl
import com.android.talabaty.retrofit.RetrofitBuilder
import com.android.talabaty.util.CustomMaterialDialog.getMaterialDialogInstance
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
        setupObserverDeleteFav()

    }

    private fun initRecycleView(viewStores: ArrayList<Product>) {
        val adapter = FavoriteCartAdapter(this, viewStores)
        val linearLayoutManager = LinearLayoutManager(this)
        rvFav.layoutManager = linearLayoutManager
        rvFav.adapter = adapter
        rvFav.setHasFixedSize(true)


        val simpleItemTouchCallback: ItemTouchHelper.SimpleCallback = object :
            ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                //Remove swiped item from list and notify the RecyclerView
                val position = viewHolder.adapterPosition

               val product =  adapter.getProduct(position)
                viewModel.deleteFromFav(product.id)
                viewStores.removeAt(position)
                adapter.notifyDataSetChanged()
            }
        }

        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(rvFav)
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
    private fun setupObserverDeleteFav() {

        viewModel.getDeleteFav().observe(this,
            Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        it.data?.let { users ->
                            Toast.makeText(this, users.message, Toast.LENGTH_SHORT).show()
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

}