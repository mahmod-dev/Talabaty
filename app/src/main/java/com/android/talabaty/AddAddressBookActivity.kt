package com.android.talabaty

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.android.talabaty.dbUtil.Status
import com.android.talabaty.dbUtil.ViewModelFactory
import com.android.talabaty.retrofit.ApiHelperImpl
import com.android.talabaty.retrofit.RetrofitBuilder
import com.android.talabaty.util.CustomMaterialDialog.getMaterialDialogInstance
import com.android.talabaty.util.LocationHelper
import com.android.talabaty.util.MyPreferences
import com.android.talabaty.viewModel.AddressBookViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.mahmoud.todoapp.util.LocationManager
import kotlinx.android.synthetic.main.activity_add_adress_book.*

class AddAddressBookActivity : AppCompatActivity(), OnMapReadyCallback {
    val TAG = "AddAddressBookActivity"
    private lateinit var viewModel: AddressBookViewModel

    private lateinit var locationHelper: LocationHelper
    private lateinit var mMap: GoogleMap
    private var long: Double? = 0.0
    private var lat: Double? = 0.0
    var num = 0
    var addressId: Int? = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_adress_book)

        val type = intent.extras?.getInt("type")
        if (type == 1) {
            val addressS = intent.extras?.getString("address")
            addressId = intent.extras?.getInt("id")
            etAddress.setText(addressS)
            btnMapLocation.text = getString(R.string.edit_address)

        }

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        MyPreferences.context = this
        initViewModel()
        initGpsLocation()

        btnMapLocation.setOnClickListener {
            val address = etAddress.text.toString()
            if (address.isEmpty()) {
                etAddress.error = getString(R.string.empty)
                return@setOnClickListener
            }

            if (lat == 0.0 && long == 0.0) {
                Toast.makeText(this, getString(R.string.select_address_on_map), Toast.LENGTH_LONG)
                    .show()

            } else {
                if (type == 0) {
                    viewModel.addAddress(lat!!, long!!, address)

                } else {

                    if (addressId != 0) {
                        viewModel.editAddress(addressId!!, lat!!, long!!, address)

                    }

                }

            }
        }

        setupObserver()
        setupObserverEdit()
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        mMap.isMyLocationEnabled = true


        mMap.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener {
            override fun onMarkerDragStart(markerDragStart: Marker) {
                // TODO Auto-generated method stub
                Log.e(TAG, "onMarkerDragStart: ")
            }

            override fun onMarkerDragEnd(markerDragEnd: Marker) {
                Log.e(TAG, "onMarkerDragEnd: ")
                mMap.animateCamera(CameraUpdateFactory.newLatLng(markerDragEnd.position))
                lat = markerDragEnd.position.latitude
                long = markerDragEnd.position.longitude
            }

            override fun onMarkerDrag(markerDrag: Marker) {
                Log.e(TAG, "onMarkerDrag: ")
            }
        })
    }


    override fun onResume() {
        super.onResume()

        if (locationHelper.checkLocationPermissions()) {
            if (locationHelper.checkMapServices()) {
                locationHelper.startLocationUpdates()

            }
        }
    }

    override fun onStop() {
        super.onStop()
        locationHelper.stopLocationUpdates()

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101) {
            initGpsLocation()
        }
    }


    private fun initGpsLocation() {
        locationHelper = LocationHelper(this, object : LocationManager {

            override fun onLocationChanged(location: Location?) {

                lat = location?.latitude
                long = location?.longitude
                Log.e(TAG, "onLocationChanged latitude: ${location?.latitude}")
                Log.e(TAG, "onLocationChanged longitude: ${location?.longitude}")

                if (num == 0 && location != null) {
                    addMarker(location.latitude, location.longitude)
                    num = 1

                }

            }

            override fun getLastKnownLocation(location: Location?) {

                Log.e(TAG, "getLastKnownLocation latitude: ${location?.latitude}")
                Log.e(TAG, "getLastKnownLocation longitude: ${location?.longitude}")
                lat = location?.latitude
                long = location?.longitude
                if (location != null) {
                    addMarker(location.latitude, location.longitude)

                }


            }

        })

    }


    private fun addMarker(lat: Double, long: Double) {
        mMap.clear()
        val cameraPosition = CameraPosition.Builder()
            .target(LatLng(lat, long))
            .zoom(17f)
            .build()
        val options = MarkerOptions().position(
            LatLng(lat, long)
        ).icon(
            BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
        ).draggable(true)
        this.lat = lat
        this.long = long
        val marker = mMap.addMarker(options)
        marker.title = "lat: ${lat.toFloat()}, lng: ${long.toFloat()}"
        marker.isDraggable = true
        //   mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

    }

    private fun initViewModel() {

        viewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiHelperImpl(RetrofitBuilder.apiService), application)
        ).get(AddressBookViewModel::class.java)
    }

    private fun setupObserver() {
        viewModel.getAddAddress().observe(this,
            Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        //   progressBar.visibility = View.GONE
                        it.data?.let { users ->
                            Toast.makeText(this, users.message, Toast.LENGTH_LONG).show()
                            finish()

                        }
                    }
                    Status.LOADING -> {
//                        progressBar.visibility = View.VISIBLE

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

    private fun setupObserverEdit() {
        viewModel.getEditAddress().observe(this,
            Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        //   progressBar.visibility = View.GONE
                        it.data?.let { users ->
                            Toast.makeText(this, users.message, Toast.LENGTH_SHORT).show()
                            finish()

                        }
                    }
                    Status.LOADING -> {
//                        progressBar.visibility = View.VISIBLE

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


}

