package com.android.talabaty.fragment.mainTab


import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.talabaty.R
import com.android.talabaty.adapter.RecycleStoresAdapter
import com.android.talabaty.dbUtil.Status
import com.android.talabaty.dbUtil.ViewModelFactory
import com.android.talabaty.model.Store
import com.android.talabaty.retrofit.ApiHelperImpl
import com.android.talabaty.retrofit.RetrofitBuilder
import com.android.talabaty.util.CustomMaterialDialog
import com.android.talabaty.util.CustomMaterialDialog.getMaterialDialogInstance
import com.android.talabaty.util.Helper
import com.android.talabaty.util.LocationHelper
import com.android.talabaty.util.MyPreferences
import com.android.talabaty.viewModel.StoresViewModel
import com.mahmoud.todoapp.util.LocationManager

class OthersFragment(var position: Int) : Fragment() {
    val TAG = "OthersFragment"
    private lateinit var viewModel: StoresViewModel
    private lateinit var data: ArrayList<Store>
    var swipeRefresh: SwipeRefreshLayout? = null
    var tvNotFound: TextView? = null
    private lateinit var locationHelper: LocationHelper
    private var long: Double? = 0.0
    private var lat: Double? = 0.0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        initGpsLocation()
        initViewModel()
        MyPreferences.context = context
        val root = inflater.inflate(R.layout.fragment_others, container, false)
        val rvStore = root.findViewById<RecyclerView>(R.id.rvStore)
         swipeRefresh = root.findViewById(R.id.swipeRefresh)
        tvNotFound = root.findViewById(R.id.tvNotFound)
        data = ArrayList()
        setupObserver(rvStore)
        swipeToRefresh()

        return root
    }

    private fun setupObserver(rv: RecyclerView) {
        tvNotFound?.visibility = View.GONE
        viewModel.getStoresById().observe(
            viewLifecycleOwner,
            Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        swipeRefresh?.isRefreshing = false
                        tvNotFound?.visibility = View.GONE
                        it.data?.let { users ->
                            data.addAll(users.stores)
                            if (users.stores.isEmpty()){
                                tvNotFound?.visibility = View.VISIBLE
                            }else
                            initRecycleView(rv)
                        }
                    }
                    Status.LOADING -> {
                        tvNotFound?.visibility = View.GONE
                        data.clear()
                        swipeRefresh?.isRefreshing = true
                        initRecycleView(rv)

                    }
                    Status.ERROR -> {
                        tvNotFound?.visibility = View.GONE
                        swipeRefresh?.isRefreshing = false
                      //  Helper.showFilterDialog(activity!!, it.message!!).show()
                        activity?.getMaterialDialogInstance(it.message!!)

                        Log.e(TAG, "setupObserver: " + it.message)
                    }
                }
            }
        )
    }

    private fun initViewModel() {

        viewModel = ViewModelProviders.of(
            activity!!,
            ViewModelFactory(ApiHelperImpl(RetrofitBuilder.apiService), activity!!.application)
        ).get(StoresViewModel::class.java)
    }


    private fun initRecycleView(rv: RecyclerView) {
        if ( activity != null) {
            val adapter = RecycleStoresAdapter(activity!!, data,lat!!,long!!)
            rv.layoutManager = LinearLayoutManager(activity)
            rv.adapter = adapter
            rv.setHasFixedSize(true)
        }
    }

    private fun swipeToRefresh(){
        swipeRefresh?.setOnRefreshListener {

            viewModel.storesById(position)
        }

    }


    private fun initGpsLocation() {
        locationHelper = LocationHelper(activity!!, object : LocationManager {

            override fun onLocationChanged(location: Location?) {

                lat = location?.latitude
                long = location?.longitude
                Log.e(TAG, "onLocationChanged latitude: ${location?.latitude}")
                Log.e(TAG, "onLocationChanged longitude: ${location?.longitude}")
                MyPreferences.setLong("lat",lat!!.toLong())
                MyPreferences.setLong("lng",long!!.toLong())


            }

            override fun getLastKnownLocation(location: Location?) {

                Log.e(TAG, "getLastKnownLocation latitude: ${location?.latitude}")
                Log.e(TAG, "getLastKnownLocation longitude: ${location?.longitude}")
                lat = location?.latitude
                long = location?.longitude

                MyPreferences.setLong("lat",lat!!.toLong())
                MyPreferences.setLong("lng",long!!.toLong())

            }

        })

    }

    override fun onStop() {
        super.onStop()
        locationHelper.stopLocationUpdates()

    }

    override fun onResume() {
        super.onResume()

        if (locationHelper.checkLocationPermissions()) {
            if (locationHelper.checkMapServices()) {
                locationHelper.startLocationUpdates()

            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101) {
            initGpsLocation()
            viewModel.storesById(position)
        }
    }



}