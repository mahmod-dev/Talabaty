package com.android.talabaty

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager2.widget.ViewPager2
import com.android.talabaty.adapter.ViewPagerCategoriesAdapter
import com.android.talabaty.dbUtil.ViewModelFactory
import com.android.talabaty.model.Categories
import com.android.talabaty.retrofit.ApiHelperImpl
import com.android.talabaty.retrofit.RetrofitBuilder
import com.android.talabaty.util.Helper
import com.android.talabaty.viewModel.CategoriesViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.tabs.TabLayoutMediator
import com.android.talabaty.dbUtil.Status
import kotlinx.android.synthetic.main.activity_store_details.*
import kotlinx.android.synthetic.main.item_restaurant_under.*
import kotlinx.android.synthetic.main.toolbar.*

class StoreDetailsActivity : AppCompatActivity() {
    val TAG = "StoreDetailsActivity"
    private lateinit var viewModelCategory: CategoriesViewModel
    var catId = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store_details)
        initViewModelCategory()
        //val catId = intent.extras?.getInt("categoryId")
        val storeId = intent.extras?.getInt("storeId")
        deserializeObject(intent)
        if (storeId != null) {
            viewModelCategory.storeCategories(storeId)
        }

        imgArrowBack.setOnClickListener {
            finish()
        }

        imgFav.setOnClickListener {
            startActivity(Intent(this,FavoriteActivity::class.java))
        }
        setupObserverCat()


        viewPagerStoreDetails?.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                Log.e(TAG, "onPageSelected: $position " )
                catId = position+1
                Log.e(TAG, "catId: $catId " )
                if (storeId != null) {
                    Log.e(TAG, "onPageSelected: storeId: $storeId categoryId: $catId" )
                    viewModelCategory.viewStoreProduct(storeId,catId)
                }
            }
        })



        handleToolbar()
    }

    private fun handleToolbar(){
        imgArrowBack.setOnClickListener {
            finish()

        }

        imgCart.setOnClickListener {
            startActivity(Intent(this,CartActivity::class.java))

        }


        imgFav.setOnClickListener {
            startActivity(Intent(this,FavoriteActivity::class.java))

        }
    }

   private fun initViewModelCategory() {

        viewModelCategory = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiHelperImpl(RetrofitBuilder.apiService), application)
        ).get(CategoriesViewModel::class.java)
    }



    private fun setupObserverCat() {

        viewModelCategory.getAllCategory().observe(this,
            Observer {
                when (it.status) {
                    Status.SUCCESS -> {

                        it.data?.let { users ->
                            for (i in users.categories.indices) {
                                Log.e(TAG, "setupObserverCat: ${users.categories[i]}")
                            }
                            viewPager2Init(users)
                        }
                    }
                    Status.LOADING -> {

                    }
                    Status.ERROR -> {
                        //Handle Error
                        Helper.showFilterDialog(this,it.message!!).show()
                        Log.e(TAG, "setupObserverCat: " + it.message)
                    }
                }

            }
        )
    }


    private fun viewPager2Init(categories: Categories) {
        viewPagerStoreDetails?.adapter = ViewPagerCategoriesAdapter(this, categories.categories,swipeRefresh)

        TabLayoutMediator(tabsStoreDetails!!, viewPagerStoreDetails!!,
            TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                tab.text = categories.categories[position].name

            }).attach()
    }

    private fun deserializeObject(intent: Intent){
       val  name = intent.extras?.getString("name")
       val  rate = intent.extras?.getInt("rate")
       val  time_from = intent.extras?.getString("time_from")
       val  time_to = intent.extras?.getString("time_to")
       val  image_profile = intent.extras?.getString("image_profile")
       val  discount_percent = intent.extras?.getInt("discount_percent")
       val  open = intent.extras?.getInt("open")
        tvDiscountStore.visibility = View.GONE

        tvTitleStore.text = name
        if (rate != null) {
            rateBarStore.numStars = rate
        }
        tvTimeStore.text = "${time_from} - ${time_to}"

        if (discount_percent != 0) {
            tvDiscountStore.visibility = View.VISIBLE
            tvDiscountStore.text =
                "${discount_percent}% ${this.resources.getString(R.string.discount)} "

        }

        if (open == 1) {
            tvIsOpenStore.text = resources.getString(R.string.opened)
            tvIsOpenStore.backgroundTintList =
                (AppCompatResources.getColorStateList(this, R.color.green_light))
            tvIsOpenStore.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.colorPrimary
                )
            )
        } else {
            tvIsOpenStore.text = resources.getString(R.string.closed)
            tvIsOpenStore.backgroundTintList =
                (AppCompatResources.getColorStateList(this, R.color.red_light))
            tvIsOpenStore.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.colorRed2
                )
            )
        }


        if (image_profile?.isNotEmpty()!!) {
            Glide.with(this).load(image_profile)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_icon_loading)
                .error(R.drawable.white)
                .into(imgStore);
        }

    }


}