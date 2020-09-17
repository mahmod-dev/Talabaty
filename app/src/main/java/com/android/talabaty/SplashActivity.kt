package com.android.talabaty

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.android.talabaty.auth.SignInActivity
import com.android.talabaty.auth.SignUpActivity
import com.android.talabaty.dbUtil.Status
import com.android.talabaty.dbUtil.ViewModelFactory
import com.android.talabaty.retrofit.ApiHelperImpl
import com.android.talabaty.retrofit.RetrofitBuilder
import com.android.talabaty.util.CustomMaterialDialog
import com.android.talabaty.util.CustomMaterialDialog.getMaterialDialogInstance
import com.android.talabaty.util.Helper
import com.android.talabaty.util.MyPreferences
import com.android.talabaty.viewModel.OrdersViewModel
import com.facebook.login.Login
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {
    val TAG = "SplashActivity"
    private lateinit var viewModel: OrdersViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        MyPreferences.context = this

        initViewModel()
        viewModel.settings()
        setupObserverSettings()
//        Handler().postDelayed({
//
//        }, 3000)
    }

    private fun initViewModel() {

        viewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiHelperImpl(RetrofitBuilder.apiService), application)
        ).get(OrdersViewModel::class.java)
    }

    private fun setupObserverSettings() {

        viewModel.getAllSettings().observe(this,
            Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        progressBar.visibility = View.GONE
                        it.data?.let { users ->

                            MyPreferences.setLong("carCost", users.items.request_car_cost.toLong())
                            MyPreferences.setLong(
                                "other_service_cost",
                                users.items.other_service_cost.toLong()
                            )
                            MyPreferences.setLong(
                                "request_service_cost",
                                users.items.request_service_cost.toLong()
                            )
                            startActivity(Intent(this@SplashActivity, SignInActivity::class.java))
                            finish()
                        }
                    }
                    Status.LOADING -> {
                        progressBar.visibility = View.VISIBLE

                    }
                    Status.ERROR -> {
                        progressBar.visibility = View.GONE
                        //  this.getMaterialDialogInstance(it.message!!)
                        Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                        finish()

                        Log.e(TAG, "setupObserver: " + it.message)

                    }
                }

            }
        )
    }
}