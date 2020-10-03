package com.android.talabaty

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.talabaty.adapter.OrdersWaitAdapter
import com.android.talabaty.dbUtil.Status
import com.android.talabaty.dbUtil.ViewModelFactory
import com.android.talabaty.model.OrderProduct
import com.android.talabaty.retrofit.ApiHelperImpl
import com.android.talabaty.retrofit.RetrofitBuilder
import com.android.talabaty.util.CustomMaterialDialog.getMaterialDialogInstance
import com.android.talabaty.util.MyPreferences
import com.android.talabaty.viewModel.OrdersViewModel
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.activity_waiting.*
import kotlinx.android.synthetic.main.activity_waiting.swipeRefresh
import kotlinx.android.synthetic.main.activity_waiting.tvDeliveryFee
import kotlinx.android.synthetic.main.toolbar.*


class WaitingActivity : AppCompatActivity() {
    private lateinit var viewModel: OrdersViewModel
    var orderId : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_waiting)
        MyPreferences.context = this
          orderId = intent.extras!!.getInt("orderId")
        Log.e("TAG", ":$orderId ")

        handleToolbar()
        initViewModel()
        viewModel.clientOrderDetails(orderId)
        setupObserver()
        swipeToRefresh()

        btnTrackWaiting.setOnClickListener {
            startActivity(
                Intent(
                    applicationContext,
                    TrackingMapActivity::class.java
                )
            )
        }
        btnChatWaiting.setOnClickListener {
            startActivity(
                Intent(
                    applicationContext,
                    ChatActivity::class.java
                )
            )
        }
        tvCode.setOnLongClickListener {
            copyCode()

            false
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
    }

    private fun initViewModel() {

        viewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiHelperImpl(RetrofitBuilder.apiService), application)
        ).get(OrdersViewModel::class.java)
    }

    private fun setupObserver() {
        viewModel.getClientOrdersDetails().observe(this,
            Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        swipeRefresh?.isRefreshing = false

                        // progressBar?.visibility = View.GONE
                        it.data?.let { users ->
                            tvStoreName.text = users.order_details.store.name
                            tvAddress.text = users.order_details.user_address.address
                            tvPayment.text = users.order_details.payment_method
                            tvOrderNum.text =
                                "#${users.order_details.id} ${getString(R.string.order)}"
                            tvTotalPrice.text =
                                "${users.order_details.sub_total} ${getString(R.string.rs)}"
                            tvDeliveryFee.text =
                                "${users.order_details.delivery_cost} ${getString(R.string.rs)}"
                            tvDiscount.text =
                                "${users.order_details.coupon_amount} ${getString(R.string.rs)}"
                            tvFinalTotal.text =
                                "${users.order_details.final_total} ${getString(R.string.rs)}"

                            handleRequestStatus(users.order_details.status)
                            initRecycleView(users.order_details.order_products)

                        }
                    }
                    Status.LOADING -> {
                        swipeRefresh?.isRefreshing = true
                        //  progressBar?.visibility = View.VISIBLE

                    }
                    Status.ERROR -> {
                        swipeRefresh?.isRefreshing = false

                        // progressBar?.visibility = View.GONE
                        getMaterialDialogInstance(it.message!!)

                    }
                }

            }
        )
    }

    private fun copyCode() {
        val clipboard: ClipboardManager =
            getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Copied Text", tvCode.text.toString())
        clipboard.setPrimaryClip(clip)
        Toast.makeText(this, getString(R.string.copied_text), Toast.LENGTH_LONG).show()

    }

    private fun initRecycleView(orders: ArrayList<OrderProduct>) {
        val adapter = OrdersWaitAdapter(this, orders)
        val linearLayoutManager = LinearLayoutManager(this)
        rvOrders?.layoutManager = linearLayoutManager
        rvOrders?.adapter = adapter
        rvOrders?.setHasFixedSize(true)

    }

    private fun handleRequestStatus(status: Int) {

        when (status) {
            0 -> {
                tvTitle1.text = getString(R.string.send_ur_order)
                img1.setImageResource(R.drawable.ic_waiting_send_selected)

                tvTitle2.text = getString(R.string.prepare_ur_order)
                img2.setImageResource(R.drawable.ic_waiting_preparing)

                tvTitle3.text = getString(R.string.deliver_ur_order)
                img3.setImageResource(R.drawable.ic_waiting_delivery)

            }
            1 -> {
                tvTitle1.text = getString(R.string.send_ur_order)
                img1.setImageResource(R.drawable.ic_waiting_send)

                tvTitle2.text = getString(R.string.prepare_ur_order)
                img2.setImageResource(R.drawable.ic_waiting_preparing_selected)

                tvTitle3.text = getString(R.string.deliver_ur_order)
                img3.setImageResource(R.drawable.ic_waiting_delivery)

            }
            2 -> {
                tvTitle1.text = getString(R.string.send_ur_order)
                img1.setImageResource(R.drawable.ic_waiting_send)

                tvTitle2.text = getString(R.string.prepare_ur_order)
                img2.setImageResource(R.drawable.ic_waiting_preparing)

                tvTitle3.text = getString(R.string.deliver_ur_order)
                img3.setImageResource(R.drawable.ic_waiting_delivery_selected)
            }
            3->{
                tvTitle1.text = getString(R.string.go_ur_order)
                img1.setImageResource(R.drawable.ic_waiting_send_selected)

                tvTitle2.text = getString(R.string.deliverd_ur_order)
                img2.setImageResource(R.drawable.ic_waiting_preparing)

                tvTitle3.text = getString(R.string.receive_ur_order)
                img3.setImageResource(R.drawable.ic_waiting_delivery)
            }

            4->{
                tvTitle1.text = getString(R.string.go_ur_order)
                img1.setImageResource(R.drawable.ic_waiting_send)

                tvTitle2.text = getString(R.string.deliverd_ur_order)
                img2.setImageResource(R.drawable.ic_waiting_preparing_selected)

                tvTitle3.text = getString(R.string.receive_ur_order)
                img3.setImageResource(R.drawable.ic_waiting_delivery)
            }
            5->{
                tvTitle1.text = getString(R.string.go_ur_order)
                img1.setImageResource(R.drawable.ic_waiting_send)

                tvTitle2.text = getString(R.string.deliverd_ur_order)
                img2.setImageResource(R.drawable.ic_waiting_preparing)

                tvTitle3.text = getString(R.string.receive_ur_order)
                img3.setImageResource(R.drawable.ic_waiting_delivery_selected)
            }
        }


    }
    private fun swipeToRefresh() {
        swipeRefresh?.setOnRefreshListener {
            viewModel.clientOrderDetails(orderId)
        }

    }



}