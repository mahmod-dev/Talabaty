package com.android.talabaty.fragment.mainTab


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.talabaty.R
import com.android.talabaty.adapter.RecycleStoresAdapter
import com.android.talabaty.dbUtil.Status
import com.android.talabaty.dbUtil.ViewModelFactory
import com.android.talabaty.model.Store
import com.android.talabaty.retrofit.ApiHelperImpl
import com.android.talabaty.retrofit.RetrofitBuilder
import com.android.talabaty.util.Helper
import com.android.talabaty.viewModel.StoresViewModel

class OthersFragment(var position: Int) : Fragment() {
    val TAG = "OthersFragment"
    private lateinit var viewModel: StoresViewModel
    private lateinit var data: ArrayList<Store>
    var swipeRefresh: SwipeRefreshLayout? = null
    var tvNotFound: TextView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        initViewModel()
        val root = inflater.inflate(R.layout.fragment_others, container, false)
        val rvStore = root.findViewById<RecyclerView>(R.id.rvStore)
         swipeRefresh = root.findViewById(R.id.swipeRefresh)
        tvNotFound = root.findViewById(R.id.tvNotFound)
        data = ArrayList()
        setupObserver(rvStore)
        swipeToRefresh(rvStore)

        return root
    }

    private fun setupObserver(rv: RecyclerView) {
        tvNotFound?.visibility = View.GONE
        viewModel.getStoresById().observe(viewLifecycleOwner!!,
            Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        swipeRefresh?.isRefreshing = false
                        tvNotFound?.visibility = View.GONE
                        it.data?.let { users ->
                            data.clear()
                            data.addAll(users.stores)
                            if (users.stores.isEmpty()){
                                tvNotFound?.visibility = View.VISIBLE
                            }
                            initRecycleView(rv)
                        }
                    }
                    Status.LOADING -> {
                        tvNotFound?.visibility = View.GONE
                        data.clear()
                        swipeRefresh?.isRefreshing = true
                        initRecycleView(rv)

                    }
                    Status.ERROR -> {
                        tvNotFound?.visibility = View.GONE
                        swipeRefresh?.isRefreshing = false
                        Helper.showFilterDialog(activity!!, it.message!!).show()
                        Log.e(TAG, "setupObserver: " + it.message)
                    }
                }
            }
        )
    }

    private fun initViewModel() {

        viewModel = ViewModelProviders.of(
            activity!!,
            ViewModelFactory(ApiHelperImpl(RetrofitBuilder.apiService), activity!!.application)
        ).get(StoresViewModel::class.java)
    }


    private fun initRecycleView(rv: RecyclerView) {
        if ( activity != null) {
            val adapter = RecycleStoresAdapter(activity!!, data)
            rv.layoutManager = LinearLayoutManager(activity)
            rv.adapter = adapter
            rv.setHasFixedSize(true)
        }
    }

    private fun swipeToRefresh(rv:RecyclerView){
        swipeRefresh?.setOnRefreshListener {

            setupObserver(rv)
        }

    }


}