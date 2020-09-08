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
import com.android.talabaty.util.MyPreferences
import com.android.talabaty.viewModel.CartViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.android.talabaty.dbUtil.Status
import com.android.talabaty.util.CustomMaterialDialog.getMaterialDialogInstance
import kotlinx.android.synthetic.main.item_cart.view.*

class CartAdapter(var activity: Activity, var data: ArrayList<Cart>) :

    RecyclerView.Adapter<CartAdapter.MyViewHolder>() {
    val TAG = "CartAdapter"
    var onItemClick: ((Cart,Int )-> Unit)? = null

    private lateinit var viewModel: CartViewModel
    init {
        MyPreferences.context = activity
        initViewModel()
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_cart, viewGroup, false)
        return MyViewHolder(view)

    }

    override fun onBindViewHolder(myViewHolder: MyViewHolder, i: Int) {
        myViewHolder.bind(data[i].product!!, i)
        setupObserverChangeQuantity()
        setupObserverAddToFav()
        setupObserverDeleteFav()

    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvCartName: TextView = itemView.tvCartName
        var tvCartDetails: TextView = itemView.tvCartDetails
        var tvCartPrice: TextView = itemView.tvCartPrice
        var tvQuantity: TextView = itemView.tvQuantity
        var imgCart: ImageView = itemView.imgCart
        var imgCartFav: ImageView = itemView.imgCartFav
        var imgSubtract: ImageView = itemView.imgSubtract
        var imgAdd: ImageView = itemView.imgAdd
        var rlDelete: RelativeLayout = itemView.rlDelete

        fun bind(product: Product, position: Int) {

            tvQuantity.text = data[position].quantity
            MyPreferences.setInt("count",data[position].quantity.toInt())

            tvCartName.text = product.name
            tvCartPrice.text = (product.price*MyPreferences.getInt("count")).toString()

            tvCartDetails.text = product.description
            if (tvCartDetails.text.length > 50) {
                val txt = tvCartDetails.text.take(50)
                tvCartDetails.text = " $txt..."

            }

            "${product.price} ${itemView.context.resources.getString(R.string.reial)}"


            if (product.image.isNotEmpty()) {
                Glide.with(itemView.context).load(product.image)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.ic_icon_loading)
                    .error(R.drawable.white)
                    .into(imgCart)
            }

//            rlDelete.setOnClickListener {
//                data.removeAt(position)
////                notifyItemRemoved(position)
////                notifyItemRangeRemoved(position, data.size)
//                notifyDataSetChanged()
//                viewModel.deleteFromCart(product.id)
//
//
//                Log.e(TAG, "bind: ${product.id} " )
//
//
//            }

            rlDelete.setOnClickListener {
                Log.e(TAG, "bind product id: ${product.id}" )
                Log.e(TAG, "bind: ${adapterPosition},,, $position" )
                onItemClick?.invoke(data[adapterPosition],position)
            }

            imgAdd.setOnClickListener {
                  MyPreferences.setInt("count",MyPreferences.getInt("count")+1)

                tvCartPrice.text = (product.price*MyPreferences.getInt("count")).toString()
                viewModel.changeQuantity(product.id, "increase")
                tvQuantity.text = MyPreferences.getInt("count").toString()

            }

            imgSubtract.setOnClickListener {

                if (MyPreferences.getInt("count") <= 1) {
                    Toast.makeText(
                        activity,
                        activity.getString(R.string.minimum_order),
                        Toast.LENGTH_SHORT
                    ).show()
                    MyPreferences.setInt("count",1)
                    tvQuantity.text = MyPreferences.getInt("count").toString()
                    tvCartPrice.text = product.price.toString()

                }else{
                    MyPreferences.setInt("count",MyPreferences.getInt("count")-1)

                    tvQuantity.text = MyPreferences.getInt("count").toString()
                    tvCartPrice.text = (product.price*MyPreferences.getInt("count")).toString()


                    viewModel.changeQuantity(product.id, "decrease")
                }

            }

            if (product.is_favorite == 1) {
                imgCartFav.setImageResource(R.drawable.ic_favo)

            } else {
                imgCartFav.setImageResource(R.drawable.ic_icon_love)

            }


            imgCartFav.setOnClickListener {
                if (product.is_favorite == 1) {
                    imgCartFav.setImageResource(R.drawable.ic_icon_love)
                    viewModel.deleteFromFav(product.id)

                } else {
                    imgCartFav.setImageResource(R.drawable.ic_favo)
                    viewModel.addToFav(product.id)

                }
            }


        }

        init {

        }


    }


    private fun initViewModel() {

        viewModel = ViewModelProviders.of(
            activity as FragmentActivity,
            ViewModelFactory(ApiHelperImpl(RetrofitBuilder.apiService), activity.application)
        ).get(CartViewModel::class.java)
    }

    private fun setupObserverChangeQuantity() {

        viewModel.getChangeQuantity().observe(activity as FragmentActivity,
            Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        // progressBar.visibility = View.GONE
                        it.data?.let { users ->
                           // Toast.makeText(activity, users.message, Toast.LENGTH_SHORT).show()



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



    private fun setupObserverAddToFav() {

        viewModel.getAddToFav().observe(activity as FragmentActivity,
            Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        // progressBar.visibility = View.GONE
                        it.data?.let { users ->
                            Toast.makeText(activity, users.message, Toast.LENGTH_SHORT).show()
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

    private fun setupObserverDeleteFav() {

        viewModel.getDeleteFav().observe(activity as FragmentActivity,
            Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        // progressBar.visibility = View.GONE

                        it.data?.let { users ->
                            Toast.makeText(activity, users.message, Toast.LENGTH_SHORT).show()
                        }

                    }
                    Status.LOADING -> {
                        //progressBar.visibility = View.VISIBLE

                    }
                    Status.ERROR -> {
                        // progressBar.visibility = View.GONE
                       // Helper.showFilterDialog(activity, it.message!!).show()
                        activity.getMaterialDialogInstance(it.message!!)

                        Log.e(TAG, "setupObserver: " + it.message)

                    }
                }

            }
        )
    }

}