package com.android.talabaty.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.talabaty.model.*
import com.android.talabaty.retrofit.ApiHelper
import com.android.talabaty.dbUtil.Resource
import kotlinx.coroutines.launch

class SignUpViewModel(private val apiHelper: ApiHelper) : ViewModel() {
    val TAG = "SignUpViewModel"
    private val signUp = MutableLiveData<Resource<SignUp>>()
    private val sendCode = MutableLiveData<Resource<CheckCode>>()
    private val reSendCode = MutableLiveData<Resource<GeneralResponse>>()


     fun signUp(  signUpPost:SignUpPost) {
        viewModelScope.launch {
            signUp.postValue(Resource.loading(null))
            try {
                val usersFromApi = apiHelper.signUp(signUpPost)

                if (usersFromApi.code == 200 && usersFromApi.status) {
                    signUp.postValue(Resource.success(usersFromApi))

                }else{
                    signUp.postValue(Resource.error(usersFromApi.message, null))

                }

            } catch (e: Exception) {
                Log.e(TAG, "singUp: ${e.message}")
                signUp.postValue(Resource.error("Something Went Wrong", null))
            }
        }
    }



     fun sendCode(code: Int, mobile: String) {
        viewModelScope.launch {
            sendCode.postValue(Resource.loading(null))
            try {
                val usersFromApi = apiHelper.checkCode(code, mobile)

                if (usersFromApi.code == 200 && usersFromApi.status)
                    sendCode.postValue(Resource.success(usersFromApi))
                else{
                    sendCode.postValue(Resource.error(usersFromApi.message, null))

                }

            } catch (e: Exception) {
                Log.e(TAG, "sendCode: ${e.message}")
                sendCode.postValue(Resource.error("Something Went Wrong", null))
            }
        }
    }

     fun reSendCode(mobile: String) {
        viewModelScope.launch {
            reSendCode.postValue(Resource.loading(null))
            try {
                val usersFromApi = apiHelper.requestNewCode( mobile)

                if (usersFromApi.code == 200 && usersFromApi.status)
                    reSendCode.postValue(Resource.success(usersFromApi))
                else{
                    reSendCode.postValue(Resource.error(usersFromApi.message, null))

                }

            } catch (e: Exception) {
                Log.e(TAG, "reSendCode: ${e.message}")
                reSendCode.postValue(Resource.error("Something Went Wrong", null))
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