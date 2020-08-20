package com.android.talabaty.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.android.talabaty.R
import com.android.talabaty.fragment.mainTab.AllFragment
import com.android.talabaty.fragment.mainTab.ResturentFragment


class FamilyPagerAdapter(var context:Context,fm: FragmentManager?) : FragmentPagerAdapter(fm!!) {

    // This determines the fragment for each tab
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                AllFragment()
            }
            1 -> {
                ResturentFragment()
            }
            else -> {
                ResturentFragment()
            }
        }
    }

    // This determines the number of tabs
    override fun getCount(): Int {
        return 6
    }

    // This determines the title for each tab
    override fun getPageTitle(position: Int): CharSequence? {
        // Generate title based on item position
        return when (position) {
            0 ->  context.getString(R.string.product_describe)
            1 -> context.getString(R.string.reviews)

            else -> null
        }
    }

}