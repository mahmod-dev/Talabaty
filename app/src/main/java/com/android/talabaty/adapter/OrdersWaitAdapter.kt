package com.android.talabaty.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.android.talabaty.R
import com.android.talabaty.model.ClientOrder
import com.android.talabaty.model.OrderProduct
import com.android.talabaty.util.MyPreferences
import kotlinx.android.synthetic.main.item_order_wait.view.*

class OrdersWaitAdapter(
    var activity: Activity,
    var data: ArrayList<OrderProduct>
) :

    RecyclerView.Adapter<OrdersWaitAdapter.MyViewHolder>() {
    val TAG = "OrdersWaitAdapter"
    var onItemClick: ((ClientOrder, Int) -> Unit)? = null

    init {
        MyPreferences.context = activity
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_order_wait, viewGroup, false)
        return MyViewHolder(view)

    }

    override fun onBindViewHolder(myViewHolder: MyViewHolder, i: Int) {

        myViewHolder.bind(data[i])


    }

    override fun getItemCount(): Int {

        return data.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvMealName: TextView = itemView.tvName
        var tvPriceOrder: TextView = itemView.tvPrice
        var tvOrderNum: TextView = itemView.tvMealNum


        fun bind(order: OrderProduct) {
            tvMealName.text = order.product.name
            tvPriceOrder.text = "${order.price} ${activity.getString(R.string.rs)}"
           // tvOrderNum.text = "#${order.quantity}"


        }

    }

    private fun TextView.setDrawableStart(drawable: Int = 0, color: Int, textColor: Int) {
        this.backgroundTintList = ContextCompat.getColorStateList(activity,color)

        this.setTextColor(ContextCompat.getColor(activity, textColor))
        this.setCompoundDrawablesWithIntrinsicBounds(0, 0, drawable, 0)
    }


}