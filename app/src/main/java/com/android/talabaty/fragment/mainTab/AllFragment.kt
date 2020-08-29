package com.android.talabaty.fragment.mainTab

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager.widget.ViewPager
import com.android.talabaty.*
import com.android.talabaty.adapter.MainCategoryAdapter
import com.android.talabaty.adapter.RecycleStoresAdapter
import com.android.talabaty.adapter.SliderAdapter
import com.android.talabaty.dbUtil.Status
import com.android.talabaty.dbUtil.ViewModelFactory
import com.android.talabaty.model.HomePageCategories
import com.android.talabaty.model.HomePageCategory
import com.android.talabaty.model.ViewStores
import com.android.talabaty.retrofit.ApiHelperImpl
import com.android.talabaty.retrofit.RetrofitBuilder
import com.android.talabaty.util.Helper
import com.android.talabaty.viewModel.AllMainViewModel
import com.android.talabaty.viewModel.StoresViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.fuzz.indicator.CutoutViewIndicator

class AllFragment : Fragment() {
    val TAG = "AllFragment"
    var adapter: SliderAdapter? = null
    var linFreeDelivery: LinearLayout? = null
    var linOrderService: LinearLayout? = null
    var linOrderCar: LinearLayout? = null
    var linRemoteServices: LinearLayout? = null
    var cardOfferServices: LinearLayout? = null
    var viewPager: ViewPager? = null
    var swipeRefresh: SwipeRefreshLayout? = null

    private lateinit var viewModelMain: AllMainViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_all, container, false)
        initViewModelMain()
        viewPager = root.findViewById(R.id.pager)
        linFreeDelivery = root.findViewById(R.id.linFreeDelivery)
        linOrderService = root.findViewById(R.id.linOrderService)
        linOrderCar = root.findViewById(R.id.linOrderCar)
        linRemoteServices = root.findViewById(R.id.linRemoteServices)
        cardOfferServices = root.findViewById(R.id.cardOfferServices)
        swipeRefresh = root.findViewById(R.id.swipeRefresh)
       val  rvCatHome = root.findViewById<RecyclerView>(R.id.rvCatHome)
        viewPager = root.findViewById(R.id.pager)
        adapter = SliderAdapter(activity!!, 3)
        viewPager?.setAdapter(adapter)
        val indicator: CutoutViewIndicator = root.findViewById(R.id.indicator_details)
        indicator.setViewPager(viewPager)
        viewModelMain.homePageCategories()

        setupObserverMainCat(rvCatHome)
        swipeToRefresh(rvCatHome)

        linFreeDelivery?.setOnClickListener { startActivity(Intent(activity, FreeDeliveryActivity::class.java)) }
        linOrderService?.setOnClickListener { startActivity(Intent(activity, DeliveryServicesActivity::class.java)) }
        linOrderCar?.setOnClickListener { startActivity(Intent(activity, CarServiceActivity::class.java)) }
        linRemoteServices?.setOnClickListener { startActivity(Intent(activity, RemoteServiceActivity::class.java)) }
        cardOfferServices?.setOnClickListener { startActivity(Intent(activity, NewestOffersServicesActivity::class.java)) }
        return root
    }




    private fun initViewModelMain() {

        viewModelMain = ViewModelProviders.of(
            activity!!,
            ViewModelFactory(ApiHelperImpl(RetrofitBuilder.apiService), activity!!.application)
        ).get(AllMainViewModel::class.java)
    }


    private fun setupObserverMainCat(rv: RecyclerView) {

        viewModelMain.getHomePageCategories().observe(viewLifecycleOwner,
            Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        swipeRefresh?.isRefreshing = false
                        it.data?.let { users ->
                            Log.e(TAG, "setupObserverMainCat: ${users.home_page_categories}")
                            initRecycleViewMainCat(rv, users)
                        }
                    }
                    Status.LOADING -> {
                        //progressBar.visibility = View.VISIBLE
                        swipeRefresh?.isRefreshing = true

                    }
                    Status.ERROR -> {
                        swipeRefresh?.isRefreshing = false
                        Helper.showFilterDialog(activity!!, it.message!!).show()
                        Log.e(TAG, "setupObserver: " + it.message)

                    }
                }

            }
        )
    }

    private fun initRecycleViewMainCat(rv: RecyclerView, data: HomePageCategories) {

        Log.e(TAG, "initRecycleViewMainCat: $data")
       val  adapterMainCat = MainCategoryAdapter(activity!!, data)
        rv.layoutManager = LinearLayoutManager(activity)
        rv.adapter = adapterMainCat
        rv.setHasFixedSize(true)

    }

    private fun swipeToRefresh(rv:RecyclerView){
        swipeRefresh?.setOnRefreshListener {

            setupObserverMainCat(rv)
        }

    }



}