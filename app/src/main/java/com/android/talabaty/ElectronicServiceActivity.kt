package com.android.talabaty

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class ElectronicServiceActivity : AppCompatActivity() {
    var btn: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_electronic_service)
        btn = findViewById(R.id.btn_electronic)
        btn.setOnClickListener(View.OnClickListener { startActivity(Intent(applicationContext, SearchLocationActivity::class.java)) })
    }
}