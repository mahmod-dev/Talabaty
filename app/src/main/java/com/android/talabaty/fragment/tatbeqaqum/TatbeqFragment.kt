package com.android.talabaty.fragment.tatbeqaqum

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.talabaty.ProductDetailsActivity
import com.android.talabaty.R
import com.android.talabaty.adapter.ProductsAdapter
import com.android.talabaty.dbUtil.Status
import com.android.talabaty.dbUtil.ViewModelFactory
import com.android.talabaty.model.Product
import com.android.talabaty.retrofit.ApiHelperImpl
import com.android.talabaty.retrofit.RetrofitBuilder
import com.android.talabaty.util.CustomMaterialDialog.getMaterialDialogInstance
import com.android.talabaty.viewModel.StoresViewModel
import kotlinx.android.synthetic.main.fragment_tatbeq.*


class TatbeqFragment(var position: Int) : Fragment() {
    val TAG = "TatbeqFragment"
    private lateinit var viewModel: StoresViewModel
    var swipeRefresh: SwipeRefreshLayout? = null
    var rvProducts: RecyclerView? = null
    private lateinit var data: ArrayList<Product>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_tatbeq, container, false)
        swipeRefresh = view.findViewById(R.id.swipeRefresh)
        rvProducts = view.findViewById(R.id.rvProducts)
        data = ArrayList()
        initViewModel()
        viewModel.tatbeqProductById(position)
        setupObserver()
        swipeToRefresh()
        return view
    }

    private fun setupObserver() {
        viewModel.getAllTatbeqakumProducts().observe(this,
            Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        swipeRefresh?.isRefreshing = false
                        it.data?.let { users ->
                         //   data.clear()
                            if (users.products.isNotEmpty()){
                                data.addAll(users.products)
                                initRecycleView()

                            }else{
                                tvNotFound.visibility = View.VISIBLE

                            }

                        }
                    }
                    Status.LOADING -> {
                        tvNotFound.visibility = View.GONE

                        data.clear()
                        swipeRefresh?.isRefreshing = true
                        initRecycleView()

                    }
                    Status.ERROR -> {
                        tvNotFound.visibility = View.GONE
                        data.clear()

                        swipeRefresh?.isRefreshing = false
                        activity?.getMaterialDialogInstance(it.message!!)
                    }
                }
            }
        )
    }

    private fun initViewModel() {

        viewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiHelperImpl(RetrofitBuilder.apiService), activity!!.application)
        ).get(StoresViewModel::class.java)
    }


    private fun initRecycleView() {
        val adapter = ProductsAdapter(activity!!, data)
        rvProducts?.layoutManager = GridLayoutManager(activity, 2)
        rvProducts?.adapter = adapter
        rvProducts?.setHasFixedSize(true)

        adapter.onItemClick = { product ->
            val intent = Intent(activity!!,ProductDetailsActivity::class.java)
            intent.putExtra("has_colors",product.has_colors)
            intent.putExtra("has_sizes", product.has_sizes)
            intent.putExtra("productId", product.id)
            startActivity(intent)

        }
    }

    private fun swipeToRefresh() {
        swipeRefresh?.setOnRefreshListener {
            viewModel.tatbeqProductById(position)
        }

    }

}