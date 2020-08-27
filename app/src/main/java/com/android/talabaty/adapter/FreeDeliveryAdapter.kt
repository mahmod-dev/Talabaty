package com.android.talabaty.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.android.talabaty.DeliveryServicesActivity
import com.android.talabaty.R
import com.android.talabaty.model.*
import com.android.talabaty.util.MyPreferences
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.item_free_delivery.view.*
import kotlinx.android.synthetic.main.item_newest.view.*

class FreeDeliveryAdapter(var activity: Activity, var data: List<FreeDelivery>) :
    RecyclerView.Adapter<FreeDeliveryAdapter.ViewHolder>() {
    val TAG = "MainCategoryAdapter"
    var mListener: OnItemClickListener? = null

    init {
        MyPreferences.context = activity
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
        fun onItemLongClick(position: Int)
    }

    fun setOnClickListener(listener: OnItemClickListener?) {
        mListener = listener
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_free_delivery, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.bind(data[i])

    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var linCat: LinearLayout = itemView.linCat
        var tvTitleCat: TextView = itemView.tvTitleCat
        var tvDiscountFree: TextView = itemView.tvDiscountFree
        var imgFreeDelivery: ImageView = itemView.imgFreeDelivery


        fun bind(freeDelivery: FreeDelivery) {


            tvTitleCat.text = freeDelivery.name
            tvDiscountFree.text =
                "${freeDelivery.discount_percent}% ${itemView.context.resources.getString(R.string.discount)} "

            if (freeDelivery.image_profile.isNotEmpty()) {
                Glide.with(itemView.context).load(freeDelivery.image_profile)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.ic_icon_loading)
                    .error(R.drawable.white)
                    .into(imgFreeDelivery)
            }

            linCat.setOnClickListener {
                val intent = Intent(activity, DeliveryServicesActivity::class.java)
                activity.startActivity(intent)
            }

        }

        init {
            itemView.setOnClickListener {
                if (mListener != null) {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        mListener!!.onItemClick(position)
                    }
                }
            }

            itemView.setOnLongClickListener {
                if (mListener != null) {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        mListener!!.onItemLongClick(position)
                    }
                }
                false

            }
        }
    }


}