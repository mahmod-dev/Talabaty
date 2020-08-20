package com.android.talabaty.fragment.mainTab

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
    var linFreeDelivery: LinearLayout? = null
    var linOrderService: LinearLayout? = null
    var linOrderCar: LinearLayout? = null
    var linRemoteServices: LinearLayout? = null
    var cardOfferServices: CardView? = null
    var viewPager: ViewPager? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_all, container, false)
        viewPager = root.findViewById(R.id.pager)
        linFreeDelivery = root.findViewById(R.id.linFreeDelivery)
        linOrderService = root.findViewById(R.id.linOrderService)
        linOrderCar = root.findViewById(R.id.linOrderCar)
        linRemoteServices = root.findViewById(R.id.linRemoteServices)
        cardOfferServices = root.findViewById(R.id.cardOfferServices)
        viewPager = root.findViewById(R.id.pager)
        adapter = SliderAdapter(activity!!, 3)
        viewPager?.setAdapter(adapter)
        val indicator: CutoutViewIndicator = root.findViewById(R.id.indicator_details)
        indicator.setViewPager(viewPager)

        linFreeDelivery?.setOnClickListener { startActivity(Intent(activity, FreeDeliveryActivity::class.java)) }
        linOrderService?.setOnClickListener { startActivity(Intent(activity, DeliveryServicesActivity::class.java)) }
        linOrderCar?.setOnClickListener { startActivity(Intent(activity, CarServiceActivity::class.java)) }
        linRemoteServices?.setOnClickListener { startActivity(Intent(activity, RemoteServiceActivity::class.java)) }
        cardOfferServices?.setOnClickListener { startActivity(Intent(activity, NewestOffersServicesActivity::class.java)) }
        return root
    }
}