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
import com.android.talabaty.ElectronicServiceActivity
import com.android.talabaty.R
import com.android.talabaty.model.Digital
import com.android.talabaty.model.FreeDelivery
import com.android.talabaty.model.getDigitals
import com.android.talabaty.util.MyPreferences
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.item_remote_service.view.*

class RemoteServiceAdapter(
    var activity: Activity,
    var data: List<Digital>
) :
    RecyclerView.Adapter<RemoteServiceAdapter.ViewHolder>() {
    val TAG = "RemoteServiceAdapter"
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
            .inflate(R.layout.item_remote_service, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.bind(data[i])

    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvTitle: TextView = itemView.tvTitle
        var img: ImageView = itemView.img
        var cardPromotion: CardView = itemView.cardPromotion


        fun bind(digital: Digital) {

            tvTitle.text = digital.name

            if (digital.image.isNotEmpty()) {
                Glide.with(itemView.context).load(digital.image)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.ic_icon_loading)
                    .error(R.drawable.white)
                    .into(img)
            }


            cardPromotion.setOnClickListener {
                val intent = Intent(activity, ElectronicServiceActivity::class.java)
                intent.putExtra("type", digital.id)
                activity.startActivity (intent)
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