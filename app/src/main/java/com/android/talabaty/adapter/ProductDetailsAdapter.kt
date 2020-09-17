package com.android.talabaty.adapter

import android.app.Activity
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.android.talabaty.R
import com.android.talabaty.model.Color
import com.android.talabaty.model.Size
import com.android.talabaty.util.MyPreferences
import com.android.talabaty.viewModel.CartViewModel
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.item_colors_families.view.*

class ProductDetailsAdapter(
    var activity: Activity,
    var sizes: ArrayList<Size>? = null,
    var colors: ArrayList<Color>? = null,
    var type: Int
) :

    RecyclerView.Adapter<ProductDetailsAdapter.MyViewHolder>() {
    val TAG = "ProductDetailsAdapter"
    var onItemClick: ((Int) -> Unit)? = null
    var colorType: Int = 0
    var sizeType: Int = 0
    var pos: Int = 0

    init {
        MyPreferences.context = activity
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_colors_families, viewGroup, false)
        return MyViewHolder(view)

    }

    override fun onBindViewHolder(myViewHolder: MyViewHolder, i: Int) {
        if (type == 0) {
            pos  = i
            myViewHolder.bindSize(sizes?.get(i)!!,i)


        } else
            myViewHolder.bindColor(colors?.get(i)!!)


    }

    override fun getItemCount(): Int {
        if (sizes != null) {
            return sizes!!.size
        } else if (colors != null) {
            return colors!!.size
        } else
            return 0
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvName: TextView = itemView.tvName
        var lin: LinearLayout = itemView.lin


        fun bindSize(size: Size,position :Int) {
            pos = position
            tvName.text = size.name
            tvName.background = ContextCompat.getDrawable(activity, R.drawable.shape_border_color)
            tvName.setTextColor(ContextCompat.getColor(activity, R.color.colorBlack))

            tvName.setOnClickListener {

                if (sizeType == 0 ) {
                    tvName.background =
                        ContextCompat.getDrawable(activity, R.drawable.shape_waiting_box)
                    tvName.setTextColor(ContextCompat.getColor(activity, R.color.colorWhite))
                    sizeType = 1
                    onItemClick?.invoke(size.id)

                }
                else {
                    tvName.background =
                        ContextCompat.getDrawable(activity, R.drawable.shape_border_color)
                    tvName.setTextColor(ContextCompat.getColor(activity, R.color.colorBlack))
                    sizeType = 0
                    onItemClick?.invoke(0)
                    notifyDataSetChanged()
                }

            }
        }

        fun bindColor(color: Color) {
            tvName.text = color.name
            tvName.background = ContextCompat.getDrawable(activity, R.drawable.shape_border_color)
            tvName.setTextColor(ContextCompat.getColor(activity, R.color.colorBlack))
            tvName.setOnClickListener {
                if (colorType == 0) {
                    tvName.background =
                        ContextCompat.getDrawable(activity, R.drawable.shape_waiting_box)
                    tvName.setTextColor(ContextCompat.getColor(activity, R.color.colorWhite))
                    colorType = 1
                    onItemClick?.invoke(color.id)

                } else {
                    tvName.background =
                        ContextCompat.getDrawable(activity, R.drawable.shape_border_color)
                    tvName.setTextColor(ContextCompat.getColor(activity, R.color.colorBlack))
                    colorType = 0
                    onItemClick?.invoke(0)

                    notifyDataSetChanged()
                }
            }

        }

    }





}