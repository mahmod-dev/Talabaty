package com.android.talabaty

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.viewpager.widget.ViewPager
import com.android.talabaty.adapter.LikeProductsAdapter
import com.android.talabaty.adapter.ProductDetailsAdapter
import com.android.talabaty.adapter.SliderProductAdapter
import com.android.talabaty.dbUtil.Status
import com.android.talabaty.dbUtil.ViewModelFactory
import com.android.talabaty.model.Color
import com.android.talabaty.model.OtherImage
import com.android.talabaty.model.Product
import com.android.talabaty.model.Size
import com.android.talabaty.retrofit.ApiHelperImpl
import com.android.talabaty.retrofit.RetrofitBuilder
import com.android.talabaty.util.CustomMaterialDialog.dismiss
import com.android.talabaty.util.CustomMaterialDialog.getMaterialDialogInstance
import com.android.talabaty.util.MyPreferences
import com.android.talabaty.viewModel.CartViewModel
import com.android.talabaty.viewModel.StoreDetailsViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.activity_product_details.*
import kotlinx.android.synthetic.main.toolbar.*


class ProductDetailsActivity : AppCompatActivity() {
    val TAG = "ProductDetailsActivity"
    private lateinit var viewModel: StoreDetailsViewModel
    private lateinit var viewModelCart: CartViewModel

    var adapter: SliderProductAdapter? = null
    private lateinit var arrImages: ArrayList<OtherImage>
    private lateinit var arrColors: ArrayList<Color>
    private lateinit var arrSizes: ArrayList<Size>
    private var productId: Int? = 0
    private var colorId: Int? = 0
    private var sizeId: Int? = 0
    private var has_colors: Int? = 0
    private var has_sizes: Int? = 0
    var idd = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)
        MyPreferences.context = this
        MyPreferences.setInt("countQ", 1)
        tvQuantity.text = MyPreferences.getInt("countQ").toString()
        arrImages = ArrayList()
        arrColors = ArrayList()
        arrSizes = ArrayList()
        has_colors = intent.extras?.getInt("has_colors")
        has_sizes = intent.extras?.getInt("has_sizes")
        productId = intent.extras?.getInt("productId")
        Log.e(TAG, "productId: $productId")
        initViewModel()
        viewModel.productDetails(productId!!)
        handleToolbar()
        setupObserver()
        swipeToRefresh()
        handleCartNum()
        handleColorSize(has_colors, has_sizes)
        subQuantity()
        addQuantity()
        addToCart()
        setupObserverChangeQuantity()
        setupObserverAddToCart()
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
        ).get(StoreDetailsViewModel::class.java)
    }


    private fun setupObserver() {

        viewModel.getProductDetails().observe(this,
            Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        swipeRefresh.isRefreshing = false

                        it.data?.let { users ->
                            for (i in users.products.indices) {
                                arrImages.addAll(users.products[i].other_images)
                                if (!users.products[i].sizes.isNullOrEmpty()) {
                                    arrSizes.addAll(users.products[i].sizes!!)

                                }
                                if (!users.products[i].colors.isNullOrEmpty()) {
                                    arrColors.addAll(users.products[i].colors!!)

                                }
                            }

                            initRecycleViewLiked(users.random_products)

                            tvDescription.text = users.products[0].description
                            tvStoreBio.text = users.products[0].store.bio
                            tvStoreNameFamily.text = users.products[0].store.name
                            tvBuyCount.text = "(${users.products[0].purchase_counts})"
                            rbFamilyDetails.rating = users.products[0].store.rate.toFloat()
                            val img = users.products[0].store.image_profile

                            if (img.isNotEmpty()) {
                                Glide.with(this).load(img)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .placeholder(R.drawable.ic_icon_loading)
                                    .error(R.drawable.white)
                                    .into(imgStore)
                            }

                            if (users.products[0].offer_price.isNullOrEmpty()) {
                                tvPriceOffer.text =
                                    "${users.products[0].price} ${getString(R.string.rs)}"
                                tvPriceOriginal.visibility = View.GONE
                            } else {
                                tvPriceOffer.text =
                                    "${users.products[0].offer_price} ${getString(R.string.rs)}"
                                tvPriceOriginal.text =
                                    "${users.products[0].price} ${getString(R.string.rs)}"
                                tvPriceOriginal.paintFlags =
                                    tvPriceOriginal.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

                            }

                            initViewPager()
                            idd = 1
                            initRecycleViewColors(arrColors)
                            initRecycleViewSizes(arrSizes)
                        }
                    }
                    Status.LOADING -> {
                        arrColors.clear()
                        arrSizes.clear()
                        swipeRefresh.isRefreshing = true

                    }
                    Status.ERROR -> {
                        swipeRefresh.isRefreshing = false
                        getMaterialDialogInstance(it.message!!)

                        //Handle Error
                        Log.e(TAG, "setupObserver: " + it.message)

                    }
                }

            }
        )
    }

    private fun initRecycleViewColors(data: ArrayList<Color>) {

        val adapter = ProductDetailsAdapter(this, null, data, 1)
        rvFamilyColor.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        rvFamilyColor.adapter = adapter
        rvFamilyColor.setHasFixedSize(true)

        adapter.onItemClick = { colorId ->
            Log.e(TAG, "initRecycleViewColors: $colorId ")
            this.colorId = colorId

        }
    }

    private fun initRecycleViewSizes(data: ArrayList<Size>) {

        val adapter = ProductDetailsAdapter(this, data, null, 0)
        val myLayoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        rvFamilySize.layoutManager = myLayoutManager
        rvFamilySize.adapter = adapter
        rvFamilySize.setHasFixedSize(true)

        adapter.onItemClick = { sizeId ->
            Log.e(TAG, "initRecycleViewSizes: $sizeId ")
            this.sizeId = sizeId
        }
    }

    private fun initViewPager() {

        adapter = SliderProductAdapter(this, arrImages)
        viewPagerFamily?.adapter = adapter
        if (idd == 0)
            indicatorFamily?.setViewPager(viewPagerFamily)

        viewPagerFamily?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, v: Float, i1: Int) {}
            override fun onPageSelected(position: Int) {}
            override fun onPageScrollStateChanged(state: Int) {
                enableDisableSwipeRefresh(state == ViewPager.SCROLL_STATE_IDLE)
            }
        })
    }

    private fun enableDisableSwipeRefresh(enable: Boolean) {
        if (swipeRefresh != null) {
            swipeRefresh?.isEnabled = enable
        }
    }


    private fun handleColorSize(has_colors: Int?, has_sizes: Int?) {
        if (has_colors == 0) {
            tvColor.visibility = View.GONE
            rvFamilyColor.visibility = View.GONE
        }
        if (has_sizes == 0) {
            tvSize.visibility = View.GONE
            rvFamilySize.visibility = View.GONE
        }
    }

    private fun initViewModelCart() {

        viewModelCart = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiHelperImpl(RetrofitBuilder.apiService), application)
        ).get(CartViewModel::class.java)
    }

    private fun setupObserverGetCart() {

        viewModelCart.getAllCart().observe(this,
            Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        it.data?.let { users ->
                            if (users.cart.isEmpty()) {
                                tvCartNum.visibility = View.GONE

                            } else {
                                tvCartNum.visibility = View.VISIBLE
                                tvCartNum.text = users.cart.size.toString()

                            }
                        }
                    }
                    Status.LOADING -> {

                    }
                    Status.ERROR -> {

                        getMaterialDialogInstance(it.message!!)
                        Log.e(TAG, "setupObserver: " + it.message)

                    }
                }

            }
        )
    }

    private fun handleCartNum() {
        initViewModelCart()
        viewModelCart.getCart()
        setupObserverGetCart()
    }

    override fun onStart() {
        super.onStart()
        handleCartNum()
        Log.e(TAG, "onStart: ")

    }

    private fun swipeToRefresh() {
        swipeRefresh.setOnRefreshListener {

            viewModel.productDetails(productId!!)
        }

    }

    private fun subQuantity() {
        imgSubtract.setOnClickListener {

            if (MyPreferences.getInt("countQ") <= 1) {
                Toast.makeText(this, getString(R.string.minimum_order), Toast.LENGTH_SHORT).show()
                MyPreferences.setInt("countQ", 1)
                tvQuantity.text = MyPreferences.getInt("countQ").toString()

            } else {
                MyPreferences.setInt("count", MyPreferences.getInt("countQ") - 1)
                tvQuantity.text = MyPreferences.getInt("countQ").toString()
                viewModelCart.changeQuantity(productId!!, "decrease")
            }

        }

    }

    private fun addQuantity() {
        imgAdd.setOnClickListener {
            MyPreferences.setInt("countQ", MyPreferences.getInt("countQ") + 1)

            viewModelCart.changeQuantity(productId!!, "increase")
            tvQuantity.text = MyPreferences.getInt("countQ").toString()
        }
    }

    private fun addToCart() {
        btnAddToCart.setOnClickListener {
            if (colorId == 0 && has_colors == 1) {
                Toast.makeText(this, getString(R.string.choose_color), Toast.LENGTH_SHORT).show()

            } else if (sizeId == 0 && has_sizes == 1) {
                Toast.makeText(this, getString(R.string.choose_size), Toast.LENGTH_SHORT).show()

            } else
                viewModelCart.addToCart(
                    productId!!,
                    MyPreferences.getInt("countQ"),
                    colorId!!,
                    sizeId!!
                )

        }
    }

    private fun setupObserverChangeQuantity() {

        viewModelCart.getChangeQuantity().observe(this,
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
                        MyPreferences.setInt("countQ", 1)
                        tvQuantity.text = MyPreferences.getInt("countQ").toString()


                        getMaterialDialogInstance(it.message!!)
                    }
                }

            }
        )
    }

    private fun setupObserverAddToCart() {

        viewModelCart.getAddToCart().observe(this,
            Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        it.data?.let { users ->
                            Toast.makeText(this, users.message, Toast.LENGTH_LONG).show()

                            viewModelCart.getCart()
                        }
                    }
                    Status.LOADING -> {

                    }
                    Status.ERROR -> {
                        Log.e(TAG, "setupObserverAddToCart: ${it.message} ")

                        getMaterialDialogInstance(it.message!!)
                    }
                }

            }
        )
    }

    override fun onPause() {
        super.onPause()
        dismiss()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        dismiss()
    }

    private fun initRecycleViewLiked(data: ArrayList<Product>) {
        val adapter = LikeProductsAdapter(this, data)
        val layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
     //   layoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS

        rvLiked.layoutManager = layoutManager
        rvLiked.setHasFixedSize(true)
        rvLiked.itemAnimator = DefaultItemAnimator()
        rvLiked.adapter = adapter

        adapter.onItemClick = { product ->



        }
    }


}