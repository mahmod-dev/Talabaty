package com.android.talabaty

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.talabaty.util.Helper
import kotlinx.android.synthetic.main.activity_settings.*


class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        tvContactUs.setOnClickListener {
            Helper.callUs(this, "0555845631")
        }

        tvAbout.setOnClickListener {
            val intent = Intent(this, SettingDetailsActivity::class.java)
            intent.putExtra("title", getString(R.string.about_app))
            startActivity(intent)
        }

        tvPrivacyPolicy.setOnClickListener {
            val intent = Intent(this, SettingDetailsActivity::class.java)
            intent.putExtra("title", getString(R.string.usage_policy))
            startActivity(intent)
        }

        tvShareApp.setOnClickListener {
            Helper.shareAPK(this)
        }

        tvSuggestion.setOnClickListener {
            val intent = Intent(this, SuggestionsActivity::class.java)
            startActivity(intent)
        }

        tvLanguage.setOnClickListener {
            val intent = Intent(this, LanguageActivity::class.java)
            startActivity(intent)
        }

    }


}