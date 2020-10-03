package com.android.talabaty.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.talabaty.R
import com.android.talabaty.adapter.CartAdapter
import com.android.talabaty.adapter.OrdersAdapter
import com.android.talabaty.dbUtil.Status
import com.android.talabaty.dbUtil.ViewModelFactory
import com.android.talabaty.model.Cart
import com.android.talabaty.model.ClientOrder
import com.android.talabaty.model.GetClientOrders
import com.android.talabaty.retrofit.ApiHelperImpl
import com.android.talabaty.retrofit.RetrofitBuilder
import com.android.talabaty.util.CustomMaterialDialog.getMaterialDialogInstance
import com.android.talabaty.util.Helper
import com.android.talabaty.viewModel.OrdersViewModel

class OrdersFragment : Fragment() {
    private lateinit var viewModel: OrdersViewModel
    var swipeRefresh: SwipeRefreshLayout? = null
    var rvOrders: RecyclerView? = null
    var progressBar: ProgressBar? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_orders, container, false)
        swipeRefresh = view.findViewById(R.id.swipeRefresh)
        rvOrders = view.findViewById(R.id.rvOrders)
        progressBar = view.findViewById(R.id.progressBar)
        initViewModel()
        viewModel.clientOrders()
        setupObserver()
        setupObserverCancelOrder()
        swipeToRefresh()
        return view
    }

    private fun initViewModel() {

        viewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiHelperImpl(RetrofitBuilder.apiService), activity!!.application)
        ).get(OrdersViewModel::class.java)
    }

    private fun setupObserver() {
        viewModel.getClientOrders().observe(this,
            Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        swipeRefresh?.isRefreshing = false

                        // progressBar?.visibility = View.GONE
                        it.data?.let { users ->
                            initRecycleView(users.client_orders)
                        }
                    }
                    Status.LOADING -> {
                        swipeRefresh?.isRefreshing = true
                        //  progressBar?.visibility = View.VISIBLE

                    }
                    Status.ERROR -> {
                        swipeRefresh?.isRefreshing = false

                        // progressBar?.visibility = View.GONE
                        activity?.getMaterialDialogInstance(it.message!!)

                    }
                }

            }
        )
    }

    private fun setupObserverCancelOrder() {
        viewModel.getCancelOrder().observe(this,
            Observer {
                when (it.status) {
                    Status.SUCCESS -> {

                        progressBar?.visibility = View.GONE
                        it.data?.let { users ->
                            Toast.makeText(activity, users.message, Toast.LENGTH_SHORT).show()
                            viewModel.clientOrders()
                        }
                    }
                    Status.LOADING -> {
                        progressBar?.visibility = View.VISIBLE
                    }
                    Status.ERROR -> {
                        progressBar?.visibility = View.GONE
                        activity?.getMaterialDialogInstance(it.message!!)
                    }
                }
            }
        )
    }

    private fun swipeToRefresh() {
        swipeRefresh?.setOnRefreshListener {
            viewModel.clientOrders()
        }
    }

    private fun initRecycleView(orders: ArrayList<ClientOrder>) {
        val adapter = OrdersAdapter(activity!!, orders)
        val linearLayoutManager = LinearLayoutManager(context)
        rvOrders?.layoutManager = linearLayoutManager
        rvOrders?.adapter = adapter
        rvOrders?.setHasFixedSize(true)

        adapter!!.onItemClick = { order, position ->
            Helper.dialogConfirm(
                activity!!,
                getString(R.string.cancel_order_question),
                getString(R.string.confirm),
                getString(R.string.back)
            )
            Helper.onItemClick = {
                viewModel.cancelOrder(order.id)
            }
        }
    }

}