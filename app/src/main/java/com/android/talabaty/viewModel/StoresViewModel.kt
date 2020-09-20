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
import kotlinx.coroutines.withTimeout
import java.io.IOException
import java.net.ConnectException

class StoresViewModel(private val apiHelper: ApiHelper?,var context: Context) :
    ViewModel() {
    private val TAG = "StoresViewModel"
    private val stores = MutableLiveData<Resource<Activities>>()
    private val viewStores = MutableLiveData<Resource<ViewStores>>()
    private val nearbyStores = MutableLiveData<Resource<GetNearbyStores>>()
    private val tatbeqakumProduct = MutableLiveData<Resource<GetViewTatbeqakumProducts>>()

    fun stores() {
        viewModelScope.launch {

            stores.postValue(Resource.loading(null))
            try {
                withTimeout(20_000) {
                    val usersFromApi = apiHelper?.getActivities()

                    if (usersFromApi!!.status && usersFromApi.code == 200)
                        stores.postValue(Resource.success(usersFromApi))
                    else {
                        stores.postValue(Resource.error(usersFromApi.message, null))
                    }
                }

            } catch (e: TimeoutCancellationException) {
                stores.postValue(Resource.error(context.getString(R.string.timeout_error), null))
            } catch (e: Exception) {
                if (e is IOException) {
                    stores.postValue(Resource.error(context.getString(R.string.network_error), null))

                } else {
                    stores.postValue(Resource.error(context.getString(R.string.something_went_error), null))
                }
                Log.e(TAG, "stores: ${e.message}")
            }
        }
    }

     fun storesById(id: Int) {
        viewModelScope.launch {

            viewStores.postValue(Resource.loading(null))
            try {
                withTimeout(20_000) {
                    val usersFromApi = apiHelper?.getViewStores(id)
                    if (usersFromApi!!.status && usersFromApi.code == 200)
                        viewStores.postValue(Resource.success(usersFromApi))
                    else {
                        viewStores.postValue(Resource.error(usersFromApi.message, null))
                    }
                }

            } catch (e: TimeoutCancellationException) {
                viewStores.postValue(Resource.error(context.getString(R.string.timeout_error), null))
            } catch (e: Exception) {
                if (e is IOException) {
                    viewStores.postValue(Resource.error(context.getString(R.string.network_error), null))

                } else {
                    viewStores.postValue(Resource.error(context.getString(R.string.something_went_error), null))
                }
                Log.e(TAG, "storesById: ${e.message}")
            }
        }
    }

     fun tatbeqProductById(id: Int) {
        viewModelScope.launch {

            tatbeqakumProduct.postValue(Resource.loading(null))
            try {
                withTimeout(20_000) {
                    val usersFromApi = apiHelper?.viewTatbeqakumProducts(id)
                    if (usersFromApi!!.status && usersFromApi.code == 200)
                        tatbeqakumProduct.postValue(Resource.success(usersFromApi))
                    else {
                        tatbeqakumProduct.postValue(Resource.error(usersFromApi.message, null))
                    }
                }
            } catch (e: TimeoutCancellationException) {
                tatbeqakumProduct.postValue(Resource.error(context.getString(R.string.timeout_error), null))
            } catch (e: Exception) {
                if (e is IOException) {
                    tatbeqakumProduct.postValue(Resource.error(context.getString(R.string.network_error), null))

                } else {
                    tatbeqakumProduct.postValue(Resource.error(context.getString(R.string.something_went_error), null))
                }
                Log.e(TAG, "tatbeqProductById: ${e.message}")
            }
        }
    }

    public fun nearbyStores(lat: Long,lng:Long) {
        viewModelScope.launch {

            nearbyStores.postValue(Resource.loading(null))
            try {
                withTimeout(20_000) {
                    val usersFromApi = apiHelper?.nearbyStores(lat, lng)
                    if (usersFromApi!!.status && usersFromApi.code == 200)
                        nearbyStores.postValue(Resource.success(usersFromApi))
                    else {
                        nearbyStores.postValue(Resource.error(usersFromApi.message, null))
                    }
                }
            } catch (e: TimeoutCancellationException) {
                nearbyStores.postValue(Resource.error(context.getString(R.string.timeout_error), null))
            } catch (e: Exception) {
                if (e is IOException) {
                    nearbyStores.postValue(Resource.error(context.getString(R.string.network_error), null))

                } else {
                    nearbyStores.postValue(Resource.error(context.getString(R.string.something_went_error), null))
                }
                Log.e(TAG, "nearbyStores: ${e.message}")
            }
        }
    }


    fun getAllStores(): LiveData<Resource<Activities>> {
        return stores
    }

    fun getStoresById(): LiveData<Resource<ViewStores>> {
        return viewStores
    }
    fun getNearbyStores(): LiveData<Resource<GetNearbyStores>> {
        return nearbyStores
    }

    fun getAllTatbeqakumProducts(): LiveData<Resource<GetViewTatbeqakumProducts>> {
        return tatbeqakumProduct
    }

}