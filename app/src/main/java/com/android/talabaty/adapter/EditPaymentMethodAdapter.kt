package com.android.talabaty.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.talabaty.R
import com.android.talabaty.model.PaymentCard
import com.android.talabaty.model.PaymentMethod
import com.android.talabaty.util.Helper.setUrlImage
import com.android.talabaty.util.MyPreferences
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.item_edit_payment_method.view.*


class EditPaymentMethodAdapter(
    var activity: Activity,
    var data: ArrayList<PaymentCard>
) :
    RecyclerView.Adapter<EditPaymentMethodAdapter.MyViewHolder>() {
    val TAG = "EditPaymentMethodAdapter"
    var onItemEditClick: ((Int, PaymentCard) -> Unit)? = null
    var onItemDeleteClick: ((Int,Int) -> Unit)? = null

    init {
        MyPreferences.context = activity
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_edit_payment_method, viewGroup, false)
        return MyViewHolder(view)

    }

    override fun onBindViewHolder(myViewHolder: MyViewHolder, i: Int) {

        myViewHolder.bind(data[i])

    }

    override fun getItemCount(): Int {

        return data.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvName: TextView = itemView.tvName
        var imgPayment: ImageView = itemView.imgPayment
        var imgEdit: ImageView = itemView.imgEdit
        var imgDelete: ImageView = itemView.imgDelete


        fun bind(paymentMethod: PaymentCard) {
            tvName.text = paymentMethod.method.name

            if (paymentMethod.method.image.isNotEmpty()) {
                imgPayment.setUrlImage(activity, paymentMethod.method.image)
            }


            imgDelete.setOnClickListener {
                onItemDeleteClick?.invoke(data[adapterPosition].id,adapterPosition)
            }

            imgEdit.setOnClickListener {
                onItemEditClick?.invoke(data[adapterPosition].id, data[adapterPosition])
            }

        }

    }


}