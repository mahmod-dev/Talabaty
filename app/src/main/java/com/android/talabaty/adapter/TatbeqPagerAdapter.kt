package com.android.talabaty.adapter

import android.app.Activity
import android.content.Context
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
import com.android.talabaty.fragment.tatbeqaqum.TatbeqFragment
import com.android.talabaty.model.MyActivity


class TatbeqPagerAdapter(
    var activity: Context?,
    var size: Int,
    var names: ArrayList<MyActivity>,
    fm: FragmentManager?
) :
    FragmentStatePagerAdapter(fm!!) {


    override fun getItem(position: Int): Fragment {

        return TatbeqFragment(position)



    }

    // This determines the number of tabs
    override fun getCount(): Int {
        return size+1
    }

    // This determines the title for each tab
    override fun getPageTitle(position: Int): CharSequence? {
        // Generate title based on item position
        val arr = ArrayList<String>()
        activity?.getString(R.string.all)?.let { arr.add(it) }
        for (i in names.indices) {
            arr.add(names[i].name)
        }
        return arr[position]


    }


}