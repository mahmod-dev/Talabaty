package com.android.talabaty

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class SuccessActivity : AppCompatActivity() {
    var btn: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_success)
        btn = findViewById(R.id.my_orders)
        btn.setOnClickListener(View.OnClickListener { startActivity(Intent(applicationContext, FreeDeliveryActivity::class.java)) })
    }
}