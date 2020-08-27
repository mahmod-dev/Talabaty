package com.android.talabaty.adapter

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.talabaty.FreeDeliveryActivity
import com.android.talabaty.R
import com.android.talabaty.dbUtil.ViewModelFactory
import com.android.talabaty.model.HomePageCategory
import com.android.talabaty.model.MyActivity
import com.android.talabaty.model.ViewStores
import com.android.talabaty.retrofit.ApiHelperImpl
import com.android.talabaty.retrofit.RetrofitBuilder
import com.android.talabaty.util.Helper
import com.android.talabaty.viewModel.AllMainViewModel
import com.android.talabaty.viewModel.StoresViewModel
import com.android.talabaty.dbUtil.Status
import com.android.talabaty.model.HomePageCategories
import kotlinx.android.synthetic.main.item_all.view.*
import kotlinx.android.synthetic.main.item_stores.view.*


class ViewPagerStoresAdapter(
    var activity: Activity,
    var data: List<MyActivity>
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val TAG = "ViewPagerStoresAdapter"
    var mListener: OnItemClickListener? = null
    var adapter: RecycleStoresAdapter? = null
    var adapterMainCat: MainCategoryAdapter? = null
    private lateinit var viewModel: StoresViewModel
    private lateinit var viewModelMain: AllMainViewModel

    init {
        initViewModel()
        initViewModelMain()
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
            viewModelMain.homePageCategories()
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
            setupObserverMainCat(allView.rvCatHome)

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
        var rvCatHome: RecyclerView = itemView.rvCatHome
        var linFreeDelivery: LinearLayout = itemView.linFreeDelivery

        fun bind(act: MyActivity) {
            linFreeDelivery.setOnClickListener {
                activity.startActivity(Intent(activity, FreeDeliveryActivity::class.java))
            }
        }

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

    private fun initRecycleViewMainCat(rv: RecyclerView, data: HomePageCategories) {

        Log.e(TAG, "initRecycleViewMainCat: $data" )
        adapterMainCat = MainCategoryAdapter(activity, data)
        rv.layoutManager = LinearLayoutManager(activity)
        rv.adapter = adapterMainCat
        rv.setHasFixedSize(true)

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
                        Helper.showFilterDialog(activity, it.message!!).show()
                        Log.e(TAG, "setupObserver: " + it.message)
                    }
                }
            }
        )
    }


    private fun initViewModelMain() {

        viewModelMain = ViewModelProviders.of(
            activity as FragmentActivity,
            ViewModelFactory(ApiHelperImpl(RetrofitBuilder.apiService), activity.application)
        ).get(AllMainViewModel::class.java)
    }

    private fun setupObserverMainCat(rv: RecyclerView) {

        viewModelMain.getHomePageCategories().observe(activity as FragmentActivity,
            Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        // progressBar.visibility = View.GONE
                        it.data?.let { users ->
                            Log.e(TAG, "setupObserverMainCat: ${users.home_page_categories}")
                            initRecycleViewMainCat(rv, users)
                        }
                    }
                    Status.LOADING -> {
                        //progressBar.visibility = View.VISIBLE

                    }
                    Status.ERROR -> {
                        // progressBar.visibility = View.GONE
                        Helper.showFilterDialog(activity, it.message!!).show()
                        Log.e(TAG, "setupObserver: " + it.message)

                    }
                }

            }
        )
    }

}