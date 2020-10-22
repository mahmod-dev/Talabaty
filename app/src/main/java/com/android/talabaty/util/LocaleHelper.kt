@file:Suppress("DEPRECATION")

package com.android.talabaty.util

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import com.android.talabaty.MainActivity
import com.yariksoffice.lingver.Lingver
import java.util.*

object LocaleHelper {

    public fun followSystemLocale(context: Context) {
        Lingver.getInstance().setFollowSystemLocale(context)
        restart(context)
    }

    private fun restart(context: Context) {
        val i = Intent(context, MainActivity::class.java)
        context.startActivity(i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK))
    }

    public fun setNewLocale(context: Context, language: String) {
        Lingver.getInstance().setLocale(context, language)
        restart(context)
    }

    public fun Configuration.getLocaleCompat(): Locale {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) locales.get(0) else locale
    }

    fun getCurrentLocal(): String {
        if (Locale.getDefault().language.equals("ar")) {
            return "ar"
        } else
            return "en"
    }
}