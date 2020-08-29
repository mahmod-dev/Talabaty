package com.android.talabaty

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.android.talabaty.dbUtil.Status
import com.android.talabaty.dbUtil.ViewModelFactory
import com.android.talabaty.model.NewOrderPost
import com.android.talabaty.retrofit.ApiHelperImpl
import com.android.talabaty.retrofit.RetrofitBuilder
import com.android.talabaty.util.Helper
import com.android.talabaty.util.MyPreferences
import com.android.talabaty.viewModel.OrdersViewModel
import kotlinx.android.synthetic.main.activity_delivery_services.*
import kotlinx.android.synthetic.main.toolbar_location.*

class DeliveryServicesActivity : AppCompatActivity() {
    val TAG = "DeliveryServicesAct"
    private lateinit var viewModel: OrdersViewModel
    var type = 0
    var lng = 0L
    var lat = 0L
    var latSrc = 0L
    var lngSrc = 0L
    var latDist = 0L
    var lngDist = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delivery_services)
        initViewModel()
        val categoryId = intent.extras?.getInt("categoryId")
        val deliveryCost = intent.extras?.getDouble("deliveryCost")
        MyPreferences.context = this
        MyPreferences.setInt("type", 0)
        setupObserverGetCart()

        Log.e(TAG, "deliveryCost: $deliveryCost ,, categoryId: $categoryId  ")
        imgArrowBack.setOnClickListener {
            finish()
        }

        imgCart.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))

        }

        imgFav.setOnClickListener {
            startActivity(Intent(this, FavoriteActivity::class.java))

        }


        linPayment1.setOnClickListener {
            startActivity(Intent(this, PaymentMethodActivity::class.java))
        }
        tvCost.text = deliveryCost.toString()

        tvPlaceOrderSrc.setOnClickListener {
            val intent = Intent(this, MapActivity::class.java)
            intent.putExtra("type", 1)
            startActivity(intent)
        }

        tvPlaceOrderDist.setOnClickListener {
            val intent = Intent(this, MapActivity::class.java)
            intent.putExtra("type", 2)
            startActivity(intent)
        }

        btnConfirm.setOnClickListener {

          val details =  etDetails.text.toString()
            if (latSrc==0L || lngSrc==0L){
                tvPlaceOrderSrc.error = getString(R.string.enter_place)
                return@setOnClickListener
            }

            if (latDist==0L ||lngDist==0L ){
                tvPlaceOrderDist.error = getString(R.string.enter_place)
                return@setOnClickListener
            }

            if (details.isEmpty()){
                etDetails.error = getString(R.string.empty)
                return@setOnClickListener
            }






            viewModel.createNewOrder(NewOrderPost(categoryId!!,details,Helper.getFormatDateTime(),latSrc,lngSrc,latDist,lngDist,deliveryCost!!))
        }


    }

    private fun initViewModel() {

        viewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiHelperImpl(RetrofitBuilder.apiService), application)
        ).get(OrdersViewModel::class.java)
    }

    private fun setupObserverGetCart() {

        viewModel.getCreateNewOrder().observe(this,
            Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        progressBar.visibility = View.GONE
                        it.data?.let { users ->
                        Toast.makeText(this, users.message,Toast.LENGTH_SHORT).show()

                        }
                    }
                    Status.LOADING -> {
                        progressBar.visibility = View.VISIBLE

                    }
                    Status.ERROR -> {
                        progressBar.visibility = View.GONE
                        Helper.showFilterDialog(this, it.message!!).show()
                        Log.e(TAG, "setupObserver: " + it.message)

                    }
                }

            }
        )
    }

    override fun onStart() {
        super.onStart()
        lng = MyPreferences.getLong("long")
        lat = MyPreferences.getLong("lat")
        type = MyPreferences.getInt("type")
        Log.e(TAG, "onStart: ")

        if (type == 1) {
            tvPlaceOrderSrc.text = "$lat, $lng"
            latSrc = lat
            lngSrc = lng
        }

        if (type == 2) {
            tvPlaceOrderDist.text = "$lat, $lng"
            latDist = lat
            lngDist = lng
        }
    }

}