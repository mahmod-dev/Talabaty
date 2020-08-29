package com.android.talabaty.adapter

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager2.widget.ViewPager2
import com.android.talabaty.R
import com.android.talabaty.dbUtil.ViewModelFactory
import com.android.talabaty.model.*
import com.android.talabaty.retrofit.ApiHelperImpl
import com.android.talabaty.retrofit.RetrofitBuilder
import com.android.talabaty.viewModel.CategoriesViewModel
import com.android.talabaty.dbUtil.Status
import kotlinx.android.synthetic.main.item_stores_category.view.*


class ViewPagerCategoriesAdapter(var activity: Activity, var data: ArrayList<Category>,var swipeRefresh:SwipeRefreshLayout) :
    RecyclerView.Adapter<ViewPagerCategoriesAdapter.ViewHolder>() {
    private val TAG = "ViewPagerStoresAdapter"
    var mListener: OnItemClickListener? = null
    var viewStores: ArrayList<Product>? = null
    private lateinit var viewModel: CategoriesViewModel

    init {
        initViewModel()
        viewStores = ArrayList()
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
        fun onItemLongClick(position: Int)
    }

    fun setOnClickListener(listener: OnItemClickListener?) {
        mListener = listener
    }


    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        i: Int
    ): ViewHolder {
        val view =
            LayoutInflater.from(viewGroup.context).inflate(R.layout.item_stores_category, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(
        viewHolder: ViewHolder,
        i: Int
    ) {
        setupObserver(viewHolder.rvStore)
        swipeToRefresh(viewHolder.rvStore)

    }

    override fun getItemCount(): Int = data.size


    inner class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var rvStore: RecyclerView = itemView.rvStore


        init {
            itemView.setOnClickListener {
                if (mListener != null) {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        mListener!!.onItemClick(position)
                    }
                }
            }

            itemView.setOnLongClickListener {
                if (mListener != null) {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        mListener!!.onItemLongClick(position)
                    }
                }
                false

            }
        }
    }



    private fun initRecycleView(rv: RecyclerView) {

        val adapter = RecycleCategoryAdapter(activity,viewStores!!)
        rv.layoutManager = LinearLayoutManager(activity)
        rv.adapter = adapter
        rv.setHasFixedSize(true)
    }

    private fun initViewModel() {

        viewModel = ViewModelProviders.of(
            activity as FragmentActivity,
            ViewModelFactory(ApiHelperImpl(RetrofitBuilder.apiService), activity.application)
        ).get(CategoriesViewModel::class.java)
    }


    private fun setupObserver(rv: RecyclerView) {

        viewModel.getStoresById().observe(activity as FragmentActivity,
            Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        swipeRefresh.isRefreshing= false

                        it.data?.let { users ->
                            viewStores = users.products
                            for (i in users.products.indices) {
                                Log.e(TAG, "setupObserver: ${users.products[i]}")
                            }
                            initRecycleView(rv)
                        }
                    }
                    Status.LOADING -> {
                      viewStores?.clear()
                        swipeRefresh.isRefreshing= true
                        initRecycleView(rv)

                    }
                    Status.ERROR -> {
                        swipeRefresh.isRefreshing= false
                        //Handle Error
                        Log.e(TAG, "setupObserver: " + it.message)

                    }
                }

            }
        )
    }


    private fun swipeToRefresh(rv:RecyclerView){
        swipeRefresh.setOnRefreshListener {

            setupObserver(rv)
        }

    }

}