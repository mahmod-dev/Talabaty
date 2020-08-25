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
import androidx.viewpager2.widget.ViewPager2
import com.android.talabaty.R
import com.android.talabaty.dbUtil.ViewModelFactory
import com.android.talabaty.model.*
import com.android.talabaty.retrofit.ApiHelperImpl
import com.android.talabaty.retrofit.RetrofitBuilder
import com.android.talabaty.viewModel.CategoriesViewModel
import com.android.talabaty.viewModel.StoresViewModel
import com.mahmoud.todoapp.util.dbUtil.Status
import kotlinx.android.synthetic.main.item_stores.view.*


class ViewPagerCategoriesAdapter(var activity: Activity, var data: List<Category>) :
    RecyclerView.Adapter<ViewPagerCategoriesAdapter.ViewHolder>() {
    private val TAG = "ViewPagerStoresAdapter"
    var mListener: OnItemClickListener? = null
    private lateinit var viewModel: CategoriesViewModel

    init {
        initViewModel()
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
        fun onItemLongClick(position: Int)
    }

    fun setOnClickListener(listener: OnItemClickListener?) {
        mListener = listener
    }

    fun getCategories(): List<Category> = data


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
        setupObserver(viewHolder.rvStore,viewHolder.progressBar)
    }

    override fun getItemCount(): Int = data.size


    inner class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var viewPagerStore: ViewPager2 = itemView.viewPagerStore
        var rvStore: RecyclerView = itemView.rvStore
        var progressBar: ProgressBar = itemView.progressBar


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



    private fun initRecycleView(rv: RecyclerView, viewStores: StoreProducts) {

        val adapter = RecycleCategoryAdapter(activity,viewStores)
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


    private fun setupObserver(rv: RecyclerView,progressBar: ProgressBar) {

        viewModel.getStoresById().observe(activity as FragmentActivity,
            Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        progressBar.visibility  = View.GONE

                        it.data?.let { users ->
                            for (i in users.products.indices) {
                                Log.e(TAG, "setupObserver: ${users.products[i]}")
                            }
                            initRecycleView(rv, users)
                        }
                    }
                    Status.LOADING -> {
                        progressBar.visibility  = View.VISIBLE

                    }
                    Status.ERROR -> {
                        progressBar.visibility  = View.GONE

                        //Handle Error
                        Log.e(TAG, "setupObserver: " + it.message)

                    }
                }

            }
        )
    }

}