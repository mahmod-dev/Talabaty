package com.android.talabaty.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.talabaty.R
import com.android.talabaty.model.Offer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.item_newest_offers.view.*


class NewestOffersAdapter(
    var activity: Activity, var data: ArrayList<Offer>

) :
    RecyclerView.Adapter<NewestOffersAdapter.ViewHolder>() {
    var mListener: OnItemClickListener? = null
    val TAG = "NewestOffersAdapter"

    interface OnItemClickListener {
        fun onItemClick(position: Int, storeId: Int)
        fun onItemLongClick(position: Int)
    }

    fun setOnClickListener(listener: OnItemClickListener) {
        mListener = listener
    }


    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        i: Int
    ): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_newest_offers, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(
        viewHolder: ViewHolder,
        i: Int
    ) {
        viewHolder.bind(data[i])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var tvNewestOffer: TextView = itemView.tvNewestOffer
        var tvNewestTitle: TextView = itemView.tvNewestTitle
        var tvNewestDelivery: TextView = itemView.tvNewestDelivery
        var tvNewestPrice: TextView = itemView.tvNewestPrice
        var imgNewestOffer: ImageView = itemView.imgNewestOffer

        fun bind(offer: Offer) {
            tvNewestOffer.text = offer.name
            tvNewestTitle.text = offer.description
            tvNewestPrice.text = "${offer.offer_price}$"

            if (offer.image.isNotEmpty()) {
                Glide.with(itemView.context).load(offer.image)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.ic_icon_loading)
                    .error(R.drawable.white)
                    .into(imgNewestOffer)
            }

            if (offer.delivery_cost!=0){
                tvNewestDelivery.visibility = View.GONE
            }
        }

        init {
            itemView.setOnClickListener {
                if (mListener != null) {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        mListener!!.onItemClick(position, data[position].id)
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