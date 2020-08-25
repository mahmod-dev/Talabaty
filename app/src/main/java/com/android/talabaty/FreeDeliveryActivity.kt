package com.android.talabaty

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_free_delivery.*

class FreeDeliveryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_free_delivery)

        imgArrowBack.setOnClickListener {
            Log.e("TAG", "onCreate: " )
            finish()
        }
    }
}