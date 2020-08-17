package com.android.talabaty


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class CartActivity : AppCompatActivity() {
    var btn: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        btn = findViewById(R.id.btnCompleteOrder)
        btn?.setOnClickListener { startActivity(Intent(applicationContext, CreditCardActivity::class.java)) }
    }
}