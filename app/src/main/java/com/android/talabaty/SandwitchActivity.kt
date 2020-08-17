package com.android.talabaty

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class SandwitchActivity : AppCompatActivity() {
    var view: View? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_details)
        view = findViewById(R.id.second_view)
        view?.setOnClickListener { startActivity(Intent(applicationContext, BagActivity::class.java)) }
    }
}