package com.android.talabaty.util

import android.content.Context

object MyPreferences {
    var context: Context? = null
    const val PREFS_NAME = "prefs"

    fun setInt(key: String?, value: Int) {
        val sharedPref = context!!.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
        val editor = sharedPref.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    fun getInt(key: String?): Int {
        val prefs = context!!.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
        return prefs.getInt(key, 0)
    }

    fun setStr(key: String?, value: String?) {
        val sharedPref = context!!.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
        val editor = sharedPref.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getStr(key: String?): String? {
        val prefs = context!!.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
        return prefs.getString(key, null)
    }

    fun getStr(key: String?, defValue: String?): String? {
        val prefs = context!!.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
        return prefs.getString(key, defValue)
    }

    fun setBool(key: String?, value: Boolean) {
        val sharedPref = context!!.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
        val editor = sharedPref.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun getBool(key: String?): Boolean {
        val prefs = context!!.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
        return prefs.getBoolean(key, false)
    }

    fun setLong(key: String?, value: Long) {
        val sharedPref = context!!.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
        val editor = sharedPref.edit()
        editor.putLong(key, value)
        editor.apply()
    }

    fun getLong(key: String?): Long {
        val prefs = context!!.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
        return prefs.getLong(key, 0)
    }
}