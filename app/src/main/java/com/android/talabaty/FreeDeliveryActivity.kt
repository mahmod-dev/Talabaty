package com.android.talabaty

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class FreeDeliveryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_free_delivery)
        val view = findViewById<View>(R.id.here)
        view.setOnClickListener { startActivity(Intent(applicationContext, CoffeeActivity::class.java)) }
    }
}