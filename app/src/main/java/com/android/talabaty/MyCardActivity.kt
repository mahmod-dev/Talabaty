package com.android.talabaty

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.talabaty.adapter.EditPaymentMethodAdapter
import com.android.talabaty.dbUtil.Status
import com.android.talabaty.dbUtil.ViewModelFactory
import com.android.talabaty.model.PaymentCard
import com.android.talabaty.retrofit.ApiHelperImpl
import com.android.talabaty.retrofit.RetrofitBuilder
import com.android.talabaty.util.CustomMaterialDialog.getMaterialDialogInstance
import com.android.talabaty.viewModel.CardPaymentViewModel
import kotlinx.android.synthetic.main.activity_my_cardctivity.*
import kotlinx.android.synthetic.main.title_toolbar.*
import kotlinx.android.synthetic.main.title_toolbar.imgArrowBack

class MyCardActivity : AppCompatActivity() {
    val TAG = "MyCardActivity"
    private lateinit var viewModel: CardPaymentViewModel
    var paymentAdapter: EditPaymentMethodAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_cardctivity)
        handleToolbar()
        initViewModel()

        viewModel.myPaymentCard()
        swipeToRefresh()
        setupObserver()
        setupObserverDelete()
    }

    private fun handleToolbar() {
        imgArrowBack.setOnClickListener {
            finish()

        }

        tvTitleToolbar.text = getString(R.string.mycards)

    }

    private fun initViewModel() {

        viewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiHelperImpl(RetrofitBuilder.apiService), application)
        ).get(CardPaymentViewModel::class.java)
    }

    private fun setupObserver() {

        viewModel.getMyPaymentCard().observe(this,
            Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        it.data?.let { users ->
                            swipeRefresh.isRefreshing = false
                            initRecycleView(users.payment_cards)
                        }
                    }
                    Status.LOADING -> {
                        swipeRefresh.isRefreshing = true
                    }
                    Status.ERROR -> {
                        swipeRefresh.isRefreshing = false

                        getMaterialDialogInstance(it.message!!)

                    }
                }

            }
        )
    }

    private fun setupObserverDelete() {

        viewModel.getDeletePaymentCard().observe(this,
            Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        it.data?.let { users ->
                            Toast.makeText(this, users.message, Toast.LENGTH_SHORT).show()

                        }
                    }
                    Status.LOADING -> {
                    }
                    Status.ERROR -> {

                        getMaterialDialogInstance(it.message!!)

                    }
                }

            }
        )
    }

    private fun initRecycleView(methods: ArrayList<PaymentCard>) {
        paymentAdapter = EditPaymentMethodAdapter(this, methods)
        val linearLayoutManager = LinearLayoutManager(this)
        rvCards.layoutManager = linearLayoutManager
        rvCards.adapter = paymentAdapter
        rvCards.setHasFixedSize(true)

        paymentAdapter!!.onItemEditClick = { paymentId, paymentCard ->
            Log.e(TAG, "onItemEditClick: $paymentId ")
            val intent = (Intent(applicationContext, AddCreditCardActivity::class.java))
            intent.putExtra("edit", 1)
            intent.putExtra("paymentId", paymentId)
            intent.putExtra("card_number", paymentCard.card_number)
            intent.putExtra("expired_date", paymentCard.expired_date)
            intent.putExtra("validation_number", paymentCard.validation_number)
            intent.putExtra("name_cardholder", paymentCard.name_cardholder)
            intent.putExtra("cardId", paymentCard.id)
            intent.putExtra("method_id", paymentCard.method_id.toInt())
            startActivity(intent)
            //  finish()

        }

        paymentAdapter!!.onItemDeleteClick = { cardId,position ->
            Log.e(TAG, "onItemDeleteClick: $cardId ")
            viewModel.deleteCardPayment(cardId)
            methods.removeAt(position)
            paymentAdapter?.notifyDataSetChanged()
        }
    }

    private fun swipeToRefresh() {
        swipeRefresh?.setOnRefreshListener {
            viewModel.myPaymentCard()

        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.myPaymentCard()
        setupObserver()
        setupObserverDelete()
    }


}