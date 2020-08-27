package com.android.talabaty.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.android.talabaty.model.*
import com.android.talabaty.retrofit.ApiHelper
import com.android.talabaty.dbUtil.Resource
import kotlinx.coroutines.launch

class StoresViewModel(private val apiHelper: ApiHelper?, application: Application) :
    AndroidViewModel(application) {
    private val TAG = "StoresViewModel"
    private val stores = MutableLiveData<Resource<Activities>>()
    private val viewStores = MutableLiveData<Resource<ViewStores>>()

    init {
        stores()
    }

    private fun stores() {
        viewModelScope.launch {

            stores.postValue(Resource.loading(null))
            try {
                val usersFromApi = apiHelper?.getActivities()

                if (usersFromApi!!.status && usersFromApi.code == 200)
                    stores.postValue(Resource.success(usersFromApi))
                else {
                    stores.postValue(Resource.error(usersFromApi.message, null))

                }


            } catch (e: Exception) {
                Log.e(TAG, "stores: ${e.message}")
                stores.postValue(Resource.error("Something Went Wrong", null))
            }
        }
    }

    public fun storesById(id: Int) {
        viewModelScope.launch {

            viewStores.postValue(Resource.loading(null))
            try {

                val usersFromApi = apiHelper?.getViewStores(id)
                if (usersFromApi!!.status && usersFromApi.code == 200)
                    viewStores.postValue(Resource.success(usersFromApi))
                else {
                    viewStores.postValue(Resource.error(usersFromApi.message, null))

                }

            } catch (e: Exception) {
                Log.e(TAG, "storesById: ${e.message}")
                viewStores.postValue(Resource.error("Something Went Wrong", null))
            }
        }
    }


    fun getAllStores(): LiveData<Resource<Activities>> {
        return stores
    }

    fun getStoresById(): LiveData<Resource<ViewStores>> {
        return viewStores
    }
}