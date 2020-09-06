package com.android.talabaty.viewModel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.android.talabaty.R
import com.android.talabaty.model.*
import com.android.talabaty.retrofit.ApiHelper
import com.android.talabaty.dbUtil.Resource
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.launch
import java.io.IOException

class CategoryViewModel(private val apiHelper: ApiHelper?, var context: Context) :
    ViewModel() {
    private val TAG = "CategoryViewModel"
    private val otherServices = MutableLiveData<Resource<OtherServices>>()


    fun otherServices() {
        viewModelScope.launch {

            otherServices.postValue(Resource.loading(null))
            try {
                val usersFromApi = apiHelper?.getOtherServices()

                if (usersFromApi!!.status && usersFromApi.code == 200)
                    otherServices.postValue(Resource.success(usersFromApi))
                else{
                    otherServices.postValue(Resource.error(usersFromApi.message, null))

                }



            } catch (e: TimeoutCancellationException) {
                otherServices.postValue(Resource.error(context.getString(R.string.timeout_error), null))
            } catch (e: Exception) {
                if (e is IOException) {
                    otherServices.postValue(Resource.error(context.getString(R.string.network_error), null))
                } else {
                    otherServices.postValue(Resource.error(context.getString(R.string.something_went_error), null))
                }
                Log.e(TAG, "otherServices: ${e.message}")
            }
        }
    }



    fun getOtherServices(): LiveData<Resource<OtherServices>> {
        return otherServices
    }


}