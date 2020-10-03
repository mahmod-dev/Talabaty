package com.android.talabaty.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.android.talabaty.R
import com.android.talabaty.model.Product
import com.android.talabaty.util.Helper.setUrlImage
import com.android.talabaty.util.MyPreferences
import kotlinx.android.synthetic.main.item_like.view.*
import java.util.*
import kotlin.collections.ArrayList

class LikeProductsAdapter(
    var activity: Activity,
    var data: ArrayList<Product>
) :
    RecyclerView.Adapter<LikeProductsAdapter.MyViewHolder>() {
    val TAG = "LikeProductsAdapter"
    var onItemClick: ((Product) -> Unit)? = null

    init {
        MyPreferences.context = activity
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_like, viewGroup, false)
        return MyViewHolder(view)

    }

    override fun onBindViewHolder(myViewHolder: MyViewHolder, i: Int) {

        myViewHolder.bind(data[i])


    }

    override fun getItemCount(): Int {

        return data.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val mRandom = Random()

        var tvName: TextView = itemView.tvDescription
        var tvPrice: TextView = itemView.tvPrice
        var img: ImageView = itemView.img
        var lin: LinearLayout = itemView.lin


        fun bind(product: Product) {
            tvName.text = product.name
            tvPrice.text = "${productPrice(product)} $"


            if (product.image.isNotEmpty()) {
                img.layoutParams.height = getRandomIntInRange(300, 200)

                img.setUrlImage(activity, product.image)
            }


            lin.setOnClickListener {
                onItemClick?.invoke(data[adapterPosition])
            }


        }
        private fun getRandomIntInRange(max: Int, min: Int): Int {
            return mRandom.nextInt(max - min + min) + min
        }

    }



    private fun productPrice(product: Product): Int {

        return product.offer_price?.toInt() ?: product.price
    }

}