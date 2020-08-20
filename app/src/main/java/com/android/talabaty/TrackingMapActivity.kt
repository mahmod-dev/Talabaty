package com.android.talabaty

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions

class TrackingMapActivity : AppCompatActivity() {
    var mMapView: MapView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tracking_map)
//        mMapView = findViewById(R.id.map_view)
//        mMapView.onCreate(savedInstanceState)
//        mMapView.onResume()
//        mMapView.getMapAsync(OnMapReadyCallback { mMap ->
//            mMapView.onResume()
//            val sydney = LatLng((-34).toDouble(), 151)
//            googleMap = mMap
//            googleMap!!.setMapStyle(MapStyleOptions.loadRawResourceStyle(applicationContext, R.raw.style_json))
//            googleMap!!.moveCamera(CameraUpdateFactory.newLatLng(sydney))
//        })
//    }
//
//    companion object {
//        private var googleMap: GoogleMap? = null
//    }

    }
}