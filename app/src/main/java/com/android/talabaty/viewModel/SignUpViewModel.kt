package com.android.talabaty.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.talabaty.R
import com.android.talabaty.model.*
import com.android.talabaty.retrofit.ApiHelper
import com.android.talabaty.dbUtil.Resource
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import java.io.IOException

class SignUpViewModel(private val apiHelper: ApiHelper,var context: Context) : ViewModel() {
    val TAG = "SignUpViewModel"
    private val signUp = MutableLiveData<Resource<SignUp>>()
    private val sendCode = MutableLiveData<Resource<CheckCode>>()
    private val reSendCode = MutableLiveData<Resource<GeneralResponse>>()


     fun signUp(  signUpPost:SignUpPost) {
        viewModelScope.launch {
            signUp.postValue(Resource.loading(null))
            try {
                withTimeout(20_000) {
                    val usersFromApi = apiHelper.signUp(signUpPost)

                    if (usersFromApi.code == 200 && usersFromApi.status) {
                        signUp.postValue(Resource.success(usersFromApi))

                    } else {
                        signUp.postValue(Resource.error(usersFromApi.message, null))
                    }
                }
            } catch (e: TimeoutCancellationException) {
                signUp.postValue(Resource.error(context.getString(R.string.timeout_error), null))
            } catch (e: Exception) {
                if (e is IOException) {
                    signUp.postValue(Resource.error(context.getString(R.string.network_error), null))

                } else {
                    signUp.postValue(Resource.error(context.getString(R.string.something_went_error), null))
                }
                Log.e(TAG, "signUp: ${e.message}")
            }
        }
    }



     fun sendCode(code: Int, mobile: String) {
        viewModelScope.launch {
            sendCode.postValue(Resource.loading(null))
            try {
                withTimeout(20_000) {
                    val usersFromApi = apiHelper.checkCode(code, mobile)

                    if (usersFromApi.code == 200 && usersFromApi.status)
                        sendCode.postValue(Resource.success(usersFromApi))
                    else {
                        sendCode.postValue(Resource.error(usersFromApi.message, null))
                    }
                }

            } catch (e: TimeoutCancellationException) {
                sendCode.postValue(Resource.error(context.getString(R.string.timeout_error), null))
            } catch (e: Exception) {
                if (e is IOException) {
                    sendCode.postValue(Resource.error(context.getString(R.string.network_error), null))

                } else {
                    sendCode.postValue(Resource.error(context.getString(R.string.something_went_error), null))
                }
                Log.e(TAG, "sendCode: ${e.message}")
            }
        }
    }

     fun reSendCode(mobile: String) {
        viewModelScope.launch {
            reSendCode.postValue(Resource.loading(null))
            try {
                withTimeout(20_000) {
                    val usersFromApi = apiHelper.requestNewCode(mobile)

                    if (usersFromApi.code == 200 && usersFromApi.status)
                        reSendCode.postValue(Resource.success(usersFromApi))
                    else {
                        reSendCode.postValue(Resource.error(usersFromApi.message, null))
                    }
                }

            } catch (e: TimeoutCancellationException) {
                reSendCode.postValue(Resource.error(context.getString(R.string.timeout_error), null))
            } catch (e: Exception) {
                if (e is IOException) {
                    reSendCode.postValue(Resource.error(context.getString(R.string.network_error), null))

                } else {
                    reSendCode.postValue(Resource.error(context.getString(R.string.something_went_error), null))
                }
                Log.e(TAG, "reSendCode: ${e.message}")
            }
        }
    }

    fun getSignUp(): LiveData<Resource<SignUp>> {
        return signUp
    }



    fun sendCode(): LiveData<Resource<CheckCode>> {
        return sendCode
    }

    fun reSendCode(): LiveData<Resource<GeneralResponse>> {
        return reSendCode
    }

}