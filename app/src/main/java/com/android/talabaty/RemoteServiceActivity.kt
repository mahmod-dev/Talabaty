package com.android.talabaty

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.talabaty.adapter.CartAdapter
import com.android.talabaty.adapter.RemoteServiceAdapter
import com.android.talabaty.dbUtil.Status
import com.android.talabaty.dbUtil.ViewModelFactory
import com.android.talabaty.model.Cart
import com.android.talabaty.model.Digital
import com.android.talabaty.retrofit.ApiHelperImpl
import com.android.talabaty.retrofit.RetrofitBuilder
import com.android.talabaty.viewModel.DigitalServiceViewModel
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.activity_remote_service.*
import kotlinx.android.synthetic.main.activity_remote_service.swipeRefresh
import kotlinx.android.synthetic.main.activity_remote_service.tvNotFound
import kotlinx.android.synthetic.main.title_toolbar.*

class RemoteServiceActivity : AppCompatActivity() {
    val TAG = "RemoteServiceActivity"
    private lateinit var viewModel: DigitalServiceViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_remote_service)
        initViewModel()
        viewModel.digitals()
        handleToolbar()
        swipeToRefresh()
        setupObserver()
    }

    private fun handleToolbar() {
        imgArrowBack.setOnClickListener {
            finish()

        }

        tvTitleToolbar.text = getString(R.string.digital_services)

    }


    private fun initViewModel() {

        viewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiHelperImpl(RetrofitBuilder.apiService), application)
        ).get(DigitalServiceViewModel::class.java)
    }

    private fun setupObserver() {

        viewModel.getAllDigitals().observe(this,
            Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        tvNotFound.visibility = View.GONE
                        swipeRefresh.isRefreshing = false

                        it.data?.let { users ->
                            if (users.digitals.isEmpty()) {
                                tvNotFound.visibility = View.VISIBLE
                            }else{
                                initRecycleView(users.digitals)
                            }

                        }
                    }
                    Status.LOADING -> {
                        tvNotFound.visibility = View.GONE

                        swipeRefresh.isRefreshing = true
                    }
                    Status.ERROR -> {
                        tvNotFound.visibility = View.GONE

                        swipeRefresh.isRefreshing = false

                    }
                }

            }
        )
    }

    private fun swipeToRefresh() {
        swipeRefresh?.setOnRefreshListener {
            setupObserver()
        }

    }

    private fun initRecycleView(data: List<Digital>) {

        val adapter = RemoteServiceAdapter(this, data)
        val linearLayoutManager = GridLayoutManager(this, 3)
        rvRemote.layoutManager = linearLayoutManager
        rvRemote.adapter = adapter
        rvRemote.setHasFixedSize(true)


//        adapter.setOnClickListener(object : RemoteServiceAdapter.OnItemClickListener{
//            override fun onItemClick(position: Int) {
//                Toast.makeText(this@RemoteServiceActivity,position,Toast.LENGTH_SHORT).show()
//            }
//
//            override fun onItemLongClick(position: Int) {
//                Toast.makeText(this@RemoteServiceActivity,position,Toast.LENGTH_SHORT).show()
//            }
//        })


    }

}