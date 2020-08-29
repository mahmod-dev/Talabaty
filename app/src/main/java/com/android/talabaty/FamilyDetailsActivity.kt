package com.android.talabaty

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.toolbar.*

class FamilyDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_family_details)

        handleToolbar()

    }

    private fun handleToolbar(){
        imgArrowBack.setOnClickListener {
            finish()

        }

        imgCart.setOnClickListener {
            startActivity(Intent(this,CartActivity::class.java))

        }

        imgCart.setOnClickListener {
            startActivity(Intent(this,CartActivity::class.java))

        }

        imgFav.setOnClickListener {
            startActivity(Intent(this,FavoriteActivity::class.java))

        }
    }

}