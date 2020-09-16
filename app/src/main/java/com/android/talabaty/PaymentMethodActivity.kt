package com.android.talabaty

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.android.talabaty.adapter.PaymentMethodAdapter
import com.android.talabaty.adapter.SliderAdapter
import com.android.talabaty.dbUtil.Status
import com.android.talabaty.dbUtil.ViewModelFactory
import com.android.talabaty.model.PaymentMethod
import com.android.talabaty.retrofit.ApiHelperImpl
import com.android.talabaty.retrofit.RetrofitBuilder
import com.android.talabaty.util.CustomMaterialDialog.getMaterialDialogInstance
import com.android.talabaty.viewModel.CardPaymentViewModel
import com.fuzz.indicator.CutoutViewIndicator
import kotlinx.android.synthetic.main.activity_payment_method.*
import kotlinx.android.synthetic.main.activity_payment_method.swipeRefresh
import kotlinx.android.synthetic.main.fragment_all.*

class PaymentMethodActivity : AppCompatActivity() {
    val TAG = "PaymentMethodActivity"
    private lateinit var viewModel: CardPaymentViewModel

    var adapter: SliderAdapter? = null
    var paymentAdapter: PaymentMethodAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_method)
        val indicator = findViewById<CutoutViewIndicator>(R.id.indicator_details)
        initViewModel()
        viewModel.paymentMethod()
        //   indicator.setViewPager(pager)
        setupObserver()
        swipeToRefresh()

        imgArrowBack.setOnClickListener {
            finish()
        }
    }

    private fun initViewModel() {

        viewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiHelperImpl(RetrofitBuilder.apiService), application)
        ).get(CardPaymentViewModel::class.java)
    }

    private fun setupObserver() {

        viewModel.getPaymentMethod().observe(this,
            Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        it.data?.let { users ->
                            swipeRefresh.isRefreshing = false
                            initRecycleView(users.payment_methods)
//                            startActivity(Intent(applicationContext, WaitingActivity::class.java))
//                            finish()

                        }
                    }
                    Status.LOADING -> {
                        swipeRefresh.isRefreshing = true
                    }
                    Status.ERROR -> {
                        swipeRefresh.isRefreshing = false

                        getMaterialDialogInstance(it.message!!)

                    }
                }

            }
        )
    }


    private fun initRecycleView(methods: ArrayList<PaymentMethod>) {
        paymentAdapter = PaymentMethodAdapter(this, methods)
        val linearLayoutManager = LinearLayoutManager(this)
        rvPaymentMethod.layoutManager = linearLayoutManager
        rvPaymentMethod.adapter = paymentAdapter
        rvPaymentMethod.setHasFixedSize(true)

        paymentAdapter!!.onItemClick = { paymentId ->
            Log.e(TAG, "paymentId: $paymentId ")
            val intent = (Intent(applicationContext, AddCreditCardActivity::class.java))
            intent.putExtra("paymentId", paymentId)
            startActivity(intent)
          //  finish()

        }
    }

    private fun swipeToRefresh() {
        swipeRefresh?.setOnRefreshListener {
            viewModel.paymentMethod()
        }
    }


//    private fun initViewPager(){
//
//        adapter = SliderAdapter(activity!!, arrImages)
//        viewPager?.adapter = adapter
//        if (idd==0)
//            indicator?.setViewPager(viewPager)
//
//        viewPager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
//            override fun onPageScrolled(position: Int, v: Float, i1: Int) {}
//            override fun onPageSelected(position: Int) {}
//            override fun onPageScrollStateChanged(state: Int) {
//                enableDisableSwipeRefresh(state == ViewPager.SCROLL_STATE_IDLE)
//            }
//        })
//    }

    private fun enableDisableSwipeRefresh(enable: Boolean) {
        if (swipeRefresh != null) {
            swipeRefresh?.isEnabled = enable
        }
    }


}