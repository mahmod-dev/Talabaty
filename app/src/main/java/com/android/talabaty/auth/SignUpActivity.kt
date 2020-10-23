package com.android.talabaty.auth

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.android.talabaty.R
import com.android.talabaty.dbUtil.ViewModelFactory
import com.android.talabaty.model.SignUpPost
import com.android.talabaty.retrofit.ApiHelperImpl
import com.android.talabaty.retrofit.RetrofitBuilder
import com.android.talabaty.util.CustomAlertDialog.getDialogInstance
import com.android.talabaty.util.Helper
import com.android.talabaty.util.Helper.emailValid
import com.android.talabaty.util.MyPreferences
import com.android.talabaty.viewModel.SignUpViewModel
import com.android.talabaty.dbUtil.Status
import com.android.talabaty.util.CustomMaterialDialog.getMaterialDialogInstance
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.logo_toolbar_back.*

class SignUpActivity : AppCompatActivity() {
    val TAG ="SignUpActivity"
    var login: TextView? = null
    private lateinit var viewModel: SignUpViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        MyPreferences.context = applicationContext
        btnLogin.setOnClickListener { startActivity(Intent(applicationContext, SignInActivity::class.java)) }
        initViewModel()
        btnRegister.setOnClickListener {
          val email =  etEmail.text.toString()
          val mobile =  etMobile.text.toString()
          val password =  etPassword.text.toString()
          val username =  etUsername.text.toString()
            val deviceType = "android"
            val user  = SignUpPost(username,email,mobile,password,"1wdasdasdasd",deviceType)

           if ( validationInput(username,email,mobile,password)){
               return@setOnClickListener
           }else{
               viewModel.signUp(user)

           }


        }
        setupObserver()

        imgArrowBack.setOnClickListener {
            finish()
        }
    }

    private fun initViewModel() {

        viewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiHelperImpl(RetrofitBuilder.apiService), application)
        ).get(SignUpViewModel::class.java)
    }


    private fun setupObserver() {
        val dialog = getDialogInstance()

        viewModel.getSignUp().observe(this,
            Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        dialog.dismiss()

                        it.data?.let { users ->
                            MyPreferences.setStr("userToken",users.user.access_token)
                            MyPreferences.setStr("userMobile",users.user.mobile)
                            startActivity(Intent(applicationContext, VerificationActivity::class.java))

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

    private fun validationInput(username:String,email:String,mobile:String,password:String): Boolean{

        if (username.isEmpty()){
            etUsername.error = getString(R.string.empty)
            return true
        }

        if (email.isEmpty()){
            etEmail.error = getString(R.string.empty)
            return true
        }

        if (password.isEmpty()){
            etPassword.error = getString(R.string.empty)
            return true
        }

        if (mobile.isEmpty()){
            etMobile.error = getString(R.string.empty)
            return true
        }

        if (!emailValid(email)){
            etEmail.error = getString(R.string.email_valid)
            return true
        }

        return false
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