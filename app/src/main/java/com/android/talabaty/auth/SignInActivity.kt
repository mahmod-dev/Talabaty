package com.android.talabaty.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.talabaty.R

class SignInActivity : AppCompatActivity() {
    var forget: TextView? = null
    var signIn: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        forget = findViewById(R.id.forget_password)
        signIn = findViewById(R.id.sign_in)
        forget?.setOnClickListener { startActivity(Intent(applicationContext, ResetPasswordActivity::class.java)) }
        signIn?.setOnClickListener { startActivity(Intent(applicationContext, VerificationActivity::class.java)) }
    }
}