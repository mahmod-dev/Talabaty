package com.android.talabaty.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.android.talabaty.CartActivity
import com.android.talabaty.R
import com.android.talabaty.adapter.MainPagerAdapter
import com.android.talabaty.dbUtil.Status
import com.android.talabaty.dbUtil.ViewModelFactory
import com.android.talabaty.retrofit.ApiHelperImpl
import com.android.talabaty.retrofit.RetrofitBuilder
import com.android.talabaty.util.Helper
import com.android.talabaty.viewModel.StoresViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.TabLayoutOnPageChangeListener
import kotlinx.android.synthetic.main.toolbar_location_cart.*

class MainFragment : Fragment() {
    val TAG = "MainFragment"
    private lateinit var viewModel: StoresViewModel

    var viewpager: ViewPager? = null
    var tabs: TabLayout? = null
    var catId = 1
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_main, container, false)
        initViewModel()
        viewModel.stores()
        viewpager = root.findViewById<View>(R.id.viewPagerMain) as ViewPager
        tabs = root.findViewById<View>(R.id.tabs) as TabLayout


        viewpager?.addOnPageChangeListener(TabLayoutOnPageChangeListener(tabs))

        tabs?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab?.position!!>0){
                    viewModel.storesById( tab?.position!!)

                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
        tabs?.setupWithViewPager(viewpager)


        setupObserver()

        return root
    }

    override fun onResume() {
        super.onResume()
//          viewpager?.currentItem = 0
    }


    private fun initViewModel() {

        viewModel = ViewModelProviders.of(
            activity!! ,
            ViewModelFactory(ApiHelperImpl(RetrofitBuilder.apiService), activity!!.application)
        ).get(StoresViewModel::class.java)
    }


    private fun setupObserver() {

        viewModel.getAllStores().observe(
            viewLifecycleOwner,
            Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        it.data?.let { users ->
                            for (i in users.activities.indices) {
                                Log.e(TAG, "setupObserver: ${users.activities[i].name}")
                            }
                            viewpager?.adapter = MainPagerAdapter(
                                activity,
                                users.activities.size,
                                users.activities,
                                activity?.supportFragmentManager
                            )
                        }
                    }
                    Status.LOADING -> {

                    }
                    Status.ERROR -> {
                        //Handle Error
                        Helper.showFilterDialog(context!!, it.message!!).show()
                        Log.e(TAG, "setupObserver: " + it.message)
                    }
                }

            }
        )
    }


}