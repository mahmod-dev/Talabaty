package com.android.talabaty.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.talabaty.R
import com.android.talabaty.model.Coupon
import com.android.talabaty.util.MyPreferences
import kotlinx.android.synthetic.main.item_coupon.view.*

class CouponAdapter(
    var activity: Activity,
    var data: ArrayList<Coupon>
) :

    RecyclerView.Adapter<CouponAdapter.MyViewHolder>() {
    val TAG = "CouponAdapter"
    var onItemClick: ((Int, Int) -> Unit)? = null

    init {
        MyPreferences.context = activity
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_coupon, viewGroup, false)
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
        var tvValid: TextView = itemView.tvValid
        var tvDiscount: TextView = itemView.tvDiscount
        var imgDelete: ImageView = itemView.imgDelete


        fun bind(coupon: Coupon) {
            tvName.text = coupon.name
            tvDiscount.text = "${coupon.discount}%"
            tvValid.text = " ${activity.getString(R.string.valid_upto)}  ${coupon.end}"
            imgDelete.setOnClickListener {
                onItemClick?.invoke(adapterPosition, data[adapterPosition].id)
            }

        }

    }


}