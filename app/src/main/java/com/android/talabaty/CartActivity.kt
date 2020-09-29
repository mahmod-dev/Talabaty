package com.android.talabaty


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.talabaty.adapter.CartAdapter
import com.android.talabaty.dbUtil.Status
import com.android.talabaty.dbUtil.ViewModelFactory
import com.android.talabaty.model.Cart
import com.android.talabaty.retrofit.ApiHelperImpl
import com.android.talabaty.retrofit.RetrofitBuilder
import com.android.talabaty.util.CustomMaterialDialog.getMaterialDialogInstance
import com.android.talabaty.util.Helper
import com.android.talabaty.util.MyPreferences
import com.android.talabaty.viewModel.CartViewModel
import kotlinx.android.synthetic.main.activity_cart.*


class CartActivity : AppCompatActivity() {
    val TAG = "CartActivity"
    private lateinit var viewModel: CartViewModel
    var adapter: CartAdapter? = null
    var coupon: String? = null
    var address: String? = null
    var paymentMethod: String? = null
    var deliveryMethod: Int = 0
    var addressId: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        MyPreferences.context = this

        initViewModel()
        viewModel.getCart()
        btnCompleteOrder?.setOnClickListener {
            address = tvAddressDelivery.text.toString()

            if (address.isNullOrEmpty() && deliveryMethod == 0) {
                tvAddressDelivery.error = getString(R.string.empty)
                return@setOnClickListener
            }

            addressId = MyPreferences.getInt("addressId")

            if (paymentMethod.isNullOrEmpty()) {
                Toast.makeText(this, getString(R.string.choose_payment_method), Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            viewModel.checkout(
                coupon = coupon,
                deliveryMethod = deliveryMethod,
                userAddressId = addressId,
                paymentMethod = paymentMethod
            )

        }

        imgArrowBackCart.setOnClickListener {
            finish()
        }

        tvBookAddress.setOnClickListener {
            MyPreferences.setBool("isCart", true)
            startActivity(Intent(applicationContext, AllAddressBookActivity::class.java))
        }


        tvVerifyCoupon.setOnClickListener {
            coupon = etAddCoupon.text.toString()
            if (coupon.isNullOrEmpty()) {
                etAddCoupon.error = getString(R.string.empty)
                return@setOnClickListener
            }

            viewModel.getCart(coupon = coupon, deliveryMethod = deliveryMethod)
        }

        radioGroup.setOnCheckedChangeListener { group, i ->
            val selectedId = group.checkedRadioButtonId
            val selected = this.findViewById(selectedId) as RadioButton
            if (selected.text == getString(R.string.deliver)) {
                deliveryMethod = 0
            } else if (selected.text == getString(R.string.receive)) {
                deliveryMethod = 1
            }

            Log.e(TAG, "deliveryMethod: $deliveryMethod " )
            viewModel.getCart(coupon = coupon, deliveryMethod = deliveryMethod)

        }

        setupObserverGetCart()
        setupObserverRemoveFromCart()
        setupObserverCheckout()
        swipeToRefresh()
        handlePaymentType()
    }


    private fun initRecycleView(carts: ArrayList<Cart>) {
        adapter = CartAdapter(this, carts)
        val linearLayoutManager = LinearLayoutManager(this)
        rvCartDetails.layoutManager = linearLayoutManager
        rvCartDetails.adapter = adapter
        rvCartDetails.setHasFixedSize(true)

        adapter!!.onItemClick = { cart, position ->
            Helper.dialogConfirm(this, getString(R.string.delete_question_cart))
            Helper.onItemClick = {
                viewModel.deleteFromCart(cart.product!!.id)
                carts.removeAt(position)
                adapter?.notifyDataSetChanged()
            }
        }
    }


    private fun initViewModel() {

        viewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiHelperImpl(RetrofitBuilder.apiService), application)
        ).get(CartViewModel::class.java)
    }

    @SuppressLint("SetTextI18n")
    private fun setupObserverGetCart() {

        viewModel.getAllCart().observe(this,
            Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        swipeRefresh?.isRefreshing = false
                        it.data?.let { users ->
                            if (users.cart.isEmpty()) {
                                tvNotFound.visibility = View.VISIBLE
                            } else {
                                initRecycleView(users.cart)

                                tvDiscountCart.text =
                                    " ${users.coupon_amount} ${getString(R.string.rs)}"
                                tvTotalAmount.text =
                                    " ${users.final_total} ${getString(R.string.rs)}"
                                tvDeliveryFee.text =
                                    " ${users.delivery_cost} ${getString(R.string.rs)}"
                                tvPurchases.text = " ${users.sub_total} ${getString(R.string.rs)}"
                                tvAddedTax.text = "${users.vat_amount} ${getString(R.string.rs)}"

                            }
                        }



                    }
                    Status.LOADING -> {
                        swipeRefresh?.isRefreshing = true
                        tvNotFound.visibility = View.GONE

                    }
                    Status.ERROR -> {
                        tvNotFound.visibility = View.GONE
                        swipeRefresh?.isRefreshing = false
                        getMaterialDialogInstance(it.message!!)
                        Log.e(TAG, "setupObserver: " + it.message)

                    }
                }

            }
        )
    }

    private fun setupObserverCheckout() {

        viewModel.getCheckout().observe(this,
            Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        swipeRefresh?.isRefreshing = false
                        it.data?.let { users ->
                            startActivity(
                                Intent(
                                    applicationContext,
                                    PaymentMethodActivity::class.java
                                )
                            )
                        }

                    }
                    Status.LOADING -> {
                        swipeRefresh?.isRefreshing = true
                        tvNotFound.visibility = View.GONE

                    }
                    Status.ERROR -> {
                        tvNotFound.visibility = View.GONE
                        swipeRefresh?.isRefreshing = false
                        getMaterialDialogInstance(it.message!!)
                        Log.e(TAG, "setupObserver: " + it.message)

                    }
                }

            }
        )
    }


    private fun swipeToRefresh() {
        swipeRefresh?.setOnRefreshListener {

            viewModel.getCart(coupon = coupon, deliveryMethod = deliveryMethod)
        }

    }

    private fun setupObserverRemoveFromCart() {

        viewModel.getDeleteToCart().observe(this,
            Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        // viewModel.getCart()
                        // progressBar.visibility = View.GONE
                        it.data?.let { users ->
                            Toast.makeText(this, users.message, Toast.LENGTH_SHORT).show()
                            viewModel.getCart()


                        }
                    }
                    Status.LOADING -> {
                        //progressBar.visibility = View.VISIBLE

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

    override fun onStart() {
        super.onStart()
        if (MyPreferences.getBool("isBook")) {
            tvAddressDelivery.text = MyPreferences.getStr("myAddress")
            MyPreferences.setBool("isBook", false)
        }
    }

    private fun handlePaymentType() {
        card.setOnClickListener {
            card.setBackgroundColor(ContextCompat.getColor(this, R.color.green_light))
            cash.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite))
            wallet.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite))
            paymentMethod = "card"

        }

        cash.setOnClickListener {
            cash.setBackgroundColor(ContextCompat.getColor(this, R.color.green_light))
            card.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite))
            wallet.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite))
            paymentMethod = "cash"

        }

        wallet.setOnClickListener {
            wallet.setBackgroundColor(ContextCompat.getColor(this, R.color.green_light))
            cash.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite))
            card.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite))
            paymentMethod = "wallet"
        }

    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val v: View? = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    v.clearFocus()
                    val imm: InputMethodManager =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0)
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }


}