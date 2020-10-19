package com.android.talabaty.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.android.talabaty.R
import com.android.talabaty.model.ClientOrder
import com.android.talabaty.util.Helper.setUrlImage
import com.android.talabaty.util.MyPreferences
import kotlinx.android.synthetic.main.item_order.view.*

class OrdersAdapter(
    var activity: Activity,
    var data: ArrayList<ClientOrder>
) :

    RecyclerView.Adapter<OrdersAdapter.MyViewHolder>() {
    val TAG = "CouponAdapter"
    var onItemClick: ((ClientOrder, Int) -> Unit)? = null

    init {
        MyPreferences.context = activity
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_order, viewGroup, false)
        return MyViewHolder(view)

    }

    override fun onBindViewHolder(myViewHolder: MyViewHolder, i: Int) {

        myViewHolder.bind(data[i])


    }

    override fun getItemCount(): Int {

        return data.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvMealName: TextView = itemView.tvMealName
        var tvPriceOrder: TextView = itemView.tvPriceOrder
        var tvDate: TextView = itemView.tvDate
        var tvStatus: TextView = itemView.tvStatus
        var tvOrderNum: TextView = itemView.tvOrderNum
        var tvMealNum: TextView = itemView.tvMealNum
        var tvPlaceAddress: TextView = itemView.tvPlaceAddress
        var img: ImageView = itemView.img
        var cardDeleteOrder: CardView = itemView.cardDeleteOrder


        fun bind(order: ClientOrder) {
            tvMealName.text = order.store?.name ?: ""
            tvPlaceAddress.text = order.store?.address ?: ""
            tvMealNum.text = order.meals_count.toString()
            tvDate.text = order.created_at.toString()

            tvPriceOrder.text = "${order.final_total} ${activity.getString(R.string.rs)}"
            tvOrderNum.text = "#${order.id}"
            if (!order.store?.image_profile.isNullOrEmpty()) {
                img.setUrlImage(activity, order.store?.image_profile)
            }

            cardDeleteOrder.setOnClickListener {
                onItemClick?.invoke(data[adapterPosition], adapterPosition)
            }

            when (order.status) {
                0 -> {
                    cardDeleteOrder.visibility = View.VISIBLE
                    tvStatus.text = activity.getString(R.string.send_ur_order)
                    tvStatus.setDrawableStart(
                        R.drawable.ic_send,
                        R.color.green_light,
                        R.color.colorPrimary
                    )
                }
                1 -> {
                    tvStatus.text = activity.getString(R.string.prepare_ur_order)
                    tvStatus.setDrawableStart(
                        R.drawable.ic_prepare,
                        R.color.green_light,
                        R.color.colorPrimary
                    )

                }
                2 -> {
                    tvStatus.text = activity.getString(R.string.deliver_ur_order)
                    tvStatus.setDrawableStart(
                        R.drawable.ic_delivery,
                        R.color.green_light,
                        R.color.colorPrimary
                    )

                }
                3 -> {
                    tvStatus.text = activity.getString(R.string.go_ur_order)
                    tvStatus.setDrawableStart(
                        R.drawable.ic_delivery,
                        R.color.green_light,
                        R.color.colorPrimary
                    )
                }
                4 -> {
                    tvStatus.text = activity.getString(R.string.deliverd_ur_order)
                    tvStatus.setDrawableStart(
                        R.drawable.ic_true,
                        R.color.green_light,
                        R.color.colorPrimary
                    )

                }
                5 -> {
                    tvStatus.text = activity.getString(R.string.receive_ur_order)
                    tvStatus.setDrawableStart(
                        R.drawable.ic_true,
                        R.color.green_light,
                        R.color.colorPrimary
                    )

                }

                6 -> {
                    tvStatus.text = activity.getString(R.string.cancel_ur_order)
                    tvStatus.setDrawableStart(
                        R.drawable.ic_cancel,
                        R.color.red_light,
                        R.color.colorRed3
                    )

                }
                7 -> {
                    tvStatus.text = activity.getString(R.string.refuse_ur_order)
                    tvStatus.setDrawableStart(
                        R.drawable.ic_cancel,
                        R.color.red_light,
                        R.color.colorRed3
                    )
                }

            }


        }

    }

    private fun TextView.setDrawableStart(drawable: Int = 0, color: Int, textColor: Int) {
        this.backgroundTintList = ContextCompat.getColorStateList(activity, color)

        this.setTextColor(ContextCompat.getColor(activity, textColor))
        this.setCompoundDrawablesWithIntrinsicBounds(0, 0, drawable, 0)
    }


}