package com.android.talabaty

import android.R.attr.label
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_waiting.*
import kotlinx.android.synthetic.main.toolbar.*


class WaitingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_waiting)
        handleToolbar()

        btnTrackWaiting.setOnClickListener { startActivity(Intent(applicationContext, TrackingMapActivity::class.java)) }
        btnChatWaiting.setOnClickListener { startActivity(Intent(applicationContext, ChatActivity::class.java)) }

        tvCode.setOnLongClickListener {
            val clipboard: ClipboardManager =
                getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Copied Text", tvCode.text.toString())
            clipboard.setPrimaryClip(clip)
            Toast.makeText(this, getString(R.string.copied_text), Toast.LENGTH_LONG).show()

            false
        }

    }

    private fun handleToolbar(){
        imgArrowBack.setOnClickListener {
            finish()

        }

        imgCart.setOnClickListener {
            startActivity(Intent(this,CartActivity::class.java))

        }

        imgFav.setOnClickListener {
            startActivity(Intent(this,FavoriteActivity::class.java))

        }
    }
}