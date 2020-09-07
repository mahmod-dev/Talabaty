package com.android.talabaty

import android.app.Activity
import android.app.DatePickerDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.android.talabaty.dbUtil.Status
import com.android.talabaty.dbUtil.ViewModelFactory
import com.android.talabaty.model.DigitalServiceBody
import com.android.talabaty.model.SpinnerObj
import com.android.talabaty.retrofit.ApiHelperImpl
import com.android.talabaty.retrofit.RetrofitBuilder
import com.android.talabaty.util.Helper
import com.android.talabaty.util.FilePath
import com.android.talabaty.viewModel.DigitalServiceViewModel
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_electronic_service.*
import kotlinx.android.synthetic.main.title_toolbar.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ElectronicServiceActivity : AppCompatActivity() {
    val TAG = "ElectrServiceActivity"
    var prop: String? = null
    private var calendarDate: Calendar? = null
    private var startDate: Date? = null
    private var endDate: Date? = null
    private var strStartDate: String? = null
    private var strEndDate: String? = null
    private var strImg: String? = null
    private var strFile: String? = null
    private lateinit var viewModel: DigitalServiceViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_electronic_service)
        handleToolbar()

        calendarDate = Calendar.getInstance()
        initViewModel()

        val type = intent.extras?.getInt("type")
        tvPriority.setOnClickListener {
            selectPropDialog(this)
        }

        tvStartDate.setOnClickListener {
            setDateStart()
        }
        tvEndDate.setOnClickListener {
            setDateEnd()
        }

        cardAddImg.setOnClickListener {
            Helper.selectImageDialog(this)
        }

        cardAddFile.setOnClickListener {
            showFileChooser()
        }

        btnConfirm.setOnClickListener {
            val name = etName.text.toString()
            val email = etEmail.text.toString()
            val mobile = etMobile.text.toString()
            val details = etDetails.text.toString()

            if (name.isEmpty()) {
                etName.error = getString(R.string.empty)
                return@setOnClickListener
            }
            if (email.isEmpty()) {
                etEmail.error = getString(R.string.empty)
                return@setOnClickListener
            }

            if (mobile.isEmpty()) {
                etMobile.error = getString(R.string.empty)
                return@setOnClickListener
            }

            if (prop.isNullOrEmpty()) {
                tvPriority.error = getString(R.string.empty)
                return@setOnClickListener
            }

            if (details.isEmpty()) {
                etDetails.error = getString(R.string.empty)
                return@setOnClickListener
            }

            if (strStartDate.isNullOrEmpty()) {
                tvStartDate.error = getString(R.string.empty)
                return@setOnClickListener
            }

            if (strEndDate.isNullOrEmpty()) {
                tvEndDate.error = getString(R.string.empty)
                return@setOnClickListener
            }


            val digital = DigitalServiceBody(
                type!!,
                name,
                email,
                mobile,
                prop!!,
                details,
                strStartDate!!,
                strEndDate!!,
                strImg,
                strFile
            )
            viewModel.digitalService(digital)


        }

        setupObserver()

    }

    private fun handleToolbar() {
        imgArrowBack.setOnClickListener {
            finish()

        }

        tvTitleToolbar.text = getString(R.string.digital_services)

    }

    private fun initSpinnerAdapter() {

        val prop = ArrayList<SpinnerObj>()
        prop.add(SpinnerObj(0, getString(R.string.periorty)))
        prop.add(SpinnerObj(0, getString(R.string.high)))
        prop.add(SpinnerObj(0, getString(R.string.middle)))
        prop.add(SpinnerObj(0, getString(R.string.low)))
        val adapter = ArrayAdapter(this, R.layout.simple_spinner_item2, prop)
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)

        //  spProp.adapter = adapter


    }

    private fun selectPropDialog(activity: Activity) {
        val options =
            arrayOf<CharSequence>(
                activity.resources.getString(R.string.high),
                activity.getString(R.string.middle),
                activity.getString(R.string.low)
            )
        val builder = MaterialAlertDialogBuilder(activity, R.style.AlertDialogCustom)

        val title = TextView(activity)
        title.text = activity.getString(R.string.periorty)
        title.setPadding(30, 30, 30, 30)
        title.textSize = 16f
        title.typeface = ResourcesCompat.getFont(activity, R.font.cairo_bold)

        title.setBackgroundColor(ContextCompat.getColor(activity, R.color.colorPrimary))
        title.setTextColor(Color.WHITE)

        builder.setCustomTitle(title)
        builder.setItems(options) { dialog, item ->
            if (options[item] == activity.getString(R.string.high)) {
                prop = activity.getString(R.string.high);
                tvPriority.text = prop

            } else if (options[item] == activity.getString(R.string.middle)) {

                prop = activity.getString(R.string.middle);
                tvPriority.text = prop

            } else if (options[item] == activity.getString(R.string.low)) {

                prop = activity.getString(R.string.low);
                tvPriority.text = prop

            }
        }

        builder.show()
    }


    private fun setDateStart() {
        val year: Int = calendarDate!!.get(Calendar.YEAR)
        val month: Int = calendarDate!!.get(Calendar.MONTH)
        val day: Int = calendarDate!!.get(Calendar.DAY_OF_MONTH)
        Log.e("system ", "setDateStart: day $day month: $month year: $year")

        val date = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                calendarDate!!.set(Calendar.YEAR, year)
                calendarDate!!.set(Calendar.MONTH, monthOfYear)
                calendarDate!!.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                startDate = calendarDate!!.time
                compareStartDate()
                Log.e(
                    "setDateStart",
                    "DatePickerDialog: day $dayOfMonth month: $monthOfYear year: $year"
                )

            }, year, month, day
        )
        date.datePicker.minDate = System.currentTimeMillis() - 1000

        date.show()

    }

    private fun setDateEnd() {
        val year: Int = calendarDate!!.get(Calendar.YEAR)
        val month: Int = calendarDate!!.get(Calendar.MONTH)
        val day: Int = calendarDate!!.get(Calendar.DAY_OF_MONTH)

        val date = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                calendarDate!!.set(Calendar.YEAR, year)
                calendarDate!!.set(Calendar.MONTH, monthOfYear)
                calendarDate!!.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                endDate = calendarDate!!.time

                compareEndDate()
            }, year, month, day
        )
        date.datePicker.minDate = System.currentTimeMillis() - 1000

        date.show()

    }

    private fun compareStartDate() {
        if (endDate == null) {
            updateDateText(calendarDate!!, tvStartDate)
            strStartDate = Helper.getFormatDate(date = startDate!!)

            return
        }

        if (endDate != null) {
            if (startDate?.compareTo(endDate)!! <= 0) {
                updateDateText(calendarDate!!, tvStartDate)
                strStartDate = Helper.getFormatDate(date = startDate!!)

            } else {
                Toast.makeText(this, getString(R.string.invalid_date), Toast.LENGTH_LONG).show()
                tvStartDate.text = getString(R.string.select_date)
                startDate = null
            }
        }

    }

    private fun compareEndDate() {
        if (startDate == null) {
            Toast.makeText(
                this,
                getString(R.string.select_start_date_before),
                Toast.LENGTH_LONG
            ).show()
            return
        }


        if (startDate?.compareTo(endDate)!! <= 0) {
            updateDateText(calendarDate!!, tvEndDate)
            strEndDate = Helper.getFormatDate(date = endDate!!)
        } else {
            Toast.makeText(this, getString(R.string.invalid_date), Toast.LENGTH_LONG).show()
        }


    }

    private fun updateDateText(c: Calendar, textView: TextView) {
        val myFormat = "dd MMM" //In which you need put here
        var locale = Locale("ar", "SA")
        if (Locale.getDefault().language == "en") {
            locale = Locale.ENGLISH
        }
        val sdf = SimpleDateFormat(myFormat, locale)


        textView.text = sdf.format(c.time)

    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 2404) {
            //Image Uri will not be null for RESULT_OK
            val fileUri = data?.data
            imgAdd.setImageURI(fileUri)

            val file: File = ImagePicker.getFile(data)!!
            val filePath: String = ImagePicker.getFilePath(data)!!
            tvImgName.text = file.name
            strImg = Helper.encodeFile(file)
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        }

        else  if (resultCode == RESULT_OK && requestCode == 111) {
            val uri: Uri? = data?.data
            Log.d(TAG, "onActivityResult Uri: " + uri.toString())
            // Get the path
            // Get the path

              val name: String = Helper.getFileName(this, uri!!)!!
            val path: String = FilePath.getPath(this, uri!!)!!
            Log.e(TAG, "onActivityResult path: $path")
            val file = File(path)
            strFile = Helper.encodeFile(file)
            tvFileName.text =name

        }
        else {
            Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show()
        }
    }


    private fun showFileChooser() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        try {
            startActivityForResult(
                Intent.createChooser(intent, "Select a File to Upload"),
                111
            )
        } catch (ex: ActivityNotFoundException) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(
                this, "Please install a File Manager.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    private fun initViewModel() {

        viewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiHelperImpl(RetrofitBuilder.apiService), application)
        ).get(DigitalServiceViewModel::class.java)
    }

    private fun setupObserver() {

        viewModel.getDigitalService().observe(this,
            Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        progressBar.visibility = View.GONE
                        it.data?.let { users ->
                            Toast.makeText(this, users.message, Toast.LENGTH_SHORT).show()

                        }
                    }
                    Status.LOADING -> {
                        progressBar.visibility = View.VISIBLE

                    }
                    Status.ERROR -> {
                        progressBar.visibility = View.GONE

                    }
                }

            }
        )
    }


}