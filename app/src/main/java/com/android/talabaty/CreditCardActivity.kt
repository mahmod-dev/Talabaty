package com.android.talabaty

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_credit_card.*

class CreditCardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_credit_card)
        btnConfirm.setOnClickListener { startActivity(Intent(applicationContext, WaitingActivity::class.java)) }
    }
}