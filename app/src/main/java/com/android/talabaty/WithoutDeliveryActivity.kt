package com.android.talabaty

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_without_delivery.*

class WithoutDeliveryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_without_delivery)
        btnSendOrder.setOnClickListener { startActivity(Intent(applicationContext, WaitingActivity::class.java)) }
    }
}