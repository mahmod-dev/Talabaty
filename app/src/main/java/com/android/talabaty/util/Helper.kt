package com.android.talabaty.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.Settings
import android.text.format.DateFormat
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.app.ActivityCompat
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.android.talabaty.R
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import java.net.InetAddress
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern


object Helper {

    private val VALID_EMAIL_ADDRESS_REGEX: Pattern =
        Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE)

    fun emailValid(emailStr: String?): Boolean {
        val matcher: Matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr)
        return matcher.find()
    }


    fun isValid(
        passwordhere: String,
        etPassword: EditText
    ): Boolean {
        val specialCharPatten =
            Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE)
        val upperCasePatten = Pattern.compile("[A-Z ]")
        val lowerCasePatten = Pattern.compile("[a-z ]")
        val digitCasePatten = Pattern.compile("[0-9 ]")
        var flag = true

        if (passwordhere.length < 8) {
            etPassword.error = "Password length must have at least 8 character !!"
            flag = false
        }
        if (!specialCharPatten.matcher(passwordhere).find()) {
            etPassword.error = "Password must have at least one special character !!"
            flag = false
        }
        if (!upperCasePatten.matcher(passwordhere).find()) {
            etPassword.error = "Password must have at least one uppercase character !!"
            flag = false
        }
        if (!lowerCasePatten.matcher(passwordhere).find()) {
            etPassword.error = "Password must have at least one lowercase character !!"
            flag = false
        }
        if (!digitCasePatten.matcher(passwordhere).find()) {
            etPassword.error = "Password must have at least one digit character !!"
            flag = false
        }
        return flag
    }

    fun getFormatDateTime(format: String = "yyyy-MM-dd hh:mm:ss a",date:Date = Date()) =
        DateFormat.format(format,date).toString()

    fun getFormatTime(format: String = "hh:mm a",time:Date = Date()) =
        DateFormat.format(format,time).toString()

    fun getFormatDate(format: String = "yyyy-MM-dd",date:Date = Date()) =
        DateFormat.format(format, date).toString()
    fun getFormatDate(format: String = "yyyy-MM-dd", date: Calendar) =
        DateFormat.format(format, date).toString()



    fun showFilterDialog(context: Context,message:String):MaterialDialog{

        val dialog = MaterialDialog(context)
            .noAutoDismiss()
            .customView(R.layout.dialog_message_error)

        // set initial preferences
        dialog.findViewById<TextView>(R.id.tvErrorMessage).text = message

        dialog.findViewById<Button>(R.id.btnConfirmDialogError).setOnClickListener {
            dialog.dismiss()
        }

       return dialog
    }

    fun isInternetAvailable(): Boolean {
        return try {
            val ipAddr: InetAddress = InetAddress.getByName("google.com")
            //You can replace it with your name
            !ipAddr.equals("")
        } catch (e: Exception) {
            false
        }
    }


}