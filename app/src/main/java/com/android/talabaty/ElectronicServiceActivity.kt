package com.android.talabaty

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.toolbar.*

class ElectronicServiceActivity : AppCompatActivity() {
    var btn: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_electronic_service)

        imgFav.setOnClickListener {
            startActivity(Intent(this,FavoriteActivity::class.java))
        }
    }
}