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
import com.android.talabaty.util.Helper.setUrlImage
import com.android.talabaty.util.MyPreferences
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.item_newest.view.*

class MainCategoryAdapter(var activity: Activity, var data:  HomePageCategories) :
    RecyclerView.Adapter<MainCategoryAdapter.ViewHolder>() {
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
            .inflate(R.layout.item_newest, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.bind(data.home_page_categories[i])

    }

    override fun getItemCount(): Int {
        return data.home_page_categories.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var linCat: LinearLayout = itemView.linCat
        var tvTitleCat: TextView = itemView.tvTitleFish
        var imgCat: ImageView = itemView.imgFish



        fun bind(categories: HomePageCategory) {


            tvTitleCat.text = categories.name

            if (categories.image.isNotEmpty()) {
                imgCat.setUrlImage(activity, categories.image)
            }

            linCat.setOnClickListener {
                val intent = Intent(activity,DeliveryServicesActivity::class.java)
                intent.putExtra("categoryId",categories.id)
                intent.putExtra("deliveryCost",data.settings.orders_delivery_cost)
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