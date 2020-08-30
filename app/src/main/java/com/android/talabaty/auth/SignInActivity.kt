package com.android.talabaty.auth

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.android.talabaty.MainActivity
import com.android.talabaty.R
import com.android.talabaty.dbUtil.ViewModelFactory
import com.android.talabaty.model.LoginPost
import com.android.talabaty.retrofit.ApiHelperImpl
import com.android.talabaty.retrofit.RetrofitBuilder
import com.android.talabaty.util.CustomAlertDialog.getDialogInstance
import com.android.talabaty.util.Helper
import com.android.talabaty.util.MyPreferences
import com.android.talabaty.viewModel.LoginViewModel
import com.facebook.*
import com.facebook.AccessToken
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.android.talabaty.dbUtil.Status
import kotlinx.android.synthetic.main.activity_sign_in.*
import org.json.JSONException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class SignInActivity : AppCompatActivity() {
    private val TAG = "SignInActivity"
    var forget: TextView? = null
    private lateinit var viewModel: LoginViewModel

    var callbackManager: CallbackManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        MyPreferences.context = applicationContext
        initViewModel()
        callbackManager = CallbackManager.Factory.create()
        setUpFacebook()
        getKey()
        forget = findViewById(R.id.tvForgetPassword)

        if (MyPreferences.getInt("isLogin")==1){
            startActivity(
                Intent(
                    applicationContext,
                    MainActivity::class.java
                )
            )
            finish()

        }

        forget?.setOnClickListener {
            startActivity(
                Intent(
                    applicationContext,
                    ResetPasswordActivity::class.java
                )
            )
        }
        btnSignIn?.setOnClickListener {
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()

            if (email.isEmpty()) {
                etEmail.error = getString(R.string.empty)
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                etPassword.error = getString(R.string.empty)
                return@setOnClickListener

            }

            viewModel.login( LoginPost(email = email,password = password,fcm_token = "sssasas"))

        }

        btnFacebook.setOnClickListener {

            LoginManager.getInstance()
                .logInWithReadPermissions(this, listOf("public_profile"));
        }

        btnSignUp.setOnClickListener {
            startActivity(
                Intent(
                    applicationContext,
                    SignUpActivity::class.java
                )
            )
        }


        setupObserver()
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        callbackManager!!.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun isExpiredFacebook(): Boolean {

        val accessToken = AccessToken.getCurrentAccessToken()
        return accessToken != null && !accessToken.isExpired

    }

    private fun getKey() {
        try {
            val info = packageManager.getPackageInfo(
                "com.android.talabaty",  //Insert your own package name.
                PackageManager.GET_SIGNATURES
            )
            for (signature in info.signatures) {
                val md: MessageDigest = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Base64.encodeToString(md.digest(), Base64.DEFAULT)
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            }
        } catch (e: PackageManager.NameNotFoundException) {
        } catch (e: NoSuchAlgorithmException) {
        }
    }

    private fun getUserProfile(currentAccessToken: AccessToken) {
        val request = GraphRequest.newMeRequest(currentAccessToken) { `object`, response ->
            Log.e("TAG", `object`.toString())
            try {
                val first_name = `object`.getString("first_name")
                val last_name = `object`.getString("last_name")
                val email = `object`.getString("email")
                val id = `object`.getString("id")
                val image_url =
                    "https://graph.facebook.com/$id/picture?type=normal"
                //  txtUsername.setText("First Name: $first_name\nLast Name: $last_name")
                //  txtEmail.setText(email)
                //  Picasso.with(this@MainActivity).load(image_url).into(imageView)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
        val parameters = Bundle()
        parameters.putString("fields", "first_name,last_name,email,id")
        request.parameters = parameters
        request.executeAsync()
    }

    private fun setUpFacebook() {

        LoginManager.getInstance().registerCallback(callbackManager,
            object : FacebookCallback<LoginResult?> {
                override fun onSuccess(loginResult: LoginResult?) {

                    Log.e(TAG, "onSuccess: ${loginResult?.accessToken?.token}")
                    Log.e(TAG, "onSuccess: ${loginResult?.recentlyGrantedPermissions}")
                    // App code
                }

                override fun onCancel() {
                    // App code
                }

                override fun onError(exception: FacebookException) {
                    Log.e(TAG, "onError: ${exception.message} ")
                    // App code
                }
            })


    }


    private fun initViewModel() {

        viewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiHelperImpl(RetrofitBuilder.apiService), application)
        ).get(LoginViewModel::class.java)
    }


    private fun setupObserver() {
        val dialog = getDialogInstance()

        viewModel.getLogin().observe(this,
            Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        dialog.dismiss()

                        it.data?.let { users ->
                            if ( rb.isChecked ){
                                MyPreferences.setInt("isLogin",1)
                            }
                            MyPreferences.setStr("userToken",users.user.access_token)
                            startActivity(Intent(applicationContext, MainActivity::class.java))
                            finish()

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

    private fun handleLogin(email: String, password: String) {
        if (email.isEmpty()) {
            etEmail.error = getString(R.string.empty)
            return
        }
        if (password.isEmpty()) {
            etPassword.error = getString(R.string.empty)
            return
        }



    }

}