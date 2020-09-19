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
import com.android.talabaty.model.PaymentMethod
import com.android.talabaty.model.Size
import com.android.talabaty.util.Helper.setUrlImage
import com.android.talabaty.util.MyPreferences
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.item_colors_families.view.*
import kotlinx.android.synthetic.main.item_colors_families.view.tvName
import kotlinx.android.synthetic.main.item_payment_method.view.*

class PaymentMethodAdapter(
    var activity: Activity,
    var data: ArrayList<PaymentMethod>
) :

    RecyclerView.Adapter<PaymentMethodAdapter.MyViewHolder>() {
    val TAG = "PaymentMethodAdapter"
    var onItemClick: ((Int) -> Unit)? = null

    init {
        MyPreferences.context = activity
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_payment_method, viewGroup, false)
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
        var rlPayment: RelativeLayout = itemView.rlPayment
        var imgPayment: ImageView = itemView.imgPayment


        fun bind(paymentMethod: PaymentMethod) {
            tvName.text = paymentMethod.name



            if (paymentMethod.image.isNotEmpty()) {
                imgPayment.setUrlImage(activity, paymentMethod.image)
            }


            rlPayment.setOnClickListener {
                onItemClick?.invoke(data[adapterPosition].id)
            }

        }

    }


}