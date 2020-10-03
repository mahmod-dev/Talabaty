package com.android.talabaty.adapter

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.android.talabaty.ProductDetailsActivity
import com.android.talabaty.R
import com.android.talabaty.dbUtil.ViewModelFactory
import com.android.talabaty.model.*
import com.android.talabaty.retrofit.ApiHelperImpl
import com.android.talabaty.retrofit.RetrofitBuilder
import com.android.talabaty.viewModel.CartViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.android.talabaty.dbUtil.Status
import com.android.talabaty.util.CustomMaterialDialog.getMaterialDialogInstance
import com.android.talabaty.util.Helper.setUrlImage
import kotlinx.android.synthetic.main.item_product.view.*


class RecycleCategoryAdapter(var activity: Activity, var data: ArrayList<Product>) :
    RecyclerView.Adapter<RecycleCategoryAdapter.ViewHolder>() {
    val TAG = "RecycleCategoryAdapter"
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

    fun getStores(): List<Product> {
        return data
    }


    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        i: Int
    ): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_product, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(
        viewHolder: ViewHolder,
        i: Int
    ) {
        viewHolder.bind(data[i])
        setupObserverAddToCart(0)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var btnAddToCartProduct: TextView = itemView.btnAddToCartProduct
        var tvPriceProduct: TextView = itemView.tvPriceProduct
        var tvDetailsProduct: TextView = itemView.tvDetailsProduct
        var tvTitleProduct: TextView = itemView.tvTitleProduct
        var imgStore: ImageView = itemView.carImgProduct

        fun bind(product: Product) {
            tvTitleProduct.text = product.name

            tvDetailsProduct.text = product.description

            if (tvDetailsProduct.text.length > 50) {
                val txt = tvDetailsProduct.text.take(50)
                tvDetailsProduct.text = " $txt..."

            }
            tvPriceProduct.text =
                "${productPrice(product)} ${itemView.context.resources.getString(R.string.reial)}"




            if (product.image.isNotEmpty()) {
                imgStore.setUrlImage(activity, product.image)
            }

            if (product.has_colors==1 || product.has_sizes==1){
                btnAddToCartProduct.text = activity.getString(R.string.show_details)
            }

            btnAddToCartProduct.setOnClickListener {
                if (product.has_colors==1 || product.has_sizes==1){
                    val intent = Intent(activity,ProductDetailsActivity::class.java)
                    intent.putExtra("has_colors",product.has_colors)
                    intent.putExtra("has_sizes", product.has_sizes)
                    intent.putExtra("productId", product.id)
                    activity.startActivity(intent)

                }else{
                    Log.e(TAG, "productId: ${product.id}" )
                    viewModel.addToCart(product.id,1,0,0)
                    setupObserverAddToCart(1)
                }

            }

        }

        init {
            itemView.setOnClickListener {
                if (mListener != null) {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        mListener!!.onItemClick(data[position].id)
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

    private fun setupObserverAddToCart( a: Int) {

        viewModel.getAddToCart().observe(activity as FragmentActivity,
            Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        // progressBar.visibility = View.GONE
                        it.data?.let { users ->
                            if (a==1){
                                Toast.makeText(activity, users.message, Toast.LENGTH_LONG).show()

                            }
                            viewModel.getCart()
                        }
                    }
                    Status.LOADING -> {

                        //progressBar.visibility = View.VISIBLE

                    }
                    Status.ERROR -> {
                        // progressBar.visibility = View.GONE
                        activity.getMaterialDialogInstance(it.message!!)
                        Log.e(TAG, "setupObserver: " + it.message)

                    }
                }

            }
        )
    }

    private fun productPrice(product: Product): Int {

        return (product.offer_price?.toInt()) ?: product.price
    }
}