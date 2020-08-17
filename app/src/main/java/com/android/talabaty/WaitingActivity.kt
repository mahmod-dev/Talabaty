package com.android.talabaty

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class WaitingActivity : AppCompatActivity() {
    var btn1: TextView? = null
    var btn2: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_waiting)
        btn1 = findViewById(R.id.mandoob)
        btn2 = findViewById(R.id.chat)
        btn1.setOnClickListener(View.OnClickListener { startActivity(Intent(applicationContext, TrackingMapActivity::class.java)) })
        btn2.setOnClickListener(View.OnClickListener { startActivity(Intent(applicationContext, ChatActivity::class.java)) })
    }
}