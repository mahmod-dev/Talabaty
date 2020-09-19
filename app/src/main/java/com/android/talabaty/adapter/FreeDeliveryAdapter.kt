package com.android.talabaty.adapter

import android.app.Activity
import android.location.Location
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.android.talabaty.R
import com.android.talabaty.model.FreeDelivery
import com.android.talabaty.util.Helper.setUrlImage
import com.android.talabaty.util.MyPreferences
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.item_free_delivery.view.*

class FreeDeliveryAdapter(
    var activity: Activity,
    var data: List<FreeDelivery>,
    var lat: Double,
    var lng: Double
) :
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
        var tvDeliveryTitle: TextView = itemView.tvDeliveryTitle
        var tvDiscountFree: TextView = itemView.tvDiscountFree
        var tvDistanceFree: TextView = itemView.tvDistanceFree
        var imgFreeDelivery: ImageView = itemView.imgFreeDelivery


        fun bind(freeDelivery: FreeDelivery) {


            tvDeliveryTitle.text = freeDelivery.name
            tvDiscountFree.text =
                "${freeDelivery.discount_percent}% ${itemView.context.resources.getString(R.string.discount)} "

            if (freeDelivery.image_profile.isNotEmpty()) {
                imgFreeDelivery.setUrlImage(activity, freeDelivery.image_profile)

            }

//            linCat.setOnClickListener {
//                val intent = Intent(activity, DeliveryServicesActivity::class.java)
//                activity.startActivity(intent)
//            }

            if (lat == 0.0 || lng == 0.0) {
                tvDistanceFree.text = activity.getString(R.string.unknown)
            } else
                tvDistanceFree.text =
                    "${calculateDistance(freeDelivery.latitude, freeDelivery.longitude)} KM"

        }

        private fun calculateDistance(distLat: Double, distLong: Double): String {
            val selected_location = Location("locationA")
            selected_location.latitude = lat
            selected_location.longitude = lng
            val near_locations = Location("locationB")
            near_locations.latitude = distLat
            near_locations.longitude = distLong

            val distance =(selected_location.distanceTo(near_locations)) / 1000

            return String.format("%.2f", distance)
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