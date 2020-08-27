package com.android.talabaty.adapter

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.android.talabaty.R
import com.android.talabaty.dbUtil.ViewModelFactory
import com.android.talabaty.model.*
import com.android.talabaty.retrofit.ApiHelperImpl
import com.android.talabaty.retrofit.RetrofitBuilder
import com.android.talabaty.util.Helper
import com.android.talabaty.viewModel.CartViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.android.talabaty.dbUtil.Status
import kotlinx.android.synthetic.main.item_cart_favorite.view.*

class FavoriteCartAdapter(var activity: Activity, var data: ArrayList<Product>) :
    RecyclerView.Adapter<FavoriteCartAdapter.ViewHolder>() {
    val TAG = "FavoriteCartAdapter"
    var mListener: OnItemClickListener? = null
    private lateinit var viewModel: CartViewModel

    init {
        initViewModel()
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
        fun onItemLongClick(position: Int)
    }

    fun setOnClickListener(listener: OnItemClickListener?) {
        mListener = listener
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_cart_favorite, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.bind(data[i],i)
        setupObserverDeleteFromCart()
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var tvCartName: TextView = itemView.tvCartName
        var tvCartDetails: TextView = itemView.tvCartDetails
        var tvCartPrice: TextView = itemView.tvCartPrice
        var imgFav: ImageView = itemView.imgCartFav
        var imgCart: ImageView = itemView.imgCart

        fun bind(product: Product,position: Int) {
            tvCartName.text = product.name

            tvCartDetails.text = product.description
            if (tvCartDetails.text.length > 50) {
                val txt = tvCartDetails.text.take(50)
                tvCartDetails.text = " $txt..."

            }

            tvCartPrice.text = "${product.price} ${itemView.context.resources.getString(R.string.reial)}"


            if (product.image.isNotEmpty()) {
                Glide.with(itemView.context).load(product.image)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.ic_icon_loading)
                    .error(R.drawable.white)
                    .into(imgCart)
            }

            imgFav.setOnClickListener {
                data.remove(data[position])
                notifyItemRemoved(position)
                notifyItemRangeRemoved(position, data.size)
                notifyDataSetChanged()

                    viewModel.deleteFromFav(product.id)

            }

        }

        init {
            itemView.setOnClickListener {
                if (mListener != null) {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        mListener!!.onItemClick(position)
                    }
                }
            }

            itemView.setOnLongClickListener {
                if (mListener != null) {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        mListener!!.onItemLongClick(position)
                    }
                }
                false

            }
        }
    }


    private fun initViewModel() {

        viewModel = ViewModelProviders.of(
            activity as FragmentActivity,
            ViewModelFactory(ApiHelperImpl(RetrofitBuilder.apiService), activity.application)
        ).get(CartViewModel::class.java)
    }

    private fun setupObserverDeleteFromCart() {

        viewModel.getDeleteToCart().observe(activity as FragmentActivity,
            Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        // progressBar.visibility = View.GONE
                        it.data?.let { users ->
                            Toast.makeText(activity, users.message, Toast.LENGTH_LONG).show()
                        }
                    }
                    Status.LOADING -> {
                        //progressBar.visibility = View.VISIBLE

                    }
                    Status.ERROR -> {
                        // progressBar.visibility = View.GONE
                        Helper.showFilterDialog(activity, it.message!!).show()
                        Log.e(TAG, "setupObserver: " + it.message)

                    }
                }

            }
        )
    }


}