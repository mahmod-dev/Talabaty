package com.android.talabaty

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_waiting.*

class WaitingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_waiting)

        btnTrackWaiting.setOnClickListener { startActivity(Intent(applicationContext, TrackingMapActivity::class.java)) }
        btnChatWaiting.setOnClickListener { startActivity(Intent(applicationContext, ChatActivity::class.java)) }
    }
}