package com.android.talabaty

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class CoffeeActivity : AppCompatActivity() {
    var btn: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coffee)
        btn = findViewById(R.id.coffee_btn)
        btn.setOnClickListener(View.OnClickListener { startActivity(Intent(applicationContext, PhotographyActivity::class.java)) })
    }
}