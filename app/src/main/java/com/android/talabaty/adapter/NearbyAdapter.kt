package com.android.talabaty.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.talabaty.R
import com.android.talabaty.model.Store
import com.android.talabaty.util.MyPreferences
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.item_home_near.view.*


class NearbyAdapter(
    var activity: Activity,
    var data: ArrayList<Store>? = null,
    var store: Store? = null
) :
    RecyclerView.Adapter<NearbyAdapter.MyViewHolder>() {
    val TAG = "NearbyAdapter"
    var onItemClick: ((Int) -> Unit)? = null

    init {
        MyPreferences.context = activity
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_home_near, viewGroup, false)
        return MyViewHolder(view)

    }

    override fun onBindViewHolder(myViewHolder: MyViewHolder, i: Int) {

        if (data != null)
            myViewHolder.bind(data?.get(i)!!)
        else
            myViewHolder.bind(store!!)

    }

    override fun getItemCount(): Int {

        return if (data == null) {
            1
        } else
            data!!.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvName: TextView = itemView.tvName
        var tvAddress: TextView = itemView.tvAddress
        var tvDistance: TextView = itemView.tvDistance
        var img: ImageView = itemView.img
        var lin: LinearLayout = itemView.lin


        fun bind(store: Store) {
            tvName.text = store.name
            tvAddress.text = store.address

            val strDist = String.format("%.2f", (store.distance / 1000))
            tvDistance.text = "$strDist KM"

            if (store.image_profile.isNotEmpty()) {
                Glide.with(itemView.context).load(store.image_profile)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.ic_icon_loading)
                    .error(R.drawable.white)
                    .into(img)
            }


            lin.setOnClickListener {
                if (data!=null)
                onItemClick?.invoke(data?.get(adapterPosition)!!.id)
            }


        }

    }


}