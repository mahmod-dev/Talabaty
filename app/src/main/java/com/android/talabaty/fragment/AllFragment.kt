package com.android.talabaty.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.android.talabaty.*
import com.android.talabaty.adapter.SliderAdapter
import com.fuzz.indicator.CutoutViewIndicator

class AllFragment : Fragment() {
    var adapter: SliderAdapter? = null
    var linear1: LinearLayout? = null
    var linear2: LinearLayout? = null
    var linear3: LinearLayout? = null
    var linear4: LinearLayout? = null
    var cardView: CardView? = null
    var viewPager: ViewPager? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_all, container, false)
        viewPager = root.findViewById(R.id.pager)
        adapter = SliderAdapter(activity!!, 3)
        viewPager?.setAdapter(adapter)
        val indicator: CutoutViewIndicator = root.findViewById(R.id.indicator_details)
        indicator.setViewPager(viewPager)
        linear1 = root.findViewById(R.id.free_delivery)
        linear2 = root.findViewById(R.id.order_car)
        linear3 = root.findViewById(R.id.order_service)
        linear4 = root.findViewById(R.id.services)
        cardView = root.findViewById(R.id.first_card)
        linear1.setOnClickListener(View.OnClickListener { startActivity(Intent(activity, FreeDeliveryActivity::class.java)) })
        linear2.setOnClickListener(View.OnClickListener { startActivity(Intent(activity, DeliveryServicesActivity::class.java)) })
        linear3.setOnClickListener(View.OnClickListener { startActivity(Intent(activity, DeliveryActivity::class.java)) })
        linear4.setOnClickListener(View.OnClickListener { startActivity(Intent(activity, ElectronicServiceActivity::class.java)) })
        cardView.setOnClickListener(View.OnClickListener { startActivity(Intent(activity, HomeActivity::class.java)) })
        return root
    }
}