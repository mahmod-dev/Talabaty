package com.android.talabaty.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.talabaty.R
import com.android.talabaty.model.EditProfile
import com.android.talabaty.model.UserPost
import com.android.talabaty.retrofit.ApiHelper
import com.android.talabaty.dbUtil.Resource
import com.android.talabaty.model.GeneralResponse
import com.android.talabaty.model.GetUserDetails
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.IOException

class ProfileViewModel(private val apiHelper: ApiHelper, var context: Context) : ViewModel() {
    val TAG = "ProfileViewModel"
    private val profile = MutableLiveData<Resource<EditProfile>>()
    private val editProfile = MutableLiveData<Resource<EditProfile>>()
    private val changeNotificationStatus = MutableLiveData<Resource<GeneralResponse>>()
    private val userDetails = MutableLiveData<Resource<GetUserDetails>>()


    fun profile() {
        viewModelScope.launch {
            profile.postValue(Resource.loading(null))
            try {
                withTimeout(20_000) {
                    val usersFromApi = apiHelper.getProfile()

                    if (usersFromApi.status && usersFromApi.code == 200)
                        profile.postValue(Resource.success(usersFromApi))
                    else {
                        profile.postValue(Resource.error(usersFromApi.message, null))

                    }
                }
            } catch (e: TimeoutCancellationException) {
                profile.postValue(Resource.error(context.getString(R.string.timeout_error), null))
            } catch (e: Exception) {
                if (e is IOException) {
                    profile.postValue(
                        Resource.error(
                            context.getString(R.string.network_error),
                            null
                        )
                    )

                } else {
                    profile.postValue(
                        Resource.error(
                            context.getString(R.string.something_went_error),
                            null
                        )
                    )
                }
                Log.e(TAG, "profile: ${e.message}")
            }
        }
    }

    fun editProfile(
        name: String,
        email: String,
        mobile: String,
        latitude: Long,
        longitude: Long,
        password: String,
        device_type: String,
        fcm_token: String,
        image_profile: MultipartBody.Part?
    ) {
        val usernameRequest = RequestBody.create(MediaType.parse("text/plain"), name)
        val emailRequest = RequestBody.create(MediaType.parse("text/plain"), email)
        val mobileRequest = RequestBody.create(MediaType.parse("text/plain"), mobile)
        val passwordRequest = RequestBody.create(MediaType.parse("text/plain"), password)
        val fcmRequest = RequestBody.create(MediaType.parse("text/plain"), fcm_token)
        val latRequest = RequestBody.create(MediaType.parse("text/plain"), latitude.toString())
        val lngRequest = RequestBody.create(MediaType.parse("text/plain"), longitude.toString())
        val deviceTypeRequest = RequestBody.create(MediaType.parse("text/plain"), device_type)

        viewModelScope.launch {
            editProfile.postValue(Resource.loading(null))
            try {
                withTimeout(70_000) {
                    val usersFromApi = apiHelper.editProfile(
                        usernameRequest,
                        emailRequest,
                        mobileRequest,
                        latRequest,
                        lngRequest,
                        passwordRequest,
                        deviceTypeRequest,
                        fcmRequest,
                        image_profile
                    )

                    if (usersFromApi.status && usersFromApi.code == 200)
                        editProfile.postValue(Resource.success(usersFromApi))
                    else {
                        editProfile.postValue(Resource.error(usersFromApi.message, null))

                    }
                }
            } catch (e: TimeoutCancellationException) {
                editProfile.postValue(
                    Resource.error(
                        context.getString(R.string.timeout_error),
                        null
                    )
                )
            } catch (e: Exception) {
                if (e is IOException) {
                    editProfile.postValue(
                        Resource.error(
                            context.getString(R.string.network_error),
                            null
                        )
                    )
                } else {
                    editProfile.postValue(
                        Resource.error(
                            context.getString(R.string.something_went_error),
                            null
                        )
                    )
                }
                Log.e(TAG, "editProfile: ${e.message}")
            }
        }
    }

    fun userDetails() {
        viewModelScope.launch {
            userDetails.postValue(Resource.loading(null))
            try {
                withTimeout(20_000) {
                    val usersFromApi = apiHelper.getUserDetails()
                    userDetails.postValue(Resource.success(usersFromApi))
                }

            } catch (e: TimeoutCancellationException) {
                userDetails.postValue(
                    Resource.error(
                        context.getString(R.string.timeout_error),
                        null
                    )
                )
            } catch (e: Exception) {
                if (e is IOException) {
                    userDetails.postValue(
                        Resource.error(
                            context.getString(R.string.network_error),
                            null
                        )
                    )
                } else {
                    userDetails.postValue(
                        Resource.error(
                            context.getString(R.string.something_went_error),
                            null
                        )
                    )
                }
                Log.e(TAG, "editProfile: ${e.message}")
            }
        }
    }


    fun changeNotificationStatus(status: Int, notification: String) {
        viewModelScope.launch {
            changeNotificationStatus.postValue(Resource.loading(null))
            try {
                withTimeout(20_000) {
                    val usersFromApi = apiHelper.changeNotifiStatus(status, notification)

                    if (usersFromApi.status && usersFromApi.code == 200)
                        changeNotificationStatus.postValue(Resource.success(usersFromApi))
                    else {
                        changeNotificationStatus.postValue(
                            Resource.error(
                                usersFromApi.message,
                                null
                            )
                        )

                    }
                }
            } catch (e: TimeoutCancellationException) {
                changeNotificationStatus.postValue(
                    Resource.error(
                        context.getString(R.string.timeout_error),
                        null
                    )
                )
            } catch (e: Exception) {
                if (e is IOException) {
                    changeNotificationStatus.postValue(
                        Resource.error(
                            context.getString(R.string.network_error),
                            null
                        )
                    )
                } else {
                    changeNotificationStatus.postValue(
                        Resource.error(
                            context.getString(R.string.something_went_error),
                            null
                        )
                    )
                }
                Log.e(TAG, "editProfile: ${e.message}")
            }
        }
    }


    fun getProfile(): LiveData<Resource<EditProfile>> {
        return profile
    }

    fun editProfile(): LiveData<Resource<EditProfile>> {
        return editProfile
    }

    fun getChangeNotificationStatus(): LiveData<Resource<GeneralResponse>> {
        return changeNotificationStatus
    }

    fun getUserDetails(): LiveData<Resource<GetUserDetails>> {
        return userDetails
    }


}