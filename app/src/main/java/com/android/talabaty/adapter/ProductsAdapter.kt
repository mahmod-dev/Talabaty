package com.android.talabaty.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.android.talabaty.R
import com.android.talabaty.model.Product
import com.android.talabaty.util.MyPreferences
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.item_productive_all.view.*


class ProductsAdapter(
    var activity: Activity,
    var data: ArrayList<Product>
) :
    RecyclerView.Adapter<ProductsAdapter.MyViewHolder>() {
    val TAG = "ProductsAdapter"
    var onItemClick: ((Product) -> Unit)? = null

    init {
        MyPreferences.context = activity
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_productive_all, viewGroup, false)
        return MyViewHolder(view)

    }

    override fun onBindViewHolder(myViewHolder: MyViewHolder, i: Int) {

            myViewHolder.bind(data[i])


    }

    override fun getItemCount(): Int {

        return data.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvName: TextView = itemView.tvTitle
        var tvPrice: TextView = itemView.tvPrice
        var img: ImageView = itemView.img
        var card: CardView = itemView.card


        fun bind(product: Product) {
            tvName.text = product.name
            tvPrice.text = "${product.price} ${activity.getString(R.string.rs)} "


            if (product.image.isNotEmpty()) {
                Glide.with(itemView.context).load(product.image)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.ic_icon_loading)
                    .error(R.drawable.white)
                    .into(img)
            }


            card.setOnClickListener {
                onItemClick?.invoke(data?.get(adapterPosition))
            }


        }

    }


}