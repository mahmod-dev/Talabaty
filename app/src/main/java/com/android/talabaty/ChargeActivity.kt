package com.android.talabaty

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.android.talabaty.dbUtil.Status
import com.android.talabaty.dbUtil.ViewModelFactory
import com.android.talabaty.retrofit.ApiHelperImpl
import com.android.talabaty.retrofit.RetrofitBuilder
import com.android.talabaty.viewModel.CouponViewModel
import kotlinx.android.synthetic.main.activity_charge.*
import kotlinx.android.synthetic.main.title_toolbar.*

class ChargeActivity : AppCompatActivity() {
    val TAG = "ChargeActivity"
    private lateinit var viewModel: CouponViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_charge)

        handleToolbar()
        initViewModel()

        btnConfirm.setOnClickListener {
            val coupon = etAddAmount.text.toString()
            if (coupon.isEmpty()){
                etAddAmount.error = getString(R.string.empty)
                return@setOnClickListener
            }
           val amount =coupon.toInt()
            viewModel.chargeWallet(amount)

        }

        setupObserver()

    }


    private fun handleToolbar() {
        imgArrowBack.setOnClickListener {
            finish()

        }

        tvTitleToolbar.text = getString(R.string.add_amount)

    }


    private fun initViewModel() {

        viewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiHelperImpl(RetrofitBuilder.apiService), application)
        ).get(CouponViewModel::class.java)
    }

    private fun setupObserver() {

        viewModel.getChargeWallet().observe(this,
            Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        progressBar.visibility = View.GONE

                        it.data?.let { users ->
                            tvEmpty.visibility = View.VISIBLE
                            tvEmpty.setTextColor(ContextCompat.getColor(this,R.color.colorPrimary))
                            tvEmpty.text = getString(R.string.add_amount_success)
                            etAddAmount.setText("")
                        }
                    }
                    Status.LOADING -> {
                        tvEmpty.visibility = View.GONE
                        progressBar.visibility = View.VISIBLE

                    }
                    Status.ERROR -> {
                        progressBar.visibility = View.GONE
                        tvEmpty.visibility = View.VISIBLE
                        tvEmpty.setTextColor(ContextCompat.getColor(this,R.color.colorRed3))
                        tvEmpty.text= it.message


                    }
                }

            }
        )
    }

}