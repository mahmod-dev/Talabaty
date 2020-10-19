package com.android.talabaty

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.android.talabaty.dbUtil.Status
import com.android.talabaty.dbUtil.ViewModelFactory
import com.android.talabaty.model.RequestOtherServicePost
import com.android.talabaty.retrofit.ApiHelperImpl
import com.android.talabaty.retrofit.RetrofitBuilder
import com.android.talabaty.util.CustomMaterialDialog.getMaterialDialogInstance
import com.android.talabaty.util.Helper
import com.android.talabaty.util.MyPreferences
import com.android.talabaty.viewModel.OrdersViewModel
import kotlinx.android.synthetic.main.activity_other_service.*
import kotlinx.android.synthetic.main.toolbar_location.*
import java.util.*

class OtherServiceActivity : AppCompatActivity() {
    val TAG = "OtherServiceActivity"
    private lateinit var viewModel: OrdersViewModel

    private var calendarDate: Calendar? = null
    private var calendarTime: Calendar? = null
    private var date: Date? = null
    private var time: Date? = null
    private var dateTime: String? = null
    var address: String? = null
    var type = 0
    var lng = 0L
    var lat = 0L
    var latSrc = 0L
    var lngSrc = 0L
    var latDist = 0L
    var lngDist = 0L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_service)
        MyPreferences.context = applicationContext
        calendarDate = Calendar.getInstance()
        calendarTime = Calendar.getInstance()
        initViewModel()
        val otherServiceId = intent.extras?.getInt("other_service_id")
        val otherCost = MyPreferences.getLong("other_service_cost")
        handleToolbar()
        linPayment1.setOnClickListener {
            startActivity(Intent(this, PaymentMethodActivity::class.java))
        }
        tvCost.text = otherCost.toString()

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

        tvTimePeriod.setOnClickListener {
            setDate()
        }

        btnConfirm.setOnClickListener {

            val details = etDetails.text.toString()
            if (latSrc == 0L || lngSrc == 0L) {
                tvPlaceOrderSrc.error = getString(R.string.enter_place)
                return@setOnClickListener
            }

            if (latDist == 0L || lngDist == 0L) {
                tvPlaceOrderDist.error = getString(R.string.enter_place)
                return@setOnClickListener
            }

            if (date == null || time == null) {
                tvTimePeriod.error = getString(R.string.enter_time_date)
                return@setOnClickListener
            } else
                dateTime =
                    "${Helper.getFormatDate(date = date!!)} ${Helper.getFormatTime(time = time!!)}"


            if (isToday()) {
                if (time!!.time < System.currentTimeMillis()) {
                    tvTimePeriod.error = getString(R.string.invalid_time)
                    //  Toast.makeText(this, getString(R.string.invalid_time), Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }
            }

            if (details.isEmpty()) {
                etDetails.error = getString(R.string.empty)
                return@setOnClickListener
            }


            viewModel.requestOtherService(
                RequestOtherServicePost(
                    otherServiceId!!,
                    details,
                    dateTime!!,
                    latSrc,
                    lngSrc,
                    latDist,
                    lngDist,
                    otherCost
                )
            )
        }

        setupObserver()

    }


    override fun onStart() {
        super.onStart()
        lng = MyPreferences.getLong("long")
        lat = MyPreferences.getLong("lat")
        type = MyPreferences.getInt("type")
        address = MyPreferences.getStr("addressName")
        Log.e(TAG, "onStart: ")

        if (type == 1) {
            tvPlaceOrderSrc.text = address
            latSrc = lat
            lngSrc = lng
        }

        if (type == 2) {
            tvPlaceOrderDist.text = address
            latDist = lat
            lngDist = lng
        }

        MyPreferences.setInt("type", 0)

    }


    private fun setTime() {
        val currentHour = calendarTime!!.get(Calendar.HOUR_OF_DAY)
        val currentMinute = calendarTime!!.get(Calendar.MINUTE)
        val timePickerDialog = TimePickerDialog(
            this,
            { timePicker, hourOfDay, minutes ->

                calendarTime!![Calendar.HOUR_OF_DAY] = hourOfDay
                calendarTime!![Calendar.MINUTE] = minutes
                calendarTime!![Calendar.SECOND] = 0
                time = calendarTime!!.time
                dateTime =
                    "${Helper.getFormatDate(date = date!!)} \n ${Helper.getFormatTime(time = time!!)}"


                if (isToday()) {
                    if (time!!.time >= System.currentTimeMillis()) {
                        tvTimePeriod.text = dateTime
                    } else {
                        tvTimePeriod.error = getString(R.string.invalid_time)
                        Toast.makeText(this, getString(R.string.invalid_time), Toast.LENGTH_LONG)
                            .show()
                    }
                } else {
                    tvTimePeriod.text = dateTime
                }
            }, currentHour, currentMinute, false
        )

        timePickerDialog.show()

    }


    private fun setDate() {

        val year: Int = calendarDate!!.get(Calendar.YEAR)
        val month: Int = calendarDate!!.get(Calendar.MONTH)
        val day: Int = calendarDate!!.get(Calendar.DAY_OF_MONTH)
        Log.e("system ", "setDateStart: day $day month: $month year: $year")

        val date = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                calendarDate!!.set(Calendar.YEAR, year)
                calendarDate!!.set(Calendar.MONTH, monthOfYear)
                calendarDate!!.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                date = calendarDate!!.time
                Log.e(
                    "setDateStart",
                    "DatePickerDialog: day $dayOfMonth month: $monthOfYear year: $year"
                )
                setTime()


            }, year, month, day
        )
        date.datePicker.minDate = System.currentTimeMillis() - 1000

        date.show()

    }


    private fun isToday(): Boolean {
        val c = Calendar.getInstance()
        val systemMonth = Helper.getFormatDate(date = c)
        val currentMonth = Helper.getFormatDate(date = date!!)
        return (systemMonth == currentMonth)
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

    private fun initViewModel() {

        viewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiHelperImpl(RetrofitBuilder.apiService), application)
        ).get(OrdersViewModel::class.java)
    }

    private fun setupObserver() {

        viewModel.getAllOtherService().observe(this,
            Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        progressBar.visibility = View.GONE
                        it.data?.let { users ->
                            Toast.makeText(this, users.message, Toast.LENGTH_SHORT).show()
                            MyPreferences.setLong("long", 0)
                            MyPreferences.setLong("lat", 0)
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

}