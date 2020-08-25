package com.android.talabaty.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.talabaty.model.EditProfile
import com.android.talabaty.model.Login
import com.android.talabaty.model.SignUp
import com.android.talabaty.model.UserPost
import com.android.talabaty.retrofit.ApiHelper
import com.mahmoud.todoapp.util.dbUtil.Resource
import kotlinx.coroutines.launch

class ProfileViewModel(private val apiHelper: ApiHelper) : ViewModel() {
    val TAG = "ProfileViewModel"
    private val profile = MutableLiveData<Resource<EditProfile>>()
    private val editProfile = MutableLiveData<Resource<EditProfile>>()


    private fun profile() {
        viewModelScope.launch {
            profile.postValue(Resource.loading(null))
            try {
                val usersFromApi = apiHelper.getProfile()

                if (usersFromApi.status && usersFromApi.code == 200)
                    profile.postValue(Resource.success(usersFromApi))
                else{
                    profile.postValue(Resource.error(usersFromApi.message, null))

                }
            } catch (e: Exception) {
                Log.e(TAG, "profile: ${e.message}")
                profile.postValue(Resource.error("context.getString(R.string.something_went_error)", null))
            }
        }
    }

    private fun editProfile(profile: UserPost) {
        viewModelScope.launch {
            editProfile.postValue(Resource.loading(null))
            try {
                val usersFromApi = apiHelper.editProfile(profile)

                if (usersFromApi.status && usersFromApi.code == 200)
                    editProfile.postValue(Resource.success(usersFromApi))
                else{
                    editProfile.postValue(Resource.error(usersFromApi.message, null))

                }

            } catch (e: Exception) {
                Log.e(TAG, "profile: ${e.message}")
                editProfile.postValue(Resource.error("context.getString(R.string.something_went_error)", null))
            }
        }
    }

    fun getProfile(): LiveData<Resource<EditProfile>> {
        return profile
    }

    fun editProfile(): LiveData<Resource<EditProfile>> {
        return profile
    }


}