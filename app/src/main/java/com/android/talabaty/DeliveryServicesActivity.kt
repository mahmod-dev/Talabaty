package com.android.talabaty

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_delivery_services.*
import kotlinx.android.synthetic.main.toolbar.*

class DeliveryServicesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delivery_services)
        imgArrowBack.setOnClickListener {
            finish()
        }

        linPayment1.setOnClickListener {
            startActivity(Intent(this,PaymentMethodActivity::class.java))
        }
    }
}