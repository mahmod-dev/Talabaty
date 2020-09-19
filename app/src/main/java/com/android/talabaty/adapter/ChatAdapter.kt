package com.android.talabaty.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.android.talabaty.R
import com.android.talabaty.model.*
import com.android.talabaty.util.Helper.setUrlImage
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.item_chat.view.*

class ChatAdapter(var activity: Activity, var data: ArrayList<Product>) :
    RecyclerView.Adapter<ChatAdapter.ViewHolder>() {
    val TAG = "ChatAdapter"


    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_chat, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.bind(data[i],i)
    }

    override fun getItemCount(): Int {
        return data.size
    }


    inner class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var tvChatName: TextView = itemView.tvChatName
        var tvChatDate: TextView = itemView.tvChatDate
        var tvLastMsg: TextView = itemView.tvLastMsg
        var imgIsOnline: ImageView = itemView.imgIsOnline
        var imgChat: ImageView = itemView.imgChat

        fun bind(product: Product,position: Int) {


            if (product.image.isNotEmpty()) {
                imgChat.setUrlImage(activity, product.image)

            }


        }
    }


}