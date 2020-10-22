package com.android.talabaty

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.talabaty.util.Helper
import kotlinx.android.synthetic.main.activity_setting_details.*

class SettingDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_details)

        val title = intent.extras?.getString("title")

        tvTitle.text = title

        if (title!! == getString(R.string.about_app)) {
            tvDetails.text = getString(R.string.about_tateqakum)

        } else if (title == getString(R.string.usage_policy)) {
            tvDetails.text = Helper.readTextFromAssets(this)

        }


    }
}