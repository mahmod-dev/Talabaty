package com.android.talabaty.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.android.talabaty.MainActivity
import com.android.talabaty.R

class VerificationActivity : AppCompatActivity() {
    var confirm: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verification)
        confirm = findViewById(R.id.confirm_btn)
        confirm?.setOnClickListener(View.OnClickListener { startActivity(Intent(applicationContext, MainActivity::class.java)) })
    }
}