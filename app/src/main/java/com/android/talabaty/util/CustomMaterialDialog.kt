package com.android.talabaty.util

import android.app.Activity
import android.content.ContextWrapper
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.android.talabaty.R

object CustomMaterialDialog : AppCompatActivity() {
    var dialog: MaterialDialog? = null

    fun Activity.getMaterialDialogInstance(message: String): MaterialDialog {

        if (dialog == null) {
            dialog = MaterialDialog(this)
                .customView(R.layout.dialog_message_error)
        }
        dialog!!.cancelable(false)
        dialog?.findViewById<TextView>(R.id.tvErrorMessage)?.text = message

        dialog?.findViewById<Button>(R.id.btnConfirmDialogError)?.setOnClickListener {
            dialog?.dismiss()
            dialog = null
        }


        dialog?.show()

        return dialog!!
    }

    fun dismiss() {
        dialog?.dismiss()
        dialog = null

    }



    //   companion object : SingletonHolder<CustomMaterialDialog, Context>(::CustomMaterialDialog)

}