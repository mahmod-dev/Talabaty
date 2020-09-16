package com.android.talabaty

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.android.talabaty.dbUtil.Status
import com.android.talabaty.dbUtil.ViewModelFactory
import com.android.talabaty.model.AddPaymentCardPost
import com.android.talabaty.model.EditPaymentCardPost
import com.android.talabaty.retrofit.ApiHelperImpl
import com.android.talabaty.retrofit.RetrofitBuilder
import com.android.talabaty.util.CustomMaterialDialog.getMaterialDialogInstance
import com.android.talabaty.util.Helper
import com.android.talabaty.viewModel.CardPaymentViewModel
import kotlinx.android.synthetic.main.activity_add_credit_card.*
import kotlinx.android.synthetic.main.activity_add_credit_card.btnConfirm
import kotlinx.android.synthetic.main.activity_add_credit_card.etName
import kotlinx.android.synthetic.main.activity_add_credit_card.progressBar
import java.text.SimpleDateFormat
import java.util.*
import kotlin.reflect.typeOf


class AddCreditCardActivity : AppCompatActivity() {
    private lateinit var viewModel: CardPaymentViewModel
    private var startDate: Date? = null
    private var calendarDate: Calendar? = null
    private var strStartDate: String? = null
    private var cardId: Int = 0
    private var method_id: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_credit_card)
        val paymentId = intent.extras!!.getInt("paymentId")
        val edit = intent.extras!!.getInt("edit")
        initViewModel()
        calendarDate = Calendar.getInstance()
        if (edit == 1) {
            tvTitle.text = getString(R.string.edit_card)
            val card_number = intent.extras!!.getString("card_number")
            val expired_date = intent.extras!!.getString("expired_date")
            val validation_number = intent.extras!!.getString("validation_number")
            val name_cardholder = intent.extras!!.getString("name_cardholder")
            method_id = intent.extras!!.getInt("method_id")
            cardId = intent.extras!!.getInt("cardId")
            etCardNum.setText(card_number)
            etName.setText(name_cardholder)
            etDate.setText(expired_date)
            etValidateNum.setText(validation_number)
        }

        btnConfirm.setOnClickListener {

            val cardNum = etCardNum.text.toString()
            val fullName = etName.text.toString()
            val date = etDate.text.toString()
            val validationNum = etValidateNum.text.toString()

            if (cardNum.isEmpty()) {
                etCardNum.error = getString(R.string.empty)
                return@setOnClickListener
            }
            if (fullName.isEmpty()) {
                etName.error = getString(R.string.empty)
                return@setOnClickListener
            }
            if (date.isEmpty()) {
                etDate.error = getString(R.string.empty)
                return@setOnClickListener
            }
            if (validationNum.isEmpty()) {
                etValidateNum.error = getString(R.string.empty)
                return@setOnClickListener
            }

            if (edit == 1) {
                if (strStartDate == null) {
                    strStartDate = date
                }
                val card = EditPaymentCardPost(
                    cardId,
                    method_id,
                    cardNum,
                    strStartDate!!,
                    validationNum,
                    fullName
                )

                viewModel.editCardPayment(card)
                return@setOnClickListener
            }
            val card =
                AddPaymentCardPost(paymentId, cardNum, strStartDate!!, validationNum, fullName)
            viewModel.addCardPayment(card)

        }

        etDate.setOnClickListener {
            setDateStart()
        }
        setupObserver()
        setupObserverEditCard()

        imgArrowBack.setOnClickListener {
            finish()
        }


    }


    private fun initViewModel() {

        viewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiHelperImpl(RetrofitBuilder.apiService), application)
        ).get(CardPaymentViewModel::class.java)
    }

    private fun setupObserver() {

        viewModel.getCardPayment().observe(this,
            Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        it.data?.let { users ->
                            progressBar.visibility = View.GONE
                            Toast.makeText(this, users.message, Toast.LENGTH_SHORT).show()

                            startActivity(Intent(applicationContext, WaitingActivity::class.java))
                            finish()

                        }
                    }
                    Status.LOADING -> {
                        progressBar.visibility = View.VISIBLE
                    }
                    Status.ERROR -> {
                        progressBar.visibility = View.GONE
                        getMaterialDialogInstance(it.message!!)
                    }
                }
            }
        )
    }

    private fun setupObserverEditCard() {

        viewModel.getEditPaymentCard().observe(this,
            Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        it.data?.let { users ->
                            progressBar.visibility = View.GONE
                            Toast.makeText(this, users.message, Toast.LENGTH_SHORT).show()
                            finish()

                        }
                    }
                    Status.LOADING -> {
                        progressBar.visibility = View.VISIBLE
                    }
                    Status.ERROR -> {
                        progressBar.visibility = View.GONE
                        getMaterialDialogInstance(it.message!!)
                    }
                }
            }
        )
    }


    private fun setDateStart() {
        val year: Int = calendarDate!!.get(Calendar.YEAR)
        val month: Int = calendarDate!!.get(Calendar.MONTH)
        val day: Int = calendarDate!!.get(Calendar.DAY_OF_MONTH)

        val date = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                calendarDate!!.set(Calendar.YEAR, year)
                calendarDate!!.set(Calendar.MONTH, monthOfYear)
                calendarDate!!.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                startDate = calendarDate!!.time
                updateDateText(calendarDate!!, etDate)
                strStartDate = Helper.getFormatDate(format = "MM/yy", date = startDate!!)

            }, year, month, day
        )
        date.datePicker.minDate = System.currentTimeMillis() - 1000

        date.show()

    }

    private fun updateDateText(c: Calendar, textView: TextView) {
        val myFormat = "MM-yyyy" //In which you need put here
        var locale = Locale("ar", "SA")
        if (Locale.getDefault().language == "en") {
            locale = Locale.ENGLISH
        }
        val sdf = SimpleDateFormat(myFormat, Locale.ENGLISH)


        textView.text = sdf.format(c.time)

    }


}