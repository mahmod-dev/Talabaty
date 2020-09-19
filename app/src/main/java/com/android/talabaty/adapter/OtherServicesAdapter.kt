package com.android.talabaty.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.android.talabaty.OtherServiceActivity
import com.android.talabaty.R
import com.android.talabaty.model.Digital
import com.android.talabaty.model.Offer
import com.android.talabaty.util.Helper.setUrlImage
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.item_all_categories.view.*
import kotlinx.android.synthetic.main.item_newest_offers.view.*


class OtherServicesAdapter(
    var activity: Activity, var data: ArrayList<Digital>

) :
    RecyclerView.Adapter<OtherServicesAdapter.ViewHolder>() {
    var mListener: OnItemClickListener? = null
    val TAG = "OtherServicesAdapter"

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
            .inflate(R.layout.item_all_categories, viewGroup, false)
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
        var cardCat: CardView = itemView.cardCat
        var tvTitle: TextView = itemView.tvTitle

        var img: ImageView = itemView.img

        fun bind(digital: Digital) {
            tvTitle.text = digital.name


            if (digital.image.isNotEmpty()) {
                img.setUrlImage(activity, digital.image)
            }

            cardCat.setOnClickListener {
                val intent = Intent(activity, OtherServiceActivity::class.java)
                intent.putExtra("other_service_id", digital.id)
                activity.startActivity(intent)
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