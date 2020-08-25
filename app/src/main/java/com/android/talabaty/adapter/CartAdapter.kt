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
import com.android.talabaty.util.MyPreferences
import com.android.talabaty.viewModel.CartViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.mahmoud.todoapp.util.dbUtil.Status
import kotlinx.android.synthetic.main.item_cart.view.*

class CartAdapter(var activity: Activity, var data: ArrayList<Cart>) :
    RecyclerView.Adapter<CartAdapter.ViewHolder>() {
    val TAG = "CartAdapter"
    var mListener: OnItemClickListener? = null
    private lateinit var viewModel: CartViewModel

    init {
        MyPreferences.context = activity
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
            .inflate(R.layout.item_cart, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.bind(data[i].product)
        setupObserverAddToCart()
        setupObserverRemoveFromCart(i)
        setupObserverAddToFav()
        setupObserverDeleteFav()
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvCartName: TextView = itemView.tvCartName
        var tvCartDetails: TextView = itemView.tvCartDetails
        var tvCartPrice: TextView = itemView.tvCartPrice
        var tvQuantity: TextView = itemView.tvQuantity
        var imgCart: ImageView = itemView.imgCart
        var imgCartFav: ImageView = itemView.imgCartFav
        var imgSubtract: ImageView = itemView.imgSubtract
        var imgAdd: ImageView = itemView.imgAdd
        var rlDelete: RelativeLayout = itemView.rlDelete

        var isFav = false

        fun bind(product: Product) {


            tvCartName.text = product.name
            tvQuantity.text = MyPreferences.getInt("count").toString()

            tvCartDetails.text = product.description
            if (tvCartDetails.text.length > 50) {
                val txt = tvCartDetails.text.take(50)
                tvCartDetails.text = " $txt..."

            }

            tvCartPrice.text =
                "${product.price} ${itemView.context.resources.getString(R.string.reial)}"


            if (product.image.isNotEmpty()) {
                Glide.with(itemView.context).load(product.image)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.ic_icon_loading)
                    .error(R.drawable.white)
                    .into(imgCart)
            }

            rlDelete.setOnClickListener {
                viewModel.deleteFromCart(product.id)
            }

            imgAdd.setOnClickListener {
                MyPreferences.setInt("count",MyPreferences.getInt("count")+1)

                viewModel.changeQuantity(product.id, "increase")
                tvQuantity.text = MyPreferences.getInt("count").toString()

            }

            imgSubtract.setOnClickListener {

                if (MyPreferences.getInt("count") <= 0) {
                    MyPreferences.setInt("count",1)
                    tvQuantity.text = MyPreferences.getInt("count").toString()
                    return@setOnClickListener
                }
                MyPreferences.setInt("count",MyPreferences.getInt("count")-1)

                tvQuantity.text = MyPreferences.getInt("count").toString()

                viewModel.changeQuantity(product.id, "decrease")
            }

            if (product.is_favorite == 1) {
                isFav = true
                imgCartFav.setImageResource(R.drawable.ic_favo)
                viewModel.addToFav(product.id)

            } else {
                isFav = false
                imgCartFav.setImageResource(R.drawable.ic_icon_love)
                viewModel.deleteFromFav(product.id)
            }


            imgCartFav.setOnClickListener {
                if (product.is_favorite == 1) {
                    isFav = true
                    imgCartFav.setImageResource(R.drawable.ic_favo)
                    viewModel.addToFav(product.id)

                } else {
                    isFav = false
                    imgCartFav.setImageResource(R.drawable.ic_icon_love)
                    viewModel.deleteFromFav(product.id)
                }
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

    private fun setupObserverAddToCart() {

        viewModel.getChangeQuantity().observe(activity as FragmentActivity,
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


    private fun setupObserverRemoveFromCart(position: Int) {

        viewModel.getDeleteToCart().observe(activity as FragmentActivity,
            Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        // progressBar.visibility = View.GONE
                        it.data?.let { users ->
                            Toast.makeText(activity, users.message, Toast.LENGTH_LONG).show()
                            data.removeAt(position)
                            notifyItemRemoved(position)
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

    private fun setupObserverAddToFav() {

        viewModel.getAddToFAv().observe(activity as FragmentActivity,
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

    private fun setupObserverDeleteFav() {

        viewModel.getDeleteFav().observe(activity as FragmentActivity,
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