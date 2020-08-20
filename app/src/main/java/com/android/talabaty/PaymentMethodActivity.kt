package com.android.talabaty

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.android.talabaty.adapter.SliderAdapter
import com.fuzz.indicator.CutoutViewIndicator

class PaymentMethodActivity : AppCompatActivity() {
    var viewPager: ViewPager? = null
    var adapter: SliderAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_method)
        viewPager = findViewById(R.id.pager)
        adapter = SliderAdapter(this, 2)
        viewPager?.setAdapter(adapter)
        val indicator = findViewById<CutoutViewIndicator>(R.id.indicator_details)
        indicator.setViewPager(viewPager)
    }
}