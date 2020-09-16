package com.android.talabaty

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.talabaty.R
import com.android.talabaty.util.Helper
import kotlinx.android.synthetic.main.activity_chat.*

class ChatActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        imgArrowBack.setOnClickListener {
            finish()
        }

    }
}