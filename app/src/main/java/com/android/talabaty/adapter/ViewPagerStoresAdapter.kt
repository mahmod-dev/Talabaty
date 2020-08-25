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
import com.android.talabaty.model.MyActivity
import com.android.talabaty.model.ViewStores
import com.android.talabaty.retrofit.ApiHelperImpl
import com.android.talabaty.retrofit.RetrofitBuilder
import com.android.talabaty.util.Helper
import com.android.talabaty.viewModel.StoresViewModel
import com.mahmoud.todoapp.util.dbUtil.Status
import kotlinx.android.synthetic.main.item_stores.view.*


class ViewPagerStoresAdapter(
    var activity: Activity,
    var data: List<MyActivity>
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val TAG = "ViewPagerStoresAdapter"
    var mListener: OnItemClickListener? = null
    var adapter: RecycleStoresAdapter? = null
    private lateinit var viewModel: StoresViewModel

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


    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        i: Int
    ): RecyclerView.ViewHolder {
        val view: View?
        if (i == 0) {
            view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.item_all, viewGroup, false)
            return ViewHolderAll(view)
        } else {
            view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.item_stores, viewGroup, false)
            return ViewHolder(view)
        }

    }

    override fun getItemViewType(position: Int): Int {
        if (position == 0) {
            return 0
        } else
            return 1
    }

    override fun onBindViewHolder(
        viewHolder: RecyclerView.ViewHolder,
        i: Int
    ) {

        if (viewHolder.itemViewType == 0) {
            val allView = viewHolder as ViewHolderAll

        } else {
            val holder = viewHolder as ViewHolder
            setupObserver(holder.rvStore, holder.progressBar)

        }

    }

    override fun getItemCount(): Int = data.size


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


//        var viewPagerStore: ViewPager2 = itemView.viewPagerStore
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


    inner class ViewHolderAll(itemView: View) : RecyclerView.ViewHolder(itemView) {


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


    private fun initRecycleView(rv: RecyclerView, viewStores: ViewStores) {

        adapter = RecycleStoresAdapter(activity, viewStores)
        rv.layoutManager = LinearLayoutManager(activity)
        rv.adapter = adapter
        rv.setHasFixedSize(true)

    }

    private fun initViewModel() {

        viewModel = ViewModelProviders.of(
            activity as FragmentActivity,
            ViewModelFactory(ApiHelperImpl(RetrofitBuilder.apiService), activity.application)
        ).get(StoresViewModel::class.java)
    }

    private fun setupObserver(rv: RecyclerView, progressBar: ProgressBar) {

        viewModel.getStoresById().observe(activity as FragmentActivity,
            Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        progressBar.visibility = View.GONE
                        it.data?.let { users ->
                            for (i in users.stores.indices) {
                                Log.e(TAG, "setupObserver: ${users.stores[i]}")
                            }
                            initRecycleView(rv, users)
                        }
                    }
                    Status.LOADING -> {
                        progressBar.visibility = View.VISIBLE

                    }
                    Status.ERROR -> {
                        progressBar.visibility = View.GONE
                        Helper.showFilterDialog(activity,it.message!!).show()
                        Log.e(TAG, "setupObserver: " + it.message)

                    }
                }

            }
        )
    }


}