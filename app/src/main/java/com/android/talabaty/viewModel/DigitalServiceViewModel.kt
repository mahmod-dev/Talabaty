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
import java.io.IOException

class DigitalServiceViewModel(private val apiHelper: ApiHelper?, var context: Context) :
   ViewModel() {
    private val TAG = "DigitalServiceViewModel"
    private val digitalService = MutableLiveData<Resource<DigitalService>>()
    private val digital = MutableLiveData<Resource<getDigitals>>()


     fun digitalService(service: DigitalServiceBody) {
        viewModelScope.launch {

            digitalService.postValue(Resource.loading(null))
            try {
                val usersFromApi = apiHelper?.requestDigitalService(service)

                if (usersFromApi!!.status && usersFromApi.code == 200)
                    digitalService.postValue(Resource.success(usersFromApi))
                else {
                    digitalService.postValue(Resource.error(usersFromApi.message, null))

                }


            } catch (e: TimeoutCancellationException) {
                digitalService.postValue(Resource.error(context.getString(R.string.timeout_error), null))
            } catch (e: Exception) {
                if (e is IOException) {
                    digitalService.postValue(Resource.error(context.getString(R.string.network_error), null))
                } else {
                    digitalService.postValue(Resource.error(context.getString(R.string.something_went_error), null))
                }
                Log.e(TAG, "digitalService: ${e.message}")
            }
        }
    }

    fun digitals() {
        viewModelScope.launch {

            digital.postValue(Resource.loading(null))
            try {
                val usersFromApi = apiHelper?.getDigitals()

                if (usersFromApi!!.status && usersFromApi.code == 200)
                    digital.postValue(Resource.success(usersFromApi))
                else {
                    digital.postValue(Resource.error(usersFromApi.message, null))

                }


            } catch (e: TimeoutCancellationException) {
                digital.postValue(Resource.error(context.getString(R.string.timeout_error), null))
            } catch (e: Exception) {
                if (e is IOException) {
                    digital.postValue(Resource.error(context.getString(R.string.network_error), null))
                } else {
                    digital.postValue(Resource.error(context.getString(R.string.something_went_error), null))
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