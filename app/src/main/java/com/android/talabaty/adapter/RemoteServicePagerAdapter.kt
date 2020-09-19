package com.android.talabaty.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.android.talabaty.R
import com.android.talabaty.util.Helper.setUrlImage
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.item_our_work.view.*


class RemoteServicePagerAdapter(var activity: Activity, var data: ArrayList<Int>) :
    RecyclerView.Adapter<RemoteServicePagerAdapter.ViewHolder>() {
    private val TAG = "RemoteServicePagerAdapter"

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        i: Int
    ): ViewHolder {
        val view =
            LayoutInflater.from(viewGroup.context).inflate(R.layout.item_our_work, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(
        viewHolder: ViewHolder,
        i: Int
    ) {
        Glide.with(activity).load(data[i])
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(R.drawable.ic_icon_loading)
            .error(R.drawable.white)
            .into(viewHolder.imgArrow)

       // viewHolder.imgArrow.setUrlImage(activity,data[i])


    }

    override fun getItemCount(): Int = data.size


    inner class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var imgArrow: ImageView = itemView.imgArrow


    }




}