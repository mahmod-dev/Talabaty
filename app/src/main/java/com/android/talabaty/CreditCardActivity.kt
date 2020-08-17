package com.android.talabaty

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class CreditCardActivity : AppCompatActivity() {
    var btn: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_credit_card)
        btn = findViewById(R.id.credit_submit)
        btn.setOnClickListener(View.OnClickListener { startActivity(Intent(applicationContext, WaitingActivity::class.java)) })
    }
}