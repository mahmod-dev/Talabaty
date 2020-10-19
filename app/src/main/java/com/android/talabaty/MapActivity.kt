package com.android.talabaty

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.android.talabaty.util.LocationHelper
import com.android.talabaty.util.MyPreferences
import com.android.talabaty.util.MyPreferences.prefSave
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.mahmoud.todoapp.util.LocationManager
import kotlinx.android.synthetic.main.activity_map.*
import java.util.*


class MapActivity : AppCompatActivity(), OnMapReadyCallback {
    val TAG = "MapActivity"
    private lateinit var locationHelper: LocationHelper
    private lateinit var mMap: GoogleMap
    private var lng: Double? = 0.0
    private var lat: Double? = 0.0
    private var address: String? = null
    var num = 0
    var type = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        MyPreferences.context = this
        type = intent.extras?.getInt("type")!!

        initGpsLocation()

        btnMapLocation.setOnClickListener {
            MyPreferences.setLong("lat", lat!!.toLong())
            MyPreferences.setLong("long", lng!!.toLong())
            MyPreferences.setInt("type", type)
           // getAddress(lat!!,lng!!).prefSave("addressName")
             finish()
            // Toast.makeText(this,"${getString(R.string.choosen_location)} \n lat: $lat, lng: $long",Toast.LENGTH_SHORT).show()
        }
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


        mMap.setOnMarkerDragListener(object : OnMarkerDragListener {
            override fun onMarkerDragStart(markerDragStart: Marker) {
                // TODO Auto-generated method stub
                Log.e(TAG, "onMarkerDragStart: ")
            }

            override fun onMarkerDragEnd(markerDragEnd: Marker) {
                Log.e(TAG, "onMarkerDragEnd: ")
                mMap.animateCamera(CameraUpdateFactory.newLatLng(markerDragEnd.position))
                lat = markerDragEnd.position.latitude
                lng = markerDragEnd.position.longitude
                getAddress(lat!!,lng!!).prefSave("addressName")

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
                lng = location?.longitude
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
                lng = location?.longitude
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
        this.lng = long
        val marker = mMap.addMarker(options)
        marker.title = "lat: ${lat.toFloat()}, lng: ${long.toFloat()}"
        marker.isDraggable = true
        //   mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))


    }

    override fun onBackPressed() {

        if (lat != null && lng != null) {
            Log.e(TAG, "onBackPressed: $lat , $lng")
            MyPreferences.setLong("lat", lat!!.toLong())
            MyPreferences.setLong("long", lng!!.toLong())
            MyPreferences.setInt("type", type)
         //   getAddress(lat!!,lng!!).prefSave("addressName")

        }

        super.onBackPressed()
    }

    private fun getAddress(lat: Double, lng: Double): String {
        val geoCoder = Geocoder(applicationContext, Locale.getDefault())
        var addresses: List<Address>? = null

        var address = ""
        try {
            addresses = geoCoder.getFromLocation(lat, lng, 1)
            address = addresses[0].getAddressLine(0)
            Log.e("addresses", address)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return address
    }


}