package com.android.talabaty.fragment.mainTab

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.android.talabaty.*
import com.android.talabaty.adapter.*
import com.android.talabaty.dbUtil.Status
import com.android.talabaty.dbUtil.ViewModelFactory
import com.android.talabaty.model.*
import com.android.talabaty.retrofit.ApiHelperImpl
import com.android.talabaty.retrofit.RetrofitBuilder
import com.android.talabaty.util.CustomMaterialDialog.getMaterialDialogInstance
import com.android.talabaty.util.MyPreferences
import com.android.talabaty.viewModel.AllMainViewModel
import com.android.talabaty.viewModel.StoresViewModel
import kotlinx.android.synthetic.main.activity_nearby.*
import kotlinx.android.synthetic.main.fragment_all.*
import kotlinx.android.synthetic.main.fragment_all.rvNearby

class AllFragment : Fragment() {
    val TAG = "AllFragment"
    var adapter: SliderAdapter? = null
    var linFreeDelivery: LinearLayout? = null
    var linOrderService: LinearLayout? = null
    var linOrderCar: LinearLayout? = null
    var linRemoteServices: LinearLayout? = null
    var cardOfferServices: LinearLayout? = null
    var linProducts: LinearLayout? = null
    var linNearby: LinearLayout? = null
    var viewPager: ViewPager? = null
    var swipeRefresh: SwipeRefreshLayout? = null
    var rvNewestOffers: RecyclerView? = null
    var rvProducts: RecyclerView? = null
    var tvNotFound: TextView? = null
    private lateinit var data: ArrayList<Offer>
    private lateinit var arrImages: ArrayList<Ad>
    var idd = 0

    private lateinit var viewModelMain: AllMainViewModel
    private lateinit var viewModelNearby: StoresViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        MyPreferences.context = context

        val root = inflater.inflate(R.layout.fragment_all, container, false)
        initViewModelMain()
        initViewModelNearby()
        linFreeDelivery = root.findViewById(R.id.linFreeDelivery)
        linOrderService = root.findViewById(R.id.linOrderService)
        linOrderCar = root.findViewById(R.id.linOrderCar)
        linRemoteServices = root.findViewById(R.id.linRemoteServices)
        linNearby = root.findViewById(R.id.linNearby)
        linProducts = root.findViewById(R.id.linProducts)
        cardOfferServices = root.findViewById(R.id.cardOfferServices)
        swipeRefresh = root.findViewById(R.id.swipeRefresh)
        rvNewestOffers = root.findViewById(R.id.rvNewestOffers)
        rvProducts = root.findViewById(R.id.rvProducts)
        viewPager = root.findViewById(R.id.pager)
        tvNotFound = root.findViewById(R.id.tvNotFound)

        val rvCatHome = root.findViewById<RecyclerView>(R.id.rvCatHome)
        data = ArrayList()
        arrImages = ArrayList()


        viewModelMain.homePageCategories()
        viewModelMain.allOffers()
        viewModelMain.allAdds()
        setupObserverMainCat(rvCatHome)
        swipeToRefresh()
        setupObserver(rvNewestOffers!!)
        setupObserverAdds()
        nearbyLocation()
        setupObserverNearby()
        linProducts?.setOnClickListener {
            startActivity(
                Intent(
                    activity,
                    TatbeqActivity::class.java
                )
            )
        }

        linNearby?.setOnClickListener {
            startActivity(
                Intent(
                    activity,
                    NearbyActivity::class.java
                )
            )
        }

        linFreeDelivery?.setOnClickListener {
            startActivity(
                Intent(
                    activity,
                    FreeDeliveryActivity::class.java
                )
            )
        }
        linOrderService?.setOnClickListener {

            val intent = Intent(activity, OrderServiceActivity::class.java)
            intent.putExtra("cat", "")
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
                            initRecycleViewMainTatbeq(rvProducts!!, users.tatbeqakumProducts)
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
                            idd = 1
                        }
                    }
                    Status.LOADING -> {
                        arrImages.clear()
                        adapter?.notifyDataSetChanged()

                    }
                    Status.ERROR -> {
                        arrImages.clear()
                        adapter?.notifyDataSetChanged()


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

    private fun initRecycleViewMainTatbeq(rv: RecyclerView, data: ArrayList<TatbeqakumProduct>) {

        Log.e(TAG, "initRecycleViewMainCat: $data")
        val adapter = TatbeqakumProductsAdapter(activity!!, data)
        rv.layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
        rv.adapter = adapter
        rv.setHasFixedSize(true)

        adapter.onItemClick = {
            Log.e(TAG, "initRecycleViewMainTatbeq: $it")
            val intent = Intent(activity, ProductDetailsActivity::class.java)
            intent.putExtra("has_colors", it.has_colors)
            intent.putExtra("has_sizes", it.has_sizes)
            intent.putExtra("productId", it.id)
            startActivity(intent)

        }

    }


    private fun swipeToRefresh() {
        swipeRefresh?.setOnRefreshListener {
            val lat = MyPreferences.getLong("lat")
            val lng = MyPreferences.getLong("lng")
            if (lat != 0L && lng != 0L) {
                viewModelNearby.nearbyStores(lat, lng)
            } else {
                tvNotFound?.visibility = View.VISIBLE
            }
            viewModelMain.homePageCategories()
            viewModelMain.allOffers()
            viewModelMain.allAdds()
        }

    }


    private fun setupObserver(rv: RecyclerView) {
        viewModelMain.getAllOffers().observe(this,
            Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        it.data?.let { users ->
                            val rnds = (users.offers.indices).random()

                            data.add(users.offers[rnds])

                            initRecycleView(rv, data)
                        }
                    }
                    Status.LOADING -> {
                        data.clear()
                        initRecycleView(rv, data)

                    }
                    Status.ERROR -> {
                        activity!!.getMaterialDialogInstance(it.message!!)
                    }
                }
            }
        )
    }

    private fun initRecycleView(rv: RecyclerView, data: ArrayList<Offer>) {
        val adapter = NewestOffersAdapter(activity!!, data)
        rv.layoutManager = LinearLayoutManager(activity!!)
        rv.adapter = adapter
        rv.setHasFixedSize(true)

    }

    private fun initViewPager() {

        adapter = SliderAdapter(activity!!, arrImages)
        viewPager?.adapter = adapter
        if (idd == 0)
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


    private fun initViewModelNearby() {

        viewModelNearby = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiHelperImpl(RetrofitBuilder.apiService), activity!!.application)
        ).get(StoresViewModel::class.java)
    }

    private fun setupObserverNearby() {

        viewModelNearby.getNearbyStores().observe(this,
            Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        swipeRefresh?.isRefreshing = false
                        it.data?.let { users ->
                            if (users.stores.isEmpty()) {
                                tvNotFound?.visibility = View.VISIBLE
                            } else {
                                val rnds = (0 until users.stores.size).random()
                                initRecycleViewNearby(users.stores[rnds])
                            }

                        }
                    }
                    Status.LOADING -> {
                        tvNotFound?.visibility = View.GONE

                    }
                    Status.ERROR -> {
                        tvNotFound?.visibility = View.GONE
                        activity!!.getMaterialDialogInstance(it.message!!)
                        Log.e(TAG, "setupObserver: " + it.message)

                    }
                }

            }
        )
    }


    private fun initRecycleViewNearby(store: Store) {
        val adapter = NearbyAdapter(activity!!, null, store)
        val linearLayoutManager = LinearLayoutManager(activity!!)
        rvNearby.layoutManager = linearLayoutManager
        rvNearby.adapter = adapter
        rvNearby.setHasFixedSize(true)

        adapter.onItemClick = { position ->

        }
    }

    private fun nearbyLocation() {
        val lat = MyPreferences.getLong("lat")
        val lng = MyPreferences.getLong("lng")
        initViewModelNearby()
        if (lat != 0L && lng != 0L) {
            viewModelNearby.nearbyStores(lat, lng)
        } else {
            tvNotFound?.visibility = View.VISIBLE
        }
    }

}