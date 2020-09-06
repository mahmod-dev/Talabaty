package com.android.talabaty

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.android.talabaty.util.LocationHelper
import com.android.talabaty.util.MyPreferences
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.mahmoud.todoapp.util.LocationManager
import kotlinx.android.synthetic.main.activity_map.*

class MapActivity : AppCompatActivity(), OnMapReadyCallback {
    val TAG = "MapActivity"
    private lateinit var locationHelper: LocationHelper
    private lateinit var mMap: GoogleMap
    private var long: Double? = 0.0
    private var lat: Double? = 0.0
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
            MyPreferences.setLong("long", long!!.toLong())
            MyPreferences.setInt("type", type)
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

        mMap.setOnMapLongClickListener {
            addMarker(it.latitude, it.longitude)
        }
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
        )
        this.lat = lat
        this.long = long
        mMap.addMarker(options).title = "lat: ${lat.toFloat()}, lng: ${long.toFloat()}"
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

    }

    override fun onBackPressed() {

        if (lat != null && long != null) {
            Log.e(TAG, "onBackPressed: $lat , $long" )
            MyPreferences.setLong("lat", lat!!.toLong())
            MyPreferences.setLong("long", long!!.toLong())
            MyPreferences.setInt("type", type)
        }

        super.onBackPressed()
    }


}