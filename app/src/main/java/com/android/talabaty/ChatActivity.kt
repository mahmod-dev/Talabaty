package com.android.talabaty

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.talabaty.R
import com.android.talabaty.util.Helper

class ChatActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        Helper.showFilterDialog(this,"message").show()
    }
}