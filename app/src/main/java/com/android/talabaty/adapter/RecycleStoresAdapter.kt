package com.android.talabaty.adapter

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.android.talabaty.R
import com.android.talabaty.StoreDetailsActivity
import com.android.talabaty.model.Offer
import com.android.talabaty.model.Store
import com.android.talabaty.model.ViewStores
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.item_restaurant_under.view.*


class RecycleStoresAdapter(var activity: Activity, var data: ArrayList<Store>) :
    RecyclerView.Adapter<RecycleStoresAdapter.ViewHolder>() {
    var mListener: OnItemClickListener? = null
    val TAG = "RecycleStoresAdapter"

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
            .inflate(R.layout.item_restaurant_under, viewGroup, false)
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
        var tvDiscountStore: TextView = itemView.tvDiscountStore
        var tvIsOpenStore: TextView = itemView.tvIsOpenStore
        var tvTitleStore: TextView = itemView.tvTitleStore
        var tvTimeStore: TextView = itemView.tvTimeStore
        var tvDistanceStore: TextView = itemView.tvDistanceStore
        var rateBarStore: RatingBar = itemView.rateBarStore
        var imgStore: ImageView = itemView.imgStore
        var card: CardView = itemView.card

        fun bind(stores: Store) {
            tvDiscountStore.visibility = View.GONE
            tvTitleStore.text = stores.name
            rateBarStore.numStars = stores.rate
            tvTimeStore.text = "${stores.time_from} - ${stores.time_to}"

            if (stores.open == 1) {
                tvIsOpenStore.text = itemView.context.resources.getString(R.string.opened)
                tvIsOpenStore.backgroundTintList =
                    (AppCompatResources.getColorStateList(itemView.context, R.color.green_light))
                tvIsOpenStore.setTextColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.colorPrimary
                    )
                )
            } else {
                tvIsOpenStore.text = itemView.context.resources.getString(R.string.closed)
                tvIsOpenStore.backgroundTintList =
                    (AppCompatResources.getColorStateList(itemView.context, R.color.red_light))
                tvIsOpenStore.setTextColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.colorRed2
                    )
                )
            }

            if (stores.discount_percent != 0) {
                tvDiscountStore.visibility = View.VISIBLE
                tvDiscountStore.text =
                    "${stores.discount_percent}% ${itemView.context.resources.getString(R.string.discount)} "

            }

            if (stores.image_profile.isNotEmpty()) {
                Glide.with(itemView.context).load(stores.image_profile)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.ic_icon_loading)
                    .error(R.drawable.white)
                    .into(imgStore);
            }

            card.setOnClickListener {
                Log.e(TAG, "bind: ")
                val intent = Intent(activity, StoreDetailsActivity::class.java)
                intent.putExtra("storeId", stores.id)
                intent.putExtra("name", stores.name)
                intent.putExtra("rate", stores.rate)
                intent.putExtra("time_from", stores.time_from)
                intent.putExtra("time_to", stores.time_to)
                intent.putExtra("discount_percent", stores.discount_percent)
                intent.putExtra("open", stores.open)
                intent.putExtra("image_profile", stores.image_profile)
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