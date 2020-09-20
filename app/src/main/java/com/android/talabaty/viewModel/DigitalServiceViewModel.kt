package com.android.talabaty.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.android.talabaty.R
import com.android.talabaty.model.*
import com.android.talabaty.retrofit.ApiHelper
import com.android.talabaty.dbUtil.Resource
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.IOException

class DigitalServiceViewModel(private val apiHelper: ApiHelper?, var context: Context) :
    ViewModel() {
    private val TAG = "DigitalServiceViewModel"
    private val digitalService = MutableLiveData<Resource<DigitalService>>()
    private val digital = MutableLiveData<Resource<getDigitals>>()


    fun digitalService(
        digital_id: Int,
        name: String,
        email: String,
        mobile: String,
        priority: String,
        details: String,
        date_from: String,
        date_to: String,
        order_images: MultipartBody.Part?,
        order_files: MultipartBody.Part?
    ) {

        val digitalIdRequest =
            RequestBody.create(MediaType.parse("text/plain"), digital_id.toString())
        val usernameRequest = RequestBody.create(MediaType.parse("text/plain"), name)
        val emailRequest = RequestBody.create(MediaType.parse("text/plain"), email)
        val mobileRequest = RequestBody.create(MediaType.parse("text/plain"), mobile)
        val priorityRequest = RequestBody.create(MediaType.parse("text/plain"), priority)
        val detailsRequest = RequestBody.create(MediaType.parse("text/plain"), details)
        val dateFromRequest = RequestBody.create(MediaType.parse("text/plain"), date_from)
        val dateToRequest = RequestBody.create(MediaType.parse("text/plain"), date_to)

        viewModelScope.launch {

            digitalService.postValue(Resource.loading(null))
            try {
                withTimeout(20_000) {
                    val usersFromApi = apiHelper?.requestDigitalService(
                        digitalIdRequest,
                        usernameRequest,
                        emailRequest,
                        mobileRequest,
                        priorityRequest,
                        detailsRequest,
                        dateFromRequest,
                        dateToRequest,
                        order_images,
                        order_files
                    )

                    if (usersFromApi!!.status && usersFromApi.code == 200)
                        digitalService.postValue(Resource.success(usersFromApi))
                    else {
                        digitalService.postValue(Resource.error(usersFromApi.message, null))

                    }

                }
            } catch (e: TimeoutCancellationException) {
                digitalService.postValue(
                    Resource.error(
                        context.getString(R.string.timeout_error),
                        null
                    )
                )
            } catch (e: Exception) {
                if (e is IOException) {
                    digitalService.postValue(
                        Resource.error(
                            context.getString(R.string.network_error),
                            null
                        )
                    )
                } else {
                    digitalService.postValue(
                        Resource.error(
                            context.getString(R.string.something_went_error),
                            null
                        )
                    )
                }
                Log.e(TAG, "digitalService: ${e.message}")
            }
        }
    }

    fun digitals() {
        viewModelScope.launch {

            digital.postValue(Resource.loading(null))
            try {
                withTimeout(20_000) {
                    val usersFromApi = apiHelper?.getDigitals()

                    if (usersFromApi!!.status && usersFromApi.code == 200)
                        digital.postValue(Resource.success(usersFromApi))
                    else {
                        digital.postValue(Resource.error(usersFromApi.message, null))

                    }

                }
            } catch (e: TimeoutCancellationException) {
                digital.postValue(Resource.error(context.getString(R.string.timeout_error), null))
            } catch (e: Exception) {
                if (e is IOException) {
                    digital.postValue(
                        Resource.error(
                            context.getString(R.string.network_error),
                            null
                        )
                    )
                } else {
                    digital.postValue(
                        Resource.error(
                            context.getString(R.string.something_went_error),
                            null
                        )
                    )
                }
                Log.e(TAG, "digitals: ${e.message}")
            }
        }
    }


    fun getDigitalService(): LiveData<Resource<DigitalService>> {
        return digitalService
    }

    fun getAllDigitals(): LiveData<Resource<getDigitals>> {
        return digital
    }

}