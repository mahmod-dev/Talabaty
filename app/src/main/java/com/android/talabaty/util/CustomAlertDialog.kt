package com.android.talabaty.util

import android.app.Activity
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.android.talabaty.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

object CustomAlertDialog {
    public var dialogTitle: String? =null


    public fun Activity.getDialogInstance(
    ): AlertDialog {
        if (dialogTitle.isNullOrEmpty()){
            dialogTitle = getString(R.string.please_wait)
        }
        val llPadding = 30
        val ll = LinearLayout(this)
        ll.orientation = LinearLayout.VERTICAL
        ll.setPadding(llPadding, llPadding, llPadding, llPadding)
        ll.gravity = Gravity.CENTER
        var llParam = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        llParam.gravity = Gravity.CENTER
        ll.layoutParams = llParam
        val progressBar = ProgressBar(this)
        progressBar.isIndeterminate = true
        progressBar.setPadding(0, 0, llPadding, 0)
        progressBar.layoutParams = llParam
        llParam = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        llParam.gravity = Gravity.CENTER
        val tvText = TextView(this)
        tvText.text = dialogTitle
        tvText.setTextColor(ContextCompat.getColor(this, R.color.colorBlack))
        tvText.textSize = 15f
        tvText.layoutParams = llParam
        ll.addView(progressBar)
        ll.addView(tvText)
        val builder = MaterialAlertDialogBuilder(this, R.style.AlertDialogCustom)
        builder.setCancelable(true)
        builder.setView(ll)
        val dialog = builder.create()
        val window: Window? = dialog.window
        if (window != null) {
            val layoutParams = WindowManager.LayoutParams()
            layoutParams.copyFrom(dialog.window!!.attributes)
            layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT
            layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
            dialog.window!!.attributes = layoutParams

        }

        return dialog
    }



  //  companion object : SingletonHolder<CustomAlertDialog, Activity>(::CustomAlertDialog)

}