package com.android.talabaty.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.android.talabaty.R
import com.android.talabaty.adapter.SliderAdapter
import com.android.talabaty.adapter.ViewPagerAdapter
import com.google.android.material.tabs.TabLayout

class FamilyFragment : Fragment() {
    var adapter: SliderAdapter? = null
    var linear1: LinearLayout? = null
    var linear2: LinearLayout? = null
    var linear3: LinearLayout? = null
    var tabLayout: TabLayout? = null
    var viewPager: ViewPager? = null
    var viewPagerAdapter: ViewPagerAdapter? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        viewPager = root.findViewById<View>(R.id.viewPager) as ViewPager
        viewPagerAdapter = ViewPagerAdapter(context!!, fragmentManager)
        viewPager!!.adapter = viewPagerAdapter
        tabLayout = root.findViewById<View>(R.id.tabs) as TabLayout
        tabLayout!!.setupWithViewPager(viewPager)
        return root
    }
}