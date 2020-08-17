package com.hzdawoud.tatbeqakum.auth

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.talabaty.R
import com.android.talabaty.auth.SignInActivity

class SignUpActivity : AppCompatActivity() {
    var login: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        login = findViewById(R.id.login_btn)
        login?.setOnClickListener { startActivity(Intent(applicationContext, SignInActivity::class.java)) }
    }
}