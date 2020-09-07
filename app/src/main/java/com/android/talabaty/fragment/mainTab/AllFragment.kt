package com.android.talabaty.fragment.mainTab

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.android.talabaty.*
import com.android.talabaty.adapter.MainCategoryAdapter
import com.android.talabaty.adapter.NewestOffersAdapter
import com.android.talabaty.adapter.SliderAdapter
import com.android.talabaty.dbUtil.Status
import com.android.talabaty.dbUtil.ViewModelFactory
import com.android.talabaty.model.Ad
import com.android.talabaty.model.HomePageCategories
import com.android.talabaty.model.Offer
import com.android.talabaty.retrofit.ApiHelperImpl
import com.android.talabaty.retrofit.RetrofitBuilder
import com.android.talabaty.util.CustomMaterialDialog.getMaterialDialogInstance
import com.android.talabaty.viewModel.AllMainViewModel
import kotlinx.android.synthetic.main.fragment_all.*

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
    var rvNewestOffers: RecyclerView? = null
    private lateinit var data: ArrayList<Offer>
    private lateinit var arrImages: ArrayList<Ad>
    var idd =0

    private lateinit var viewModelMain: AllMainViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_all, container, false)
        initViewModelMain()
        linFreeDelivery = root.findViewById(R.id.linFreeDelivery)
        linOrderService = root.findViewById(R.id.linOrderService)
        linOrderCar = root.findViewById(R.id.linOrderCar)
        linRemoteServices = root.findViewById(R.id.linRemoteServices)
        cardOfferServices = root.findViewById(R.id.cardOfferServices)
        swipeRefresh = root.findViewById(R.id.swipeRefresh)
        rvNewestOffers = root.findViewById(R.id.rvNewestOffers)
        viewPager = root.findViewById(R.id.pager)

        val rvCatHome = root.findViewById<RecyclerView>(R.id.rvCatHome)
        data = ArrayList()
        arrImages = ArrayList()


        viewModelMain.homePageCategories()
        viewModelMain.allOffers()
        viewModelMain.allAdds()
        setupObserverMainCat(rvCatHome)
        swipeToRefresh(rvCatHome)
        setupObserver(rvNewestOffers!!)
        setupObserverAdds()



        linFreeDelivery?.setOnClickListener {
            startActivity(
                Intent(
                    activity,
                    FreeDeliveryActivity::class.java
                )
            )
        }
        linOrderService?.setOnClickListener {

               val intent =  Intent(activity, OrderServiceActivity::class.java)
            intent.putExtra("cat","")
            startActivity(intent)

        }
        linOrderCar?.setOnClickListener {
            startActivity(
                Intent(
                    activity,
                    CarServiceActivity::class.java
                )
            )
        }
        linRemoteServices?.setOnClickListener {
            startActivity(
                Intent(
                    activity,
                    RemoteServiceActivity::class.java
                )
            )
        }
        cardOfferServices?.setOnClickListener {
            startActivity(
                Intent(
                    activity,
                    NewestOffersServicesActivity::class.java
                )
            )
        }
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
                        //    Helper.showFilterDialog(activity!!, it.message!!).show()
                        activity?.getMaterialDialogInstance(it.message!!)

                        Log.e(TAG, "setupObserver: " + it.message)

                    }
                }

            }
        )
    }

    private fun setupObserverAdds() {

        viewModelMain.getAdds().observe(viewLifecycleOwner,
            Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        it.data?.let { users ->
                            arrImages.addAll(users.ads)
                            initViewPager()
                            idd =1

                        }

                    }
                    Status.LOADING -> {
                        arrImages.clear()
                    }
                    Status.ERROR -> {
                        arrImages.clear()

                        activity?.getMaterialDialogInstance(it.message!!)

                    }
                }

            }
        )
    }


    private fun initRecycleViewMainCat(rv: RecyclerView, data: HomePageCategories) {

        Log.e(TAG, "initRecycleViewMainCat: $data")
        val adapterMainCat = MainCategoryAdapter(activity!!, data)
        rv.layoutManager = LinearLayoutManager(activity)
        rv.adapter = adapterMainCat
        rv.setHasFixedSize(true)

    }

    private fun swipeToRefresh(rv: RecyclerView) {
        swipeRefresh?.setOnRefreshListener {

            setupObserverMainCat(rv)
        }

    }



    private fun setupObserver(rv: RecyclerView) {
        viewModelMain.getAllOffers().observe(this,
            Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        it.data?.let { users ->
                            data.clear()
                            data.add(users.offers[0])

                            initRecycleView(rv)
                        }
                    }
                    Status.LOADING -> {
                        data.clear()
                        initRecycleView(rv)

                    }
                    Status.ERROR -> {
                        activity!!.getMaterialDialogInstance(it.message!!)
                    }
                }
            }
        )
    }

    private fun initRecycleView(rv : RecyclerView) {
        val adapter = NewestOffersAdapter(activity!!, data)
        rv.layoutManager = LinearLayoutManager(activity!!)
        rv.adapter = adapter
        rv.setHasFixedSize(true)

    }

    private fun initViewPager(){

        adapter = SliderAdapter(activity!!, arrImages)
        adapter?.notifyDataSetChanged()
        viewPager?.adapter = adapter

        if (idd==0)
        indicator?.setViewPager(viewPager)

        viewPager?.addOnPageChangeListener(object : OnPageChangeListener {
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

}