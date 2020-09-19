package com.android.talabaty.util

import android.app.Activity
import android.content.Context
import android.database.Cursor
import android.graphics.Color
import android.net.Uri
import android.provider.OpenableColumns
import android.text.format.DateFormat
import android.util.Base64
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.android.talabaty.R
import com.android.talabaty.model.Cart
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import de.hdodenhof.circleimageview.CircleImageView
import java.io.File
import java.net.InetAddress
import java.net.URISyntaxException
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern


object Helper {
    var onItemClick: (() -> Unit)? = null

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

    fun getFormatDateTime(format: String = "yyyy-MM-dd hh:mm:ss a", date: Date = Date()) =
        DateFormat.format(format, date).toString()

    fun getFormatTime(format: String = "hh:mm a", time: Date = Date()) =
        DateFormat.format(format, time).toString()

    fun getFormatDate(format: String = "yyyy-MM-dd", date: Date = Date()) =
        DateFormat.format(format, date).toString()

    fun getFormatDate(format: String = "yyyy-MM-dd", date: Calendar) =
        DateFormat.format(format, date).toString()


//    fun showFilterDialog(context: Context, message: String): MaterialDialog {
//
//        val dialog = MaterialDialog(context)
//            .noAutoDismiss()
//            .customView(R.layout.dialog_message_error)
//        dialog.findViewById<TextView>(R.id.tvErrorMessage).text = message
//
//        dialog.findViewById<Button>(R.id.btnConfirmDialogError).setOnClickListener {
//            dialog.dismiss()
//        }
//
//        return dialog
//    }

    fun Activity.showLogoutDialog(): MaterialDialog {

        val dialog = MaterialDialog(this)
            .noAutoDismiss()
            .cancelable(false)
            .customView(R.layout.dialog_logout)


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


    fun selectImageDialog(activity: Activity) {
        val options =
            arrayOf<CharSequence>(
                activity.resources.getString(R.string.take_photo),
                activity.getString(R.string.choose_gallery)
            )
        val builder = MaterialAlertDialogBuilder(activity, R.style.AlertDialogCustom)

        val title = TextView(activity)
        title.text = activity.getString(R.string.choose_pic)
        title.setPadding(30, 30, 30, 30)
        title.textSize = 18f
        title.typeface = ResourcesCompat.getFont(activity, R.font.cairo_bold)

        title.setBackgroundColor(ContextCompat.getColor(activity, R.color.colorPrimary))
        title.setTextColor(Color.WHITE)

        builder.setCustomTitle(title)
        builder.setItems(options) { dialog, item ->
            if (options[item] == activity.resources.getString(R.string.take_photo)) {
                ImagePicker.with(activity)
                    .cameraOnly()
                    .crop()
                    .start()
            } else if (options[item] == activity.getString(R.string.choose_gallery)) {

                ImagePicker.with(activity)
                    .galleryOnly()
                    .crop()
                    .start()
            }
        }

        builder.setNegativeButton(activity.resources.getString(R.string.cancel)) { dialog, which ->
            dialog.dismiss()

        }
        builder.show()
    }


    fun dialogConfirm(activity: Activity, message: String) {

        val builder = MaterialDialog(activity)

            .customView(R.layout.dialog_confirm)

        // set initial preferences
        builder.findViewById<TextView>(R.id.tvDelete).text = message

        builder.findViewById<Button>(R.id.btnYes).setOnClickListener {
            onItemClick?.invoke()
            builder.dismiss()

        }

        builder.findViewById<Button>(R.id.btnNo).setOnClickListener {
            builder.dismiss()

        }
        builder.cancelable(false)
        builder.show()
    }


    @Throws(URISyntaxException::class)
    fun getPath(context: Context, uri: Uri): String? {
        if ("content".equals(uri.getScheme(), ignoreCase = true)) {
            val projection = arrayOf("_data")
            var cursor: Cursor? = null
            try {
                cursor = context.contentResolver.query(uri, projection, null, null, null)
                if (cursor != null) {
                    val column_index: Int = cursor.getColumnIndexOrThrow("_data")

                    if (cursor.moveToFirst()) {
                        return cursor.getString(column_index)
                    }
                }

            } catch (e: java.lang.Exception) {
                // Eat it
            }
        } else if ("file".equals(uri.getScheme(), ignoreCase = true)) {
            return uri.getPath()
        }
        return null
    }

    fun getFileName(context: Context, uri: Uri): String? {
        var result: String? = null
        if (uri.scheme == "content") {
            context.contentResolver.query(uri, null, null, null, null).use { cursor ->
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                }
            }
        }
        if (result == null) {
            result = uri.path
            var cut = 0
            if (result != null) {
                cut = result!!.lastIndexOf('/')
            }
            if (cut != -1) {
                if (result != null) {
                    result = result!!.substring(cut + 1)
                }
            }
        }
        return result
    }


    fun encodeFile(myFile: File): String? {
        val bytes = ByteArray(myFile.length().toInt())
        return Base64.encodeToString(bytes, 0)
    }

    fun ImageView.setUrlImage(context: Context,imgUrl :String?) {
        Glide.with(context).load(imgUrl)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(R.drawable.ic_icon_loading)
            .error(R.drawable.white)
            .into(this)

    }

   public fun CircleImageView.setUrlImage(context: Context,imgUrl :String?) {
        Glide.with(context).load(imgUrl)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(R.drawable.ic_icon_loading)
            .error(R.drawable.white)
            .into(this)

    }

}