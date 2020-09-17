package com.android.talabaty

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.talabaty.adapter.CouponAdapter
import com.android.talabaty.dbUtil.Status
import com.android.talabaty.dbUtil.ViewModelFactory
import com.android.talabaty.model.Coupon
import com.android.talabaty.retrofit.ApiHelperImpl
import com.android.talabaty.retrofit.RetrofitBuilder
import com.android.talabaty.util.CustomMaterialDialog.getMaterialDialogInstance
import com.android.talabaty.viewModel.CouponViewModel
import kotlinx.android.synthetic.main.activity_all_coupons.*
import kotlinx.android.synthetic.main.title_toolbar.*

class AllCouponsActivity : AppCompatActivity() {
    val TAG = "AllCouponsActivity"
    private lateinit var viewModel: CouponViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_coupons)
        handleToolbar()
        initViewModel()
        viewModel.allCoupons()
        setupObserverAll()
        swipeToRefresh()
        setupObserverDelete()
    }

    private fun handleToolbar() {
        imgArrowBack.setOnClickListener {
            finish()

        }

        tvTitleToolbar.text = getString(R.string.coupons)

    }

    private fun initViewModel() {

        viewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiHelperImpl(RetrofitBuilder.apiService), application)
        ).get(CouponViewModel::class.java)
    }

    private fun setupObserverAll() {

        viewModel.getAllCoupons().observe(this,
            Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        swipeRefresh?.isRefreshing = false
                        it.data?.let { users ->
                            if (users.coupons.isEmpty()) {
                                tvNotFound.visibility = View.VISIBLE
                            } else
                                initRecycleView(users.coupons)
                        }
                    }
                    Status.LOADING -> {
                        swipeRefresh?.isRefreshing = true
                        tvNotFound.visibility = View.GONE
                    }
                    Status.ERROR -> {
                        tvNotFound.visibility = View.GONE
                        swipeRefresh?.isRefreshing = false
                        getMaterialDialogInstance(it.message!!)


                    }
                }

            }
        )
    }

    private fun setupObserverDelete() {

        viewModel.getDeleteCoupon().observe(this,
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


                    }
                }

            }
        )
    }

    private fun initRecycleView(data: ArrayList<Coupon>) {
        val adapter = CouponAdapter(this, data)
        val linearLayoutManager = LinearLayoutManager(this)
        rvCoupons.layoutManager = linearLayoutManager
        rvCoupons.adapter = adapter
        rvCoupons.setHasFixedSize(true)

        adapter.onItemClick = { position, coupon ->

            viewModel.deleteCoupon(coupon)
            data.removeAt(position)
            adapter.notifyDataSetChanged()

        }
    }

    private fun swipeToRefresh() {
        swipeRefresh?.setOnRefreshListener {

            viewModel.allCoupons()
        }

    }

}