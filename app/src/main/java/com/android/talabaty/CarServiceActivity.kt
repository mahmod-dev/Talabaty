package com.android.talabaty

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.android.talabaty.dbUtil.Status
import com.android.talabaty.dbUtil.ViewModelFactory
import com.android.talabaty.model.RequestCarPost
import com.android.talabaty.model.SpinnerObj
import com.android.talabaty.retrofit.ApiHelperImpl
import com.android.talabaty.retrofit.RetrofitBuilder
import com.android.talabaty.util.CustomMaterialDialog
import com.android.talabaty.util.CustomMaterialDialog.getMaterialDialogInstance
import com.android.talabaty.util.Helper
import com.android.talabaty.util.MyPreferences
import com.android.talabaty.viewModel.OrdersViewModel
import kotlinx.android.synthetic.main.activity_car_service.*
import kotlinx.android.synthetic.main.activity_car_service.btnConfirm
import kotlinx.android.synthetic.main.activity_car_service.etDetails
import kotlinx.android.synthetic.main.simple_spinner_item.*
import kotlinx.android.synthetic.main.toolbar_location.*

class CarServiceActivity : AppCompatActivity() {
    val TAG = "CarServiceActivity"
    private lateinit var viewModel: OrdersViewModel
    var car : ArrayList<SpinnerObj>? = null
    var type = 0
    var lng = 0L
    var lat = 0L
    var latSrc = 0L
    var lngSrc = 0L
    var latDist = 0L
    var lngDist = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_car_service)
        MyPreferences.context = this
        MyPreferences.setInt("type", 0)
         car = ArrayList()
        car?.add(SpinnerObj(0,getString(R.string.car_type)))
        handleToolbar()
        initViewModel()
        initSpinnerAdapter(car!!)
        viewModel.getCars()
        setupObserverGetAllCars()
        setupObserverRequestCar()
        linPayment.setOnClickListener {
            startActivity(Intent(this, PaymentMethodActivity::class.java))
        }

        tvSrc.setOnClickListener {
            val intent = Intent(this, MapActivity::class.java)
            intent.putExtra("type", 1)
            startActivity(intent)
        }

        tvDist.setOnClickListener {
            val intent = Intent(this, MapActivity::class.java)
            intent.putExtra("type", 2)
            startActivity(intent)
        }



        tvPrice.text = "${MyPreferences.getLong("carCost")} SR"
        btnConfirm.setOnClickListener {

            val details =  etDetails.text.toString()

            if (latSrc==0L || lngSrc==0L){
                tvSrc.error = getString(R.string.enter_place)
                return@setOnClickListener
            }

            if (latDist==0L ||lngDist==0L ){
                tvDist.error = getString(R.string.enter_place)
                return@setOnClickListener
            }

            if (details.isEmpty()){
                etDetails.error = getString(R.string.empty)
                return@setOnClickListener
            }

            if (spCars.selectedItem.toString().equals(getString(R.string.select_type))){
                Toast.makeText(this, getString(R.string.select_car_type), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
        val obj =     spCars.selectedItem as SpinnerObj
            Log.e(TAG, "onCreate: ${obj.name},  ${obj.id}" )
            viewModel.requestCar(RequestCarPost(obj.id,details,latSrc,lngSrc,latDist,lngDist,MyPreferences.getLong("carCost").toDouble()))
        }


    }

    private fun initViewModel() {

        viewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiHelperImpl(RetrofitBuilder.apiService), application)
        ).get(OrdersViewModel::class.java)
    }

    private fun setupObserverGetAllCars() {
        viewModel.getAllCars().observe(this,
            Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                     //   progressBar.visibility = View.GONE
                        it.data?.let { users ->
                            for (i in users.cars.indices) {

                                car?.add(SpinnerObj(users.cars[i].id,users.cars[i].name))
                            }
                            initSpinnerAdapter(car!!)


                        }
                    }
                    Status.LOADING -> {
//                        progressBar.visibility = View.VISIBLE

                    }
                    Status.ERROR -> {
                       // progressBar.visibility = View.GONE
                        getMaterialDialogInstance(it.message!!)
                        Log.e(TAG, "setupObserver: " + it.message)

                    }
                }

            }
        )
    }

    private fun setupObserverRequestCar() {

        viewModel.getRequestCar().observe(this,
            Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        progressBar.visibility = View.GONE
                        it.data?.let { users ->

                            Toast.makeText(this, users.message, Toast.LENGTH_LONG).show()
                            finish()
                        }
                    }
                    Status.LOADING -> {
                        progressBar.visibility = View.VISIBLE

                    }
                    Status.ERROR -> {
                        progressBar.visibility = View.GONE
                        getMaterialDialogInstance(it.message!!)
                        Log.e(TAG, "setupObserver: " + it.message)

                    }
                }

            }
        )
    }

    private fun initSpinnerAdapter(list: ArrayList<SpinnerObj>) {

        val adapter = ArrayAdapter(this,R.layout.simple_spinner_item, list)
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)

        spCars.adapter = adapter


    }

    override fun onStart() {
        super.onStart()
        lng = MyPreferences.getLong("long")
        lat = MyPreferences.getLong("lat")
        type = MyPreferences.getInt("type")
        Log.e(TAG, "onStart: ")

        if (type == 1) {
            tvSrc.text = "$lat, $lng"
            latSrc = lat
            lngSrc = lng
        }

        if (type == 2) {
            tvDist.text = "$lat, $lng"
            latDist = lat
            lngDist = lng
        }
    }

    private fun handleToolbar() {
        imgArrowBack.setOnClickListener {
            finish()
        }

        imgCart.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))

        }

        imgFav.setOnClickListener {
            startActivity(Intent(this, FavoriteActivity::class.java))

        }
        tvHomeLocation.text = MyPreferences.getStr("city")


    }


}