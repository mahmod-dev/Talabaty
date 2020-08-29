package com.android.talabaty.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.android.talabaty.MainActivity
import com.android.talabaty.R
import com.android.talabaty.dbUtil.ViewModelFactory
import com.android.talabaty.retrofit.ApiHelperImpl
import com.android.talabaty.retrofit.RetrofitBuilder
import com.android.talabaty.util.CustomAlertDialog.getDialogInstance
import com.android.talabaty.util.Helper
import com.android.talabaty.util.MyPreferences
import com.android.talabaty.viewModel.SignUpViewModel
import com.android.talabaty.dbUtil.Status
import kotlinx.android.synthetic.main.activity_verification.*

class VerificationActivity : AppCompatActivity() {
    val TAG = "VerificationActivity"
    private lateinit var viewModel: SignUpViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verification)
        MyPreferences.context = applicationContext
        initViewModel()
        var mobile = MyPreferences.getStr("userMobile")
        if (mobile.isNullOrEmpty())
            mobile = "111"

        btnConfirm?.setOnClickListener {

            if (tvCode.text.toString().isEmpty()) {
                tvCode.error = getString(R.string.empty)
                return@setOnClickListener
            }




         val code =    tvCode.text.toString().toInt()
            viewModel.sendCode(code, mobile)
        }

        tvResendCode.setOnClickListener {
            viewModel.reSendCode("0597116675")
        }
        setupObserver()
        setupObserverReSend()
    }

    private fun initViewModel() {

        viewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiHelperImpl(RetrofitBuilder.apiService), application)
        ).get(SignUpViewModel::class.java)
    }


    private fun setupObserver() {
        val dialog = getDialogInstance()

        viewModel.sendCode().observe(this,
            Observer {
                when (it.status) {
                    Status.SUCCESS -> {

                        it.data?.let { users ->
                            startActivity(Intent(applicationContext, MainActivity::class.java))

                        }
                    }
                    Status.LOADING -> {

                    }
                    Status.ERROR -> {
                        //Handle Error
                        Helper.showFilterDialog(this!!, it.message!!).show()

                        Log.e(TAG, "setupObserver: " + it.message)
                    }
                }

            }
        )
    }

    private fun setupObserverReSend() {
        val dialog = getDialogInstance()

        viewModel.reSendCode().observe(this,
            Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        dialog.dismiss()

                        it.data?.let { users ->


                            //show dialog

                        }
                    }
                    Status.LOADING -> {
                        dialog.show()

                    }
                    Status.ERROR -> {
                        dialog.dismiss()
                        Helper.showFilterDialog(this!!, it.message!!).show()

                        //Handle Error
                        Log.e(TAG, "setupObserver: " + it.message)
                    }
                }

            }
        )
    }


}