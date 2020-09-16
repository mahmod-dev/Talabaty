//package com.android.talabaty.adapter
//
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.recyclerview.widget.RecyclerView
//import com.android.talabaty.R
//import com.android.talabaty.model.Cart
//
//class MessageAdapter(var data: List<Chat>) :
//    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
//
//    fun getEvents(): List<Chat> {
//        return data
//    }
//
//    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): RecyclerView.ViewHolder {
//        val view: View?
//
//        if (i == 0) {
//            view = LayoutInflater.from(viewGroup.context)
//                .inflate(R.layout.item_sender, viewGroup, false)
//            return ViewHolderSender(view)
//        } else {
//            view = LayoutInflater.from(viewGroup.context)
//                .inflate(R.layout.item_receiver, viewGroup, false)
//            return ViewHolderReceiver(view)
//        }
//
//    }
//
//    override fun onBindViewHolder(
//        viewHolder: RecyclerView.ViewHolder,
//        i: Int
//    ) {
//        if (viewHolder.itemViewType == 0) {
//            val senderView = viewHolder as ViewHolderSender
//            senderView.bindSender(data[i].event!!)
//        } else {
//            val receiverView = viewHolder as ViewHolderReceiver
//            receiverView.bindReceiver(data[i].task!!)
//        }
//
//
//    }
//
//    override fun getItemCount(): Int {
//        return data.size
//    }
//
//    override fun getItemViewType(position: Int): Int {
//        if (data[position].type == 0) {
//            return 0
//        } else
//            return 1
//    }
//
//
//    inner class ViewHolderSender(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        fun bindSender(sender: Cart) {
//        }
//
//    }
//
//    inner class ViewHolderReceiver(itemView: View) : RecyclerView.ViewHolder(itemView) {
//
//
//        fun bindReceiver(receiver:Cart) {
//        }
//
//    }
//
//
//}