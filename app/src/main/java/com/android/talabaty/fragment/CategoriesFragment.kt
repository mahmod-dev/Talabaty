package com.android.talabaty.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.talabaty.R
import com.android.talabaty.adapter.NewestOffersAdapter
import com.android.talabaty.adapter.OtherServicesAdapter
import com.android.talabaty.dbUtil.Status
import com.android.talabaty.dbUtil.ViewModelFactory
import com.android.talabaty.model.Digital
import com.android.talabaty.model.GetOffers
import com.android.talabaty.model.Offer
import com.android.talabaty.model.OtherServices
import com.android.talabaty.retrofit.ApiHelperImpl
import com.android.talabaty.retrofit.RetrofitBuilder
import com.android.talabaty.util.CustomMaterialDialog.getMaterialDialogInstance
import com.android.talabaty.viewModel.AllMainViewModel
import com.android.talabaty.viewModel.CategoryViewModel

class CategoriesFragment : Fragment() {
    var rvOtherServices: RecyclerView? = null
    var swipeRefresh: SwipeRefreshLayout? = null
    private lateinit var viewModel: CategoryViewModel
    private lateinit var data: ArrayList<Digital>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_categories, container, false)
        rvOtherServices = view.findViewById(R.id.rvOtherServices)
        swipeRefresh = view.findViewById(R.id.swipeRefresh)
        data = ArrayList()
        initViewModel()
        viewModel.otherServices()
        setupObserver(rvOtherServices!!)
        swipeToRefresh(rvOtherServices!!)
        return view
    }


    private fun initViewModel() {

        viewModel = ViewModelProviders.of(
            activity!!,
            ViewModelFactory(ApiHelperImpl(RetrofitBuilder.apiService), activity!!.application)
        ).get(CategoryViewModel::class.java)
    }


    private fun swipeToRefresh(rv: RecyclerView) {
        swipeRefresh?.setOnRefreshListener {

            setupObserver(rv)
        }

    }

    private fun setupObserver(rv: RecyclerView) {
        viewModel.getOtherServices().observe(this,
            Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        it.data?.let { users ->
                            data.clear()
                            swipeRefresh?.isRefreshing = false
                            data.addAll(users.digitals)

                            initRecycleView(rv)
                        }
                    }
                    Status.LOADING -> {
                        swipeRefresh?.isRefreshing = true
                        data.clear()
                      //  initRecycleView(rv)

                    }
                    Status.ERROR -> {
                        data.clear()
                        swipeRefresh?.isRefreshing = false
                        activity!!.getMaterialDialogInstance(it.message!!)
                    }
                }
            }
        )
    }

    private fun initRecycleView(rv: RecyclerView) {
        val adapter = OtherServicesAdapter(activity!!, data)
        rv.layoutManager = GridLayoutManager(activity!!,3)
        rv.adapter = adapter
        rv.setHasFixedSize(true)

    }

}