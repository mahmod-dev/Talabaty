package com.android.talabaty

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_suggestions.*

class SuggestionsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_suggestions)

        btnSend.setOnClickListener {
            val details = etDetails.text.toString()
            if (details.isEmpty()) {
                etDetails.error = getString(R.string.empty)
                return@setOnClickListener
            }

            btnSend.text = getString(R.string.been_sent)
            btnSend.isEnabled = false
            etDetails.setText("")
        }


    }
}