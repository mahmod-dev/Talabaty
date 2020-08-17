package com.android.talabaty

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class OrderDetails : AppCompatActivity() {
    var confirm: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_details)
        confirm = findViewById(R.id.btn_map)
        confirm.setOnClickListener(View.OnClickListener { startActivity(Intent(applicationContext, MapActivity::class.java)) })
    }
}