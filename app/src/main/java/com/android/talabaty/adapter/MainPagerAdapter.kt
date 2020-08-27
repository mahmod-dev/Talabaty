package com.android.talabaty.adapter

import android.app.Activity
import android.util.Log
import androidx.fragment.app.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.android.talabaty.R
import com.android.talabaty.dbUtil.ViewModelFactory
import com.android.talabaty.fragment.mainTab.*
import com.android.talabaty.retrofit.ApiHelperImpl
import com.android.talabaty.retrofit.RetrofitBuilder
import com.android.talabaty.viewModel.StoresViewModel
import com.android.talabaty.dbUtil.Status


class MainPagerAdapter(var context: Activity, fm: FragmentManager?) :
    FragmentStatePagerAdapter (fm!!) {
    private lateinit var viewModel: StoresViewModel
    var counts = 0
    var names: ArrayList<String>? = null
    val TAG = "MainPagerAdapter"

    init {
        names = ArrayList()
        initViewModel()
        setupObserver()
    }

    // This determines the fragment for each tab
    override fun getItem(position: Int): Fragment {

//        return when (position) {
//            0 -> {
//                AllFragment()
//            }
//            1 -> {
//                RestaurantFragment()
//            }
//            2 -> {
//                StoreFragment()
//            }
//            3 -> {
//                WearFragment()
//            }
//
//            4 -> {
//                GiftFragment()
//            }
//
//            5 -> {
//                ElectronicDevicesFragment()
//            }
//
//            else -> {
//                AllFragment()
//            }
//        }

        return AllFragment()
    }

    // This determines the number of tabs
    override fun getCount(): Int {
        return 8
    }

    // This determines the title for each tab
    override fun getPageTitle(position: Int): CharSequence? {
        // Generate title based on item position



        return when (position) {
            0 -> context.getString(R.string.all)
            1 -> context.getString(R.string.restaurants)
            2 -> context.getString(R.string.stores)
            3 -> context.getString(R.string.wears)
            4 -> context.getString(R.string.gifts)
            5 -> context.getString(R.string.electrical_devices)
            else -> null
        }
    }

    private fun initViewModel() {

        viewModel = ViewModelProviders.of(
            context as FragmentActivity,
            ViewModelFactory(ApiHelperImpl(RetrofitBuilder.apiService), context.application)
        ).get(StoresViewModel::class.java)
    }


    private fun setupObserver() {

        Log.e(TAG, "setupObserver: 1111")
        viewModel.getAllStores().observe(context as FragmentActivity,

            Observer {
                when (it.status) {
                    Status.SUCCESS -> {

                        it.data?.let { users ->
                            for (i in users.activities.indices) {
                                Log.e(TAG, "setupObserver: ${users.activities[i]}")
                                names?.add(users.activities[i].name)
                                counts++
                            }
                        }
                    }
                    Status.LOADING -> {

                    }
                    Status.ERROR -> {
                        //Handle Error

                        Log.e("MainPagerAdapter", "setupObserver: " + it.message)

                    }
                }

            }
        )
    }


}