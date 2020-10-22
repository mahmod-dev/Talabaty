package com.android.talabaty


import android.app.Application
import com.yariksoffice.lingver.Lingver
import com.yariksoffice.lingver.store.PreferenceLocaleStore
import java.util.*


class App : Application() {

    @Suppress("UNUSED_VARIABLE")
    override fun onCreate() {
        super.onCreate()
        val store = PreferenceLocaleStore(this, Locale("ar"))
        // you can use this instance for DI or get it via Lingver.getInstance() later on
        val lingver = Lingver.init(this, store)
    }


}