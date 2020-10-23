package com.android.talabaty.auth

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.IntentCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.android.talabaty.MainActivity
import com.android.talabaty.R
import com.android.talabaty.dbUtil.Status
import com.android.talabaty.dbUtil.ViewModelFactory
import com.android.talabaty.retrofit.ApiHelperImpl
import com.android.talabaty.retrofit.RetrofitBuilder
import com.android.talabaty.util.CustomAlertDialog.getDialogInstance
import com.android.talabaty.util.CustomMaterialDialog.getMaterialDialogInstance
import com.android.talabaty.util.MyPreferences
import com.android.talabaty.viewModel.SignUpViewModel
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


            val code = tvCode.text.toString().toInt()
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

        viewModel.sendCode().observe(this,
            Observer {
                when (it.status) {
                    Status.SUCCESS -> {

                        it.data?.let { users ->
                            Intent(applicationContext, MainActivity::class.java).apply {
                                addFlags(
                                    Intent.FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK
                                )
                                startActivity(this)
                            }

                            finish()
                        }
                    }
                    Status.LOADING -> {

                    }
                    Status.ERROR -> {
                        //Handle Error
                        getMaterialDialogInstance(it.message!!)

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

                        }
                    }
                    Status.LOADING -> {
                        dialog.show()

                    }
                    Status.ERROR -> {
                        dialog.dismiss()
                        getMaterialDialogInstance(it.message!!)

                        //Handle Error
                        Log.e(TAG, "setupObserver: " + it.message)
                    }
                }

            }
        )
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val v: View? = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    v.clearFocus()
                    val imm: InputMethodManager =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0)
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }


}