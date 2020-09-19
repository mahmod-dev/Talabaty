package com.android.talabaty.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.android.talabaty.R
import com.android.talabaty.model.Ad
import com.android.talabaty.model.OtherImage
import com.android.talabaty.util.Helper.setUrlImage
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class SliderProductAdapter(var activity: Activity, var data: ArrayList<OtherImage>) : PagerAdapter() {
    init {

        notifyDataSetChanged()

    }




    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view: View = LayoutInflater.from(container.context)
            .inflate(R.layout.item_home_page, container, false)
        container.addView(view)
        val img: ImageView
        img = view.findViewById(R.id.imgArrow)

        if (data[position].image.isNotEmpty()) {
            img.setUrlImage(activity, data[position].image)
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
        return data.size
    }

}
