package com.android.talabaty.util

import android.app.Activity
import android.widget.Button
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.android.talabaty.R

object CustomMaterialDialog   {
    var dialog: MaterialDialog? = null

     fun Activity.getMaterialDialogInstance(message: String) {

        if (dialog == null) {
            dialog = MaterialDialog(this)
                .noAutoDismiss()
                .customView(R.layout.dialog_message_error)

            // set initial preferences
            dialog?.findViewById<TextView>(R.id.tvErrorMessage)?.text = message

            dialog?.findViewById<Button>(R.id.btnConfirmDialogError)?.setOnClickListener {
                dialog?.dismiss()
            }

        }

      dialog?.show()

    }

 //   companion object : SingletonHolder<CustomMaterialDialog, Context>(::CustomMaterialDialog)

}