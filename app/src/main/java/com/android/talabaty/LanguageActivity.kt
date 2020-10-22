package com.android.talabaty

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RadioButton
import com.android.talabaty.util.Helper
import com.android.talabaty.util.LocaleHelper
import kotlinx.android.synthetic.main.activity_language.*

class LanguageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_language)

        if (LocaleHelper.getCurrentLocal() == "ar") {
            rbArabic.isChecked = true
        } else {
            rbEnglish.isChecked = true

        }
        radioGroup.setOnCheckedChangeListener { group, i ->
            val selectedId = group.checkedRadioButtonId
            val selected = this.findViewById(selectedId) as RadioButton
            if (selected.text == getString(R.string.arabic)) {
                LocaleHelper.setNewLocale(this, "ar")

            } else if (selected.text == getString(R.string.english)) {
                LocaleHelper.setNewLocale(this, "en")

            }


        }
    }
}