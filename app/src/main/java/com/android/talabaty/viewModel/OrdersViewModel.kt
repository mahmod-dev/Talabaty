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

class OrdersViewModel(private val apiHelper: ApiHelper?,var context: Context) :
    ViewModel() {
    private val TAG = "OrdersViewModel"
    private val orders = MutableLiveData<Resource<MyOrders>>()
    private val storesFreeDelivery = MutableLiveData<Resource<StoresFreeDelivery>>()
    private val newOrder = MutableLiveData<Resource<CreateNewOrder>>()
    private val requestCar = MutableLiveData<Resource<RequestCar>>()
    private val cars = MutableLiveData<Resource<GetCars>>()
    private val settings = MutableLiveData<Resource<MainSettings>>()


     fun myOrders() {
        viewModelScope.launch {

            orders.postValue(Resource.loading(null))
            try {
                val usersFromApi = apiHelper?.getMyOrders()

                if (usersFromApi!!.status && usersFromApi.code == 200)
                    orders.postValue(Resource.success(usersFromApi))
                else {
                    orders.postValue(Resource.error(usersFromApi.message, null))

                }


            }catch (e: TimeoutCancellationException) {
                orders.postValue(Resource.error(context.getString(R.string.timeout_error), null))
            } catch (e: Exception) {
                if (e is IOException) {
                    orders.postValue(Resource.error(context.getString(R.string.network_error), null))
                } else {
                    orders.postValue(Resource.error(context.getString(R.string.something_went_error), null))
                }
                Log.e(TAG, "myOrders: ${e.message}")
            }
        }
    }

    fun settings() {
        viewModelScope.launch {

            settings.postValue(Resource.loading(null))
            try {
                val usersFromApi = apiHelper?.getSettings()

                if (usersFromApi!!.status && usersFromApi.code == 200)
                    settings.postValue(Resource.success(usersFromApi))
                else {
                    settings.postValue(Resource.error(usersFromApi.message, null))

                }


            } catch (e: TimeoutCancellationException) {
                settings.postValue(Resource.error(context.getString(R.string.timeout_error), null))
            } catch (e: Exception) {
                if (e is IOException) {
                    settings.postValue(Resource.error(context.getString(R.string.network_error), null))
                } else {
                    settings.postValue(Resource.error(context.getString(R.string.something_went_error), null))
                }
                Log.e(TAG, "settings: ${e.message}")
            }
        }
    }


    fun requestCar(car:RequestCarPost) {
        viewModelScope.launch {

            requestCar.postValue(Resource.loading(null))
            try {
                val usersFromApi = apiHelper?.requestCar(car)

                if (usersFromApi!!.status && usersFromApi.code == 200)
                    requestCar.postValue(Resource.success(usersFromApi))
                else {
                    requestCar.postValue(Resource.error(usersFromApi.message, null))

                }


            } catch (e: TimeoutCancellationException) {
                requestCar.postValue(Resource.error(context.getString(R.string.timeout_error), null))
            } catch (e: Exception) {
                if (e is IOException) {
                    requestCar.postValue(Resource.error(context.getString(R.string.network_error), null))
                } else {
                    requestCar.postValue(Resource.error(context.getString(R.string.something_went_error), null))
                }
                Log.e(TAG, "requestCar: ${e.message}")
            }
        }
    }


    fun getCars() {
        viewModelScope.launch {

            cars.postValue(Resource.loading(null))
            try {
                val usersFromApi = apiHelper?.getCars()

                if (usersFromApi!!.status && usersFromApi.code == 200)
                    cars.postValue(Resource.success(usersFromApi))
                else {
                    cars.postValue(Resource.error(usersFromApi.message, null))

                }


            }catch (e: TimeoutCancellationException) {
                cars.postValue(Resource.error(context.getString(R.string.timeout_error), null))
            } catch (e: Exception) {
                if (e is IOException) {
                    cars.postValue(Resource.error(context.getString(R.string.network_error), null))
                } else {
                    cars.postValue(Resource.error(context.getString(R.string.something_went_error), null))
                }
                Log.e(TAG, "getCars: ${e.message}")
            }
        }
    }


    fun createNewOrder(order: NewOrderPost) {
        viewModelScope.launch {

            newOrder.postValue(Resource.loading(null))
            try {
                val usersFromApi = apiHelper?.createNewOrder(order)

                if (usersFromApi!!.status && usersFromApi.code == 200)
                    newOrder.postValue(Resource.success(usersFromApi))
                else {
                    newOrder.postValue(Resource.error(usersFromApi.message, null))

                }


            }catch (e: TimeoutCancellationException) {
                newOrder.postValue(Resource.error(context.getString(R.string.timeout_error), null))
            } catch (e: Exception) {
                if (e is IOException) {
                    newOrder.postValue(Resource.error(context.getString(R.string.network_error), null))
                } else {
                    newOrder.postValue(Resource.error(context.getString(R.string.something_went_error), null))
                }
                Log.e(TAG, "createNewOrder: ${e.message}")
            }
        }
    }

    fun storesFreeDelivery() {
        viewModelScope.launch {

            storesFreeDelivery.postValue(Resource.loading(null))
            try {
                val usersFromApi = apiHelper?.getStoresFreeDelivery()

                if (usersFromApi!!.status && usersFromApi.code == 200)
                    storesFreeDelivery.postValue(Resource.success(usersFromApi))
                else {
                    storesFreeDelivery.postValue(Resource.error(usersFromApi.message, null))

                }


            } catch (e: TimeoutCancellationException) {
                storesFreeDelivery.postValue(Resource.error(context.getString(R.string.timeout_error), null))
            } catch (e: Exception) {
                if (e is IOException) {
                    storesFreeDelivery.postValue(Resource.error(context.getString(R.string.network_error), null))
                } else {
                    storesFreeDelivery.postValue(Resource.error(context.getString(R.string.something_went_error), null))
                }
                Log.e(TAG, "storesFreeDelivery: ${e.message}")
            }
        }
    }


    fun getMyOrders(): LiveData<Resource<MyOrders>> {
        return orders
    }


    fun getStoresFreeDelivery(): LiveData<Resource<StoresFreeDelivery>> {
        return storesFreeDelivery
    }

    fun getCreateNewOrder(): LiveData<Resource<CreateNewOrder>> {
        return newOrder
    }

    fun getRequestCar(): LiveData<Resource<RequestCar>> {
        return requestCar
    }

    fun getAllCars(): LiveData<Resource<GetCars>> {
        return cars
    }

    fun getAllSettings(): LiveData<Resource<MainSettings>> {
        return settings
    }

}