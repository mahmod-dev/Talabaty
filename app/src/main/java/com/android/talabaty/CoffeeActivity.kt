package com.android.talabaty

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_coffee.*
import kotlinx.android.synthetic.main.toolbar.*

class CoffeeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coffee)
        btnCoffee.setOnClickListener { startActivity(Intent(applicationContext, SuccessActivity::class.java)) }

        imgFav.setOnClickListener {
            startActivity(Intent(this,FavoriteActivity::class.java))
        }
    }
}