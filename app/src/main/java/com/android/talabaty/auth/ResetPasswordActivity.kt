package com.android.talabaty.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.android.talabaty.R
import com.android.talabaty.dbUtil.ViewModelFactory
import com.android.talabaty.retrofit.ApiHelperImpl
import com.android.talabaty.retrofit.RetrofitBuilder
import com.android.talabaty.util.CustomAlertDialog.getDialogInstance
import com.android.talabaty.util.Helper
import com.android.talabaty.viewModel.LoginViewModel
import com.android.talabaty.dbUtil.Status
import kotlinx.android.synthetic.main.activity_reset_password.*

class ResetPasswordActivity : AppCompatActivity() {
    val TAG = "ResetPasswordActivity"
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)
        initViewModel()

        btnReset.setOnClickListener {

            if (etReSetEmail.text.toString().isEmpty())
                return@setOnClickListener

            viewModel.forgotPassword(etReSetEmail.text.toString())

        }

        setupObserver()
    }


    private fun initViewModel() {

        viewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiHelperImpl(RetrofitBuilder.apiService), application)
        ).get(LoginViewModel::class.java)
    }


    private fun setupObserver() {
        val dialog = getDialogInstance()

        viewModel.getForgotPassword().observe(this,
            Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        dialog.dismiss()

                        it.data?.let { users ->
                            //dialog
                            startActivity(Intent(applicationContext, SignInActivity::class.java))

                        }
                    }
                    Status.LOADING -> {
                        dialog.show()

                    }
                    Status.ERROR -> {
                        //Handle Error
                        dialog.dismiss()
                        Helper.showFilterDialog(this!!, it.message!!).show()

                        Log.e(TAG, "setupObserver: " + it.message)
                    }
                }

            }
        )
    }
}
