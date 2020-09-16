package com.android.talabaty

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.android.talabaty.adapter.ProductDetailsAdapter
import com.android.talabaty.adapter.RecycleCategoryAdapter
import com.android.talabaty.adapter.SliderAdapter
import com.android.talabaty.adapter.SliderProductAdapter
import com.android.talabaty.dbUtil.Status
import com.android.talabaty.dbUtil.ViewModelFactory
import com.android.talabaty.model.*
import com.android.talabaty.retrofit.ApiHelperImpl
import com.android.talabaty.retrofit.RetrofitBuilder
import com.android.talabaty.util.CustomMaterialDialog.getMaterialDialogInstance
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)
        arrImages = ArrayList()
        arrColors = ArrayList()
        arrSizes = ArrayList()
        val has_colors = intent.extras?.getInt("has_colors")
        val has_sizes = intent.extras?.getInt("has_sizes")
        val productId = intent.extras?.getInt("productId")
        Log.e(TAG, "productId: $productId" )
        initViewModel()
        viewModel.productDetails(productId!!)
        handleToolbar()
        setupObserver()
        handleCartNum()
        handleColorSize(has_colors,has_sizes)


    }

    private fun handleToolbar(){
        imgArrowBack.setOnClickListener {
            finish()

        }

        imgCart.setOnClickListener {
            startActivity(Intent(this,CartActivity::class.java))

        }

        imgCart.setOnClickListener {
            startActivity(Intent(this,CartActivity::class.java))

        }

        imgFav.setOnClickListener {
            startActivity(Intent(this,FavoriteActivity::class.java))

        }
    }


    private fun initViewModel() {

        viewModel = ViewModelProviders.of(this,
            ViewModelFactory(ApiHelperImpl(RetrofitBuilder.apiService), application)
        ).get(StoreDetailsViewModel::class.java)
    }


    private fun setupObserver() {

        viewModel.getProductDetails().observe(this,
            Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        swipeRefresh.isRefreshing= false

                        it.data?.let { users ->
                            for (i in users.products.indices) {
                                arrImages.addAll(users.products[i].other_images)
                                arrSizes.addAll(users.products[i].sizes)
                                arrColors.addAll(users.products[i].colors)

                            }
                            tvDetailsFamily.text = users.products[0].description
                            tvStoreBio.text = users.products[0].store.bio
                            tvStoreNameFamily.text = users.products[0].store.name
                            tvBuyCount.text = "(${users.products[0].purchase_counts})"
                            tvPriceOriginal.text = users.products[0].price.toString()
                            rbFamilyDetails.rating = users.products[0].store.rate.toFloat()
                            val img =  users.products[0].store.image_profile

                            if (img.isNotEmpty()) {
                                Glide.with(this).load(img)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .placeholder(R.drawable.ic_icon_loading)
                                    .error(R.drawable.white)
                                    .into(imgStore)
                            }

                            initViewPager()
                            initRecycleViewColors(arrColors)
                            initRecycleViewSizes(arrSizes)
                        }
                    }
                    Status.LOADING -> {
                        swipeRefresh.isRefreshing= true

                    }
                    Status.ERROR -> {
                        swipeRefresh.isRefreshing= false
                        getMaterialDialogInstance(it.message!!)

                        //Handle Error
                        Log.e(TAG, "setupObserver: " + it.message)

                    }
                }

            }
        )
    }

    private fun initRecycleViewColors(data:ArrayList<Color>) {

        val adapter = ProductDetailsAdapter(this,null,data,1)
        rvFamilyColor.layoutManager = LinearLayoutManager(this,RecyclerView.HORIZONTAL,false)
        rvFamilyColor.adapter = adapter
        rvFamilyColor.setHasFixedSize(true)

        adapter.onItemClick = { colorId ->
            Log.e(TAG, "initRecycleViewColors: $colorId " )

        }
    }

    private fun initRecycleViewSizes(data:ArrayList<Size>) {

        val adapter = ProductDetailsAdapter(this,data,null,0)
   val myLayoutManager =     LinearLayoutManager(this,RecyclerView.HORIZONTAL,false)
        rvFamilySize.layoutManager = myLayoutManager
        rvFamilySize.adapter = adapter
        rvFamilySize.setHasFixedSize(true)

        adapter.onItemClick = { sizeId ->
            Log.e(TAG, "initRecycleViewSizes: $sizeId " )

        }
    }


    private fun initViewPager(){

        adapter = SliderProductAdapter(this, arrImages)
        viewPagerFamily?.adapter = adapter
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


    private fun handleColorSize(has_colors: Int?, has_sizes: Int?){
        if (has_colors==0){
            tvColor.visibility = View.GONE
            rvFamilyColor.visibility =  View.GONE
        }
        if (has_sizes==0){
            tvSize.visibility = View.GONE
            rvFamilySize.visibility =  View.GONE
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

    private fun handleCartNum(){
        initViewModelCart()
        viewModelCart.getCart()
        setupObserverGetCart()
    }

    override fun onStart() {
        super.onStart()
        handleCartNum()
        Log.e(TAG, "onStart: " )

    }
}