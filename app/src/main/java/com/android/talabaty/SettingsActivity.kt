package com.android.talabaty

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {
    var question: TextView? = null
    var suggestion: TextView? = null
    var contactUs: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
//        question = findViewById(R.id.tv_question)
//        suggestion = findViewById(R.id.tv_suggestion)
//        contactUs = findViewById(R.id.tv_contact)
//        question.setOnClickListener(View.OnClickListener { startActivity(Intent(applicationContext, WelcomeActivity::class.java)) })
//        suggestion.setOnClickListener(View.OnClickListener { startActivity(Intent(applicationContext, DeliveryActivity::class.java)) })
//        contactUs.setOnClickListener(View.OnClickListener { startActivity(Intent(applicationContext, HelpActivity::class.java)) })
    }
}