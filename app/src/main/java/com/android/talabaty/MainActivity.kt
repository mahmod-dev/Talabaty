package com.android.talabaty

import android.os.Bundle
import android.util.Log
import com.android.talabaty.util.MyPreferences


class MainActivity : BaseActivity() {
    val TAG =  "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MyPreferences.context = applicationContext

        Log.e(TAG, "onCreate:${ MyPreferences.getStr("userToken")} :: ${MyPreferences.getStr("userMobile")} " )
    }

}