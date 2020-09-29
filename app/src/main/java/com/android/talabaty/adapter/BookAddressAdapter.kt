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
import com.android.talabaty.model.Address
import com.android.talabaty.util.MyPreferences
import kotlinx.android.synthetic.main.item_book_address.view.*

class BookAddressAdapter(
    var activity: Activity,
    var data: ArrayList<Address>
) :
    RecyclerView.Adapter<BookAddressAdapter.MyViewHolder>() {
    val TAG = "BookAddressAdapter"
    var onItemEditClick: ((Int, Address) -> Unit)? = null
    var onItemDeleteClick: ((Int, Int) -> Unit)? = null
    var onItemClick: ((Int, Address) -> Unit)? = null

    init {
        MyPreferences.context = activity
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_book_address, viewGroup, false)
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
        var imgEdit: ImageView = itemView.imgEdit
        var imgDelete: ImageView = itemView.imgDelete
        var card: CardView = itemView.cardPayment


        fun bind(address: Address) {
            tvName.text = address.address

            imgDelete.setOnClickListener {
                onItemDeleteClick?.invoke(data[adapterPosition].id, adapterPosition)
            }

            imgEdit.setOnClickListener {
                onItemEditClick?.invoke(data[adapterPosition].id, data[adapterPosition])
            }

            card.setOnClickListener {
                onItemClick?.invoke(data[adapterPosition].id, data[adapterPosition])
            }

        }

    }


}