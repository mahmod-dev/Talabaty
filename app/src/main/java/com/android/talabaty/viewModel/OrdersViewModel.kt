package com.android.talabaty.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.android.talabaty.model.*
import com.android.talabaty.retrofit.ApiHelper
import com.android.talabaty.dbUtil.Resource
import kotlinx.coroutines.launch

class OrdersViewModel(private val apiHelper: ApiHelper?, application: Application) :
    AndroidViewModel(application) {
    private val TAG = "OrdersViewModel"
    private val orders = MutableLiveData<Resource<MyOrders>>()
    private val storesFreeDelivery = MutableLiveData<Resource<StoresFreeDelivery>>()
    private val newOrder = MutableLiveData<Resource<CreateNewOrder>>()


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


            } catch (e: Exception) {
                Log.e(TAG, "homePageCategories: ${e.message}")
                orders.postValue(Resource.error("Something Went Wrong", null))
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


            } catch (e: Exception) {
                Log.e(TAG, "homePageCategories: ${e.message}")
                newOrder.postValue(Resource.error("Something Went Wrong", null))
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


            } catch (e: Exception) {
                Log.e(TAG, "storesFreeDelivery: ${e.message}")
                storesFreeDelivery.postValue(Resource.error("Something Went Wrong", null))
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


}