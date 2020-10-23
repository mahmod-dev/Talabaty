package com.android.talabaty

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.android.talabaty.dbUtil.Status
import com.android.talabaty.dbUtil.ViewModelFactory
import com.android.talabaty.model.UserPost
import com.android.talabaty.retrofit.ApiHelperImpl
import com.android.talabaty.retrofit.RetrofitBuilder
import com.android.talabaty.util.CustomMaterialDialog.getMaterialDialogInstance
import com.android.talabaty.util.Helper
import com.android.talabaty.util.Helper.setUrlImage
import com.android.talabaty.util.MyPreferences
import com.android.talabaty.viewModel.ProfileViewModel
import com.github.dhaval2404.imagepicker.ImagePicker
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.title_toolbar.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File


class EditProfileActivity : AppCompatActivity() {
    val TAG = "EditProfileActivity"
    private lateinit var viewModel: ProfileViewModel
    private var strImg: String? = null
    private var file: File? = null
    var filePart: MultipartBody.Part? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        MyPreferences.context = this
        handleToolbar()
        initViewModel()
        setupObserver()

        val userNameP = MyPreferences.getStr("userName")
        val emailP = MyPreferences.getStr("userEmail")
        val mobileP = MyPreferences.getStr("userMobile")
        val imageP = MyPreferences.getStr("userImage")
        val lat = MyPreferences.getLong("lat")
        val lng = MyPreferences.getLong("lng")

        etEmail.setText(emailP)
        etMobile.setText(mobileP)
        etUsername.setText(userNameP)
        img.setUrlImage(this, imageP)


        btnEdit.setOnClickListener {
            val email = etEmail.text.toString()
            val mobile = etMobile.text.toString()
            val password = etPassword.text.toString()
            val username = etUsername.text.toString()
            val deviceType = "android"

            if (validationInput(username, email, mobile, password)) {
                return@setOnClickListener
            } else {
                val fcm = "1wdasdasdasd"
                val user = UserPost(
                    username,
                    email,
                    mobile,
                    password,
                    fcm,
                    deviceType,
                    strImg,
                    lat,
                    lng
                )
                if (file!=null){
                     filePart = MultipartBody.Part.createFormData(
                        "image_profile",
                        file!!.name,
                        RequestBody.create(MediaType.parse("image/*"), file!!)
                    )
                }


                viewModel.editProfile(
                    username,
                    email,
                    mobile,
                    lat,
                    lng,
                    password,
                    deviceType,
                    "1wdasdasdasd",
                    filePart
                )

            }
        }

        img.setOnClickListener {
            Helper.selectImageDialog(this, true)
        }
    }

    private fun handleToolbar() {
        imgArrowBack.setOnClickListener {
            finish()

        }

        tvTitleToolbar.text = getString(R.string.edit_profile)

    }

    private fun initViewModel() {

        viewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiHelperImpl(RetrofitBuilder.apiService), application)
        ).get(ProfileViewModel::class.java)
    }

    private fun setupObserver() {

        viewModel.editProfile().observe(this,
            Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        progressBar.visibility = View.GONE

                        it.data?.let { users ->
                            Toast.makeText(this, users.message, Toast.LENGTH_SHORT).show()
                            finish()

                        }
                    }
                    Status.LOADING -> {
                        progressBar.visibility = View.VISIBLE

                    }
                    Status.ERROR -> {
                        progressBar.visibility = View.GONE

                        getMaterialDialogInstance(it.message!!)

                    }
                }

            }
        )
    }

    private fun validationInput(
        username: String,
        email: String,
        mobile: String,
        passwordNew: String
    ): Boolean {

        if (username.isEmpty()) {
            etUsername.error = getString(R.string.empty)
            return true
        }

        if (email.isEmpty()) {
            etEmail.error = getString(R.string.empty)
            return true
        }

        if (passwordNew.isEmpty()) {
            etPassword.error = getString(R.string.empty)
            return true
        }


        if (mobile.isEmpty()) {
            etMobile.error = getString(R.string.empty)
            return true
        }

        if (!Helper.emailValid(email)) {
            etEmail.error = getString(R.string.email_valid)
            return true
        }

        return false
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == ImagePicker.REQUEST_CODE) {
            //Image Uri will not be null for RESULT_OK
            val fileUri = data?.data
            img.setImageURI(fileUri)

            file = ImagePicker.getFile(data)!!
            val filePath: String = ImagePicker.getFilePath(data)!!
            //   strImg = Helper.encodeFile(file)
            val bitmap = BitmapFactory.decodeFile(filePath)

            //    strImg = Helper.encodeImage(bitmap)

        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show()
        }
    }


}