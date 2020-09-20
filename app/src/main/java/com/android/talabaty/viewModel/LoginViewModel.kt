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

class LoginViewModel(private val apiHelper: ApiHelper,var context: Context) : ViewModel() {
    private val TAG = "LoginViewModel"
    private val login = MutableLiveData<Resource<Login>>()
    private val logout = MutableLiveData<Resource<GeneralResponse>>()
    private val forgotPassword = MutableLiveData<Resource<GeneralResponse>>()


    fun login(user: LoginPost) {
        viewModelScope.launch {
            login.postValue(Resource.loading(null))
            try {
                withTimeout(20_000) {
                    val usersFromApi = apiHelper.login(user)

                    if (usersFromApi.status && usersFromApi.code == 200)
                        login.postValue(Resource.success(usersFromApi))
                    else {
                        login.postValue(Resource.error("Something Went Wrong", null))
                    }

                }
            } catch (e: TimeoutCancellationException) {
                login.postValue(Resource.error(context.getString(R.string.timeout_error), null))
            } catch (e: Exception) {
                if (e is IOException) {
                    login.postValue(Resource.error(context.getString(R.string.network_error), null))
                } else {
                    login.postValue(Resource.error(context.getString(R.string.something_went_error), null))
                }
                Log.e(TAG, "login: ${e.message}")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            logout.postValue(Resource.loading(null))
            try {
                withTimeout(20_000) {
                    val usersFromApi = apiHelper.getLogout()

                    if (usersFromApi.status && usersFromApi.code == 200)
                        logout.postValue(Resource.success(usersFromApi))
                    else {
                        logout.postValue(Resource.error(usersFromApi.message, null))

                    }

                }
            }catch (e: TimeoutCancellationException) {
                logout.postValue(Resource.error(context.getString(R.string.timeout_error), null))
            } catch (e: Exception) {
                if (e is IOException) {
                    logout.postValue(Resource.error(context.getString(R.string.network_error), null))
                } else {
                    logout.postValue(Resource.error(context.getString(R.string.something_went_error), null))
                }
                Log.e(TAG, "logout: ${e.message}")
            }
        }
    }

    fun forgotPassword(email: String) {
        viewModelScope.launch {
            forgotPassword.postValue(Resource.loading(null))
            try {
                withTimeout(20_000) {
                    val usersFromApi = apiHelper.forgotPassword(email)

                    if (usersFromApi.code == 200 && usersFromApi.status)
                        forgotPassword.postValue(Resource.success(usersFromApi))
                    else {
                        forgotPassword.postValue(Resource.error(usersFromApi.message, null))

                    }
                }
            }catch (e: TimeoutCancellationException) {
                forgotPassword.postValue(Resource.error(context.getString(R.string.timeout_error), null))
            } catch (e: Exception) {
                if (e is IOException) {
                    forgotPassword.postValue(Resource.error(context.getString(R.string.network_error), null))
                } else {
                    forgotPassword.postValue(Resource.error(context.getString(R.string.something_went_error), null))
                }
                Log.e(TAG, "forgotPassword: ${e.message}")
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