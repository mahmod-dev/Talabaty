package com.android.talabaty.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.talabaty.R
import com.android.talabaty.model.*
import com.android.talabaty.retrofit.ApiHelper
import com.mahmoud.todoapp.util.dbUtil.Resource
import kotlinx.coroutines.launch

class LoginViewModel(private val apiHelper: ApiHelper) : ViewModel() {
    private val TAG = "LoginViewModel"
    private val login = MutableLiveData<Resource<Login>>()
    private val logout = MutableLiveData<Resource<GeneralResponse>>()
    private val forgotPassword = MutableLiveData<Resource<GeneralResponse>>()


    fun login(user: LoginPost) {
        viewModelScope.launch {
            login.postValue(Resource.loading(null))
            try {
                val usersFromApi = apiHelper.login(user)

                if (usersFromApi.status && usersFromApi.code == 200)
                    login.postValue(Resource.success(usersFromApi))


            } catch (e: Exception) {
                Log.e(TAG, "login: ${e.message}")
                login.postValue(Resource.error("Something Went Wrong", null))
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            logout.postValue(Resource.loading(null))
            try {
                val usersFromApi = apiHelper.getLogout()

                if (usersFromApi.status && usersFromApi.code == 200)
                    logout.postValue(Resource.success(usersFromApi))
                else {
                    logout.postValue(Resource.error(usersFromApi.message, null))

                }


            } catch (e: Exception) {
                Log.e(TAG, "logout: ${e.message}")
                logout.postValue(Resource.error("Something Went Wrong", null))
            }
        }
    }

    fun forgotPassword(email: String) {
        viewModelScope.launch {
            forgotPassword.postValue(Resource.loading(null))
            try {
                val usersFromApi = apiHelper.forgotPassword(email)

                if (usersFromApi.code == 200 && usersFromApi.status)
                    forgotPassword.postValue(Resource.success(usersFromApi))
                else {
                    forgotPassword.postValue(Resource.error(usersFromApi.message, null))

                }
            } catch (e: Exception) {
                Log.e(TAG, "forgotPassword: ${e.message}")
                forgotPassword.postValue(Resource.error("Something Went Wrong", null))
            }
        }
    }


    fun getLogin(): LiveData<Resource<Login>> {
        return login
    }

    fun getLogout(): LiveData<Resource<GeneralResponse>> {
        return logout
    }

    fun getForgotPassword(): LiveData<Resource<GeneralResponse>> {
        return forgotPassword
    }
}