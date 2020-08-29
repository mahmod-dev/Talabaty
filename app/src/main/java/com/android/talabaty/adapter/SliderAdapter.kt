package com.android.talabaty.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.android.talabaty.R

class SliderAdapter(var activity: Activity, var counts: Int) : PagerAdapter() {


    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view: View = LayoutInflater.from(container.context)
            .inflate(R.layout.item_home_page, container, false)
        container.addView(view)
        val img: ImageView
        img = view.findViewById(R.id.imgArrowFish)
        if (count == 3) {
            img.setImageResource(R.drawable.img_aklak)
        } else {
            img.setImageResource(R.drawable.img_wp)
        }
        return view
    }

    override fun destroyItem(
        container: ViewGroup,
        position: Int,
        `object`: Any
    ) {
        container.removeView(`object` as View)
    }

    override fun getCount(): Int {
        return counts
    }

}
