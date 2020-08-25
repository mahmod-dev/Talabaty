package com.android.talabaty.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager2.widget.ViewPager2
import com.android.talabaty.R
import com.android.talabaty.adapter.ViewPagerStoresAdapter
import com.android.talabaty.dbUtil.ViewModelFactory
import com.android.talabaty.model.Activities
import com.android.talabaty.retrofit.ApiHelperImpl
import com.android.talabaty.retrofit.RetrofitBuilder
import com.android.talabaty.util.CustomAlertDialog.getDialogInstance
import com.android.talabaty.util.Helper
import com.android.talabaty.viewModel.StoresViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.mahmoud.todoapp.util.dbUtil.Status

class MainFragment : Fragment() {
    val TAG = "MainFragment"
    private lateinit var viewModel: StoresViewModel
    var viewpager: ViewPager2? = null
    var tabs: TabLayout? = null
    var catId = 1
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_main, container, false)
        initViewModel()
        viewpager = root.findViewById<View>(R.id.viewPagerMain) as ViewPager2
        tabs = root.findViewById<View>(R.id.tabs) as TabLayout

        viewpager?.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                Log.e(TAG, "onPageSelected: $position ")
                catId = position
                Log.e(TAG, "catId: $catId ")

                viewModel.storesById(catId)
            }
        })
        setupObserver()

        return root
    }

    override fun onResume() {
        super.onResume()
      //  viewpager?.currentItem = 0
    }


    private fun viewPager2Init(activities: Activities) {
          if (viewpager?.adapter ==null) {
              viewpager?.adapter = ViewPagerStoresAdapter(activity!!, activities.activities)

        val  names =   ArrayList<String>()
        names.add(0, getString(R.string.all))
        for (i in activities.activities.indices) {
            names.add(i+1,activities.activities[i].name)
        }
        names.add(activities.activities.size-1,activities.activities[activities.activities.size-1].name)


        TabLayoutMediator(tabs!!, viewpager!!,
            TabLayoutMediator.TabConfigurationStrategy { tab, position ->

                tab.text = names[position]

            }).attach()
          }
    }


    private fun initViewModel() {

        viewModel = ViewModelProviders.of(
            activity as FragmentActivity,
            ViewModelFactory(ApiHelperImpl(RetrofitBuilder.apiService), activity!!.application)
        ).get(StoresViewModel::class.java)
    }


    private fun setupObserver() {
        val dialog = activity?.getDialogInstance()

        viewModel.getAllStores().observe(activity as FragmentActivity,
            Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        dialog?.dismiss()
                        it.data?.let { users ->
                            for (i in users.activities.indices) {
                                Log.e(TAG, "setupObserver: ${users.activities[i].name}")
                            }
                            viewPager2Init(users)
                        }
                    }
                    Status.LOADING -> {
                        dialog?.show()

                    }
                    Status.ERROR -> {
                        //Handle Error
                        dialog?.dismiss()
                        Helper.showFilterDialog(context!!, it.message!!).show ()
                        Log.e(TAG, "setupObserver: " + it.message)
                    }
                }

            }
        )
    }


}