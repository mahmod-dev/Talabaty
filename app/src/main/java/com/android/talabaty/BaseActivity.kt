package com.android.talabaty

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.android.talabaty.dbUtil.Status
import com.android.talabaty.dbUtil.ViewModelFactory
import com.android.talabaty.fragment.*
import com.android.talabaty.retrofit.ApiHelperImpl
import com.android.talabaty.retrofit.RetrofitBuilder
import com.android.talabaty.util.CustomMaterialDialog.getMaterialDialogInstance
import com.android.talabaty.util.LocationHelper
import com.android.talabaty.util.MyPreferences
import com.android.talabaty.viewModel.CartViewModel
import com.android.talabaty.viewModel.ProfileViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.toolbar_location_cart.*


open class BaseActivity : AppCompatActivity() {
    open val TAG = "BaseActivity"
    private lateinit var viewModel: ProfileViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
        // window.statusBarColor = ContextCompat.getColor(this, R.color.colorWhite)
        MyPreferences.context = applicationContext
        initViewModel()
        handleBottomNav()
        supportFragmentManager.beginTransaction().replace(
            R.id.fragmentContainer,
            MainFragment()
        ).commit()

        viewModel.userDetails()

        setupObserver()
    }

    private fun handleBottomNav() {

        val navigationItemSelectedListener: BottomNavigationView.OnNavigationItemSelectedListener =
            BottomNavigationView.OnNavigationItemSelectedListener { item ->
                var selectedFragment: Fragment? = null

                when (item.itemId) {

                    R.id.navMain -> {
                        selectedFragment = MainFragment()

                    }
                    R.id.navRequests -> {
                        selectedFragment = OrdersFragment()
                    }
                    R.id.navChat -> {
                        selectedFragment = ChatFragment()
                    }

                    R.id.navCategory -> {
                        selectedFragment = CategoriesFragment()
                    }

                    R.id.navProfile -> {
                        selectedFragment = ProfileFragment()
                    }
                }

                supportFragmentManager.beginTransaction().replace(
                    R.id.fragmentContainer,
                    selectedFragment!!
                ).commit()

                true
            }

        bottomNavMain.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

    }



    private fun initViewModel() {

        viewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiHelperImpl(RetrofitBuilder.apiService), application)
        ).get(ProfileViewModel::class.java)
    }

    private fun setupObserver() {

        viewModel.getUserDetails().observe(this,
            Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        it.data?.let { users ->
                            if (users.city.isNotEmpty()){
                                tvHomeLocation.text = users.city
                            }else
                            tvHomeLocation.text = getString(R.string.unknown)

                        }
                    }
                    Status.LOADING -> {

                    }
                    Status.ERROR -> {

                    }
                }

            }
        )
    }



}