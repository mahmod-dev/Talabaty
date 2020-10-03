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

class OrdersViewModel(private val apiHelper: ApiHelper?, var context: Context) :
    ViewModel() {
    private val TAG = "OrdersViewModel"
    private val clientOrders = MutableLiveData<Resource<GetClientOrders>>()
    private val clientOrderDetails = MutableLiveData<Resource<GetClientOrderDetails>>()
    private val cancelOrder = MutableLiveData<Resource<GeneralResponse>>()
    private val storesFreeDelivery = MutableLiveData<Resource<StoresFreeDelivery>>()
    private val newOrder = MutableLiveData<Resource<CreateNewOrder>>()
    private val otherServices = MutableLiveData<Resource<RequestOtherService>>()
    private val services = MutableLiveData<Resource<RequestOtherService>>()
    private val requestCar = MutableLiveData<Resource<RequestCar>>()
    private val cars = MutableLiveData<Resource<GetCars>>()
    private val settings = MutableLiveData<Resource<MainSettings>>()


    fun clientOrders() {
        viewModelScope.launch {

            clientOrders.postValue(Resource.loading(null))
            try {
                withTimeout(20_000) {
                    val usersFromApi = apiHelper?.getClientOrders()

                    if (usersFromApi!!.status && usersFromApi.code == 200)
                        clientOrders.postValue(Resource.success(usersFromApi))
                    else {
                        clientOrders.postValue(Resource.error(usersFromApi.message, null))

                    }
                }

            } catch (e: TimeoutCancellationException) {
                clientOrders.postValue(
                    Resource.error(
                        context.getString(R.string.timeout_error),
                        null
                    )
                )
            } catch (e: Exception) {
                if (e is IOException) {
                    clientOrders.postValue(
                        Resource.error(
                            context.getString(R.string.network_error),
                            null
                        )
                    )
                } else {
                    clientOrders.postValue(
                        Resource.error(
                            context.getString(R.string.something_went_error),
                            null
                        )
                    )
                }
                Log.e(TAG, "clientOrders: ${e.message}")
            }
        }
    }

    fun clientOrderDetails(orderId: Int) {
        viewModelScope.launch {

            clientOrderDetails.postValue(Resource.loading(null))
            try {
                withTimeout(20_000) {
                    val usersFromApi = apiHelper?.getClientOrderDetails(orderId)

                    if (usersFromApi!!.status && usersFromApi.code == 200)
                        clientOrderDetails.postValue(Resource.success(usersFromApi))
                    else {
                        clientOrderDetails.postValue(Resource.error(usersFromApi.message, null))
                    }
                }

            } catch (e: TimeoutCancellationException) {
                clientOrderDetails.postValue(
                    Resource.error(
                        context.getString(R.string.timeout_error),
                        null
                    )
                )
            } catch (e: Exception) {
                if (e is IOException) {
                    clientOrderDetails.postValue(
                        Resource.error(
                            context.getString(R.string.network_error),
                            null
                        )
                    )
                } else {
                    clientOrderDetails.postValue(
                        Resource.error(
                            context.getString(R.string.something_went_error),
                            null
                        )
                    )
                }
                Log.e(TAG, "clientOrderDetails: ${e.message}")
            }
        }
    }

    fun cancelOrder(orderId: Int, notice: String = "") {
        viewModelScope.launch {

            cancelOrder.postValue(Resource.loading(null))
            try {
                withTimeout(20_000) {
                    val usersFromApi = apiHelper?.clientCancelOrder(orderId, notice)

                    if (usersFromApi!!.status && usersFromApi.code == 200)
                        cancelOrder.postValue(Resource.success(usersFromApi))
                    else {
                        cancelOrder.postValue(Resource.error(usersFromApi.message, null))

                    }
                }

            } catch (e: TimeoutCancellationException) {
                cancelOrder.postValue(
                    Resource.error(
                        context.getString(R.string.timeout_error),
                        null
                    )
                )
            } catch (e: Exception) {
                if (e is IOException) {
                    cancelOrder.postValue(
                        Resource.error(
                            context.getString(R.string.network_error),
                            null
                        )
                    )
                } else {
                    cancelOrder.postValue(
                        Resource.error(
                            context.getString(R.string.something_went_error),
                            null
                        )
                    )
                }
                Log.e(TAG, "cancelOrder: ${e.message}")
            }
        }
    }

    fun settings() {
        viewModelScope.launch {

            settings.postValue(Resource.loading(null))
            try {
                withTimeout(20_000) {
                    val usersFromApi = apiHelper?.getSettings()

                    if (usersFromApi!!.status && usersFromApi.code == 200)
                        settings.postValue(Resource.success(usersFromApi))
                    else {
                        settings.postValue(Resource.error(usersFromApi.message, null))

                    }

                }
            } catch (e: TimeoutCancellationException) {
                settings.postValue(Resource.error(context.getString(R.string.timeout_error), null))
            } catch (e: Exception) {
                if (e is IOException) {
                    settings.postValue(
                        Resource.error(
                            context.getString(R.string.network_error),
                            null
                        )
                    )
                } else {
                    settings.postValue(
                        Resource.error(
                            context.getString(R.string.something_went_error),
                            null
                        )
                    )
                }
                Log.e(TAG, "settings: ${e.message}")
            }
        }
    }

    fun requestCar(car: RequestCarPost) {
        viewModelScope.launch {

            requestCar.postValue(Resource.loading(null))
            try {
                withTimeout(20_000) {
                    val usersFromApi = apiHelper?.requestCar(car)

                    if (usersFromApi!!.status && usersFromApi.code == 200)
                        requestCar.postValue(Resource.success(usersFromApi))
                    else {
                        requestCar.postValue(Resource.error(usersFromApi.message, null))

                    }
                }

            } catch (e: TimeoutCancellationException) {
                requestCar.postValue(
                    Resource.error(
                        context.getString(R.string.timeout_error),
                        null
                    )
                )
            } catch (e: Exception) {
                if (e is IOException) {
                    requestCar.postValue(
                        Resource.error(
                            context.getString(R.string.network_error),
                            null
                        )
                    )
                } else {
                    requestCar.postValue(
                        Resource.error(
                            context.getString(R.string.something_went_error),
                            null
                        )
                    )
                }
                Log.e(TAG, "requestCar: ${e.message}")
            }
        }
    }

    fun getCars() {
        viewModelScope.launch {

            cars.postValue(Resource.loading(null))
            try {
                withTimeout(20_000) {
                    val usersFromApi = apiHelper?.getCars()

                    if (usersFromApi!!.status && usersFromApi.code == 200)
                        cars.postValue(Resource.success(usersFromApi))
                    else {
                        cars.postValue(Resource.error(usersFromApi.message, null))
                    }
                }

            } catch (e: TimeoutCancellationException) {
                cars.postValue(Resource.error(context.getString(R.string.timeout_error), null))
            } catch (e: Exception) {
                if (e is IOException) {
                    cars.postValue(Resource.error(context.getString(R.string.network_error), null))
                } else {
                    cars.postValue(
                        Resource.error(
                            context.getString(R.string.something_went_error),
                            null
                        )
                    )
                }
                Log.e(TAG, "getCars: ${e.message}")
            }
        }
    }

    fun createNewOrder(order: NewOrderPost) {
        viewModelScope.launch {

            newOrder.postValue(Resource.loading(null))
            try {
                withTimeout(20_000) {
                    val usersFromApi = apiHelper?.createNewOrder(order)

                    if (usersFromApi!!.status && usersFromApi.code == 200)
                        newOrder.postValue(Resource.success(usersFromApi))
                    else {
                        newOrder.postValue(Resource.error(usersFromApi.message, null))
                    }
                }
            } catch (e: TimeoutCancellationException) {
                newOrder.postValue(Resource.error(context.getString(R.string.timeout_error), null))
            } catch (e: Exception) {
                if (e is IOException) {
                    newOrder.postValue(
                        Resource.error(
                            context.getString(R.string.network_error),
                            null
                        )
                    )
                } else {
                    newOrder.postValue(
                        Resource.error(
                            context.getString(R.string.something_went_error),
                            null
                        )
                    )
                }
                Log.e(TAG, "createNewOrder: ${e.message}")
            }
        }
    }

    fun requestOtherService(order: RequestOtherServicePost) {
        viewModelScope.launch {

            otherServices.postValue(Resource.loading(null))
            try {
                withTimeout(20_000) {
                    val usersFromApi = apiHelper?.requestOtherService(order)

                    if (usersFromApi!!.status && usersFromApi.code == 200)
                        otherServices.postValue(Resource.success(usersFromApi))
                    else {
                        otherServices.postValue(Resource.error(usersFromApi.message, null))
                    }
                }

            } catch (e: TimeoutCancellationException) {
                otherServices.postValue(
                    Resource.error(
                        context.getString(R.string.timeout_error),
                        null
                    )
                )
            } catch (e: Exception) {
                if (e is IOException) {
                    otherServices.postValue(
                        Resource.error(
                            context.getString(R.string.network_error),
                            null
                        )
                    )
                } else {
                    otherServices.postValue(
                        Resource.error(
                            context.getString(R.string.something_went_error),
                            null
                        )
                    )
                }
                Log.e(TAG, "requestOtherService: ${e.message}")
            }
        }
    }

    fun requestService(order: RequestServicePost) {
        viewModelScope.launch {

            services.postValue(Resource.loading(null))
            try {
                withTimeout(20_000) {
                    val usersFromApi = apiHelper?.requestService(order)

                    if (usersFromApi!!.status && usersFromApi.code == 200)
                        services.postValue(Resource.success(usersFromApi))
                    else {
                        services.postValue(Resource.error(usersFromApi.message, null))
                    }
                }

            } catch (e: TimeoutCancellationException) {
                services.postValue(Resource.error(context.getString(R.string.timeout_error), null))
            } catch (e: Exception) {
                if (e is IOException) {
                    services.postValue(
                        Resource.error(
                            context.getString(R.string.network_error),
                            null
                        )
                    )
                } else {
                    services.postValue(
                        Resource.error(
                            context.getString(R.string.something_went_error),
                            null
                        )
                    )
                }
                Log.e(TAG, "requestService: ${e.message}")
            }
        }
    }


    fun storesFreeDelivery() {
        viewModelScope.launch {

            storesFreeDelivery.postValue(Resource.loading(null))
            try {
                withTimeout(20_000) {
                    val usersFromApi = apiHelper?.getStoresFreeDelivery()

                    if (usersFromApi!!.status && usersFromApi.code == 200)
                        storesFreeDelivery.postValue(Resource.success(usersFromApi))
                    else {
                        storesFreeDelivery.postValue(Resource.error(usersFromApi.message, null))
                    }
                }
            } catch (e: TimeoutCancellationException) {
                storesFreeDelivery.postValue(
                    Resource.error(
                        context.getString(R.string.timeout_error),
                        null
                    )
                )
            } catch (e: Exception) {
                if (e is IOException) {
                    storesFreeDelivery.postValue(
                        Resource.error(
                            context.getString(R.string.network_error),
                            null
                        )
                    )
                } else {
                    storesFreeDelivery.postValue(
                        Resource.error(
                            context.getString(R.string.something_went_error),
                            null
                        )
                    )
                }
                Log.e(TAG, "storesFreeDelivery: ${e.message}")
            }
        }
    }


    fun getClientOrders(): LiveData<Resource<GetClientOrders>> {
        return clientOrders
    }

    fun getClientOrdersDetails(): LiveData<Resource<GetClientOrderDetails>> {
        return clientOrderDetails
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

    fun getAllOtherService(): LiveData<Resource<RequestOtherService>> {
        return otherServices
    }

    fun getService(): LiveData<Resource<RequestOtherService>> {
        return services
    }

    fun getCancelOrder(): LiveData<Resource<GeneralResponse>> {
        return cancelOrder
    }
}