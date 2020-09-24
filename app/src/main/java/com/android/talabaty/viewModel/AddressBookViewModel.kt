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

class AddressBookViewModel(private val apiHelper: ApiHelper?, var context: Context) :
    ViewModel() {
    private val TAG = "AddressBookViewModel"
    private val allAddress = MutableLiveData<Resource<GetAllBookAddress>>()
    private val addAddress = MutableLiveData<Resource<AddNewAddress>>()
    private val editAddress = MutableLiveData<Resource<GeneralResponse>>()
    private val deleteAddress = MutableLiveData<Resource<GeneralResponse>>()


    fun allAddress() {
        viewModelScope.launch {

            allAddress.postValue(Resource.loading(null))
            try {
                withTimeout(20_000) {

                    val usersFromApi = apiHelper?.getMyAddresses()

                    if (usersFromApi!!.status && usersFromApi.code == 200)
                        allAddress.postValue(Resource.success(usersFromApi))
                    else {
                        allAddress.postValue(Resource.error(usersFromApi.message, null))
                    }
                }

            } catch (e: TimeoutCancellationException) {
                allAddress.postValue(
                    Resource.error(
                        context.getString(R.string.timeout_error),
                        null
                    )
                )
            } catch (e: Exception) {
                if (e is IOException) {
                    allAddress.postValue(
                        Resource.error(
                            context.getString(R.string.network_error),
                            null
                        )
                    )
                } else if (e is TimeoutCancellationException) {
                    allAddress.postValue(
                        Resource.error(
                            context.getString(R.string.timeout_error),
                            null
                        )
                    )
                } else {
                    allAddress.postValue(
                        Resource.error(
                            context.getString(R.string.something_went_error),
                            null
                        )
                    )
                }
                Log.e(TAG, "allAddress: ${e.localizedMessage}")
            }
        }
    }

    fun addAddress(lat: Double, lng: Double, address: String) {
        viewModelScope.launch {

            addAddress.postValue(Resource.loading(null))
            try {
                withTimeout(20_000) {

                    val usersFromApi = apiHelper?.addNewAddress(lat, lng, address)

                    if (usersFromApi!!.status && usersFromApi.code == 200)
                        addAddress.postValue(Resource.success(usersFromApi))
                    else {
                        addAddress.postValue(Resource.error(usersFromApi.message, null))
                    }
                }

            } catch (e: TimeoutCancellationException) {
                addAddress.postValue(
                    Resource.error(
                        context.getString(R.string.timeout_error),
                        null
                    )
                )
            } catch (e: Exception) {
                if (e is IOException) {
                    addAddress.postValue(
                        Resource.error(
                            context.getString(R.string.network_error),
                            null
                        )
                    )
                } else if (e is TimeoutCancellationException) {
                    addAddress.postValue(
                        Resource.error(
                            context.getString(R.string.timeout_error),
                            null
                        )
                    )
                } else {
                    addAddress.postValue(
                        Resource.error(
                            context.getString(R.string.something_went_error),
                            null
                        )
                    )
                }
                Log.e(TAG, "addAddress: ${e.localizedMessage}")
            }
        }
    }


    fun editAddress(addressId: Int, lat: Double, lng: Double, address: String) {
        viewModelScope.launch {

            editAddress.postValue(Resource.loading(null))
            try {
                withTimeout(20_000) {

                    val usersFromApi = apiHelper?.editMyAddress(addressId, lat, lng, address)

                    if (usersFromApi!!.status && usersFromApi.code == 200)
                        editAddress.postValue(Resource.success(usersFromApi))
                    else {
                        editAddress.postValue(Resource.error(usersFromApi.message, null))
                    }
                }

            } catch (e: TimeoutCancellationException) {
                editAddress.postValue(
                    Resource.error(
                        context.getString(R.string.timeout_error),
                        null
                    )
                )
            } catch (e: Exception) {
                if (e is IOException) {
                    editAddress.postValue(
                        Resource.error(
                            context.getString(R.string.network_error),
                            null
                        )
                    )
                } else if (e is TimeoutCancellationException) {
                    editAddress.postValue(
                        Resource.error(
                            context.getString(R.string.timeout_error),
                            null
                        )
                    )
                } else {
                    editAddress.postValue(
                        Resource.error(
                            context.getString(R.string.something_went_error),
                            null
                        )
                    )
                }
                Log.e(TAG, "editAddress: ${e.localizedMessage}")
            }
        }
    }


    fun deleteAddress(addressId: Int) {
        viewModelScope.launch {

            deleteAddress.postValue(Resource.loading(null))
            try {
                withTimeout(20_000) {

                    val usersFromApi = apiHelper?.deleteMyAddress(addressId)

                    if (usersFromApi!!.status && usersFromApi.code == 200)
                        deleteAddress.postValue(Resource.success(usersFromApi))
                    else {
                        deleteAddress.postValue(Resource.error(usersFromApi.message, null))
                    }
                }

            } catch (e: TimeoutCancellationException) {
                deleteAddress.postValue(
                    Resource.error(
                        context.getString(R.string.timeout_error),
                        null
                    )
                )
            } catch (e: Exception) {
                if (e is IOException) {
                    deleteAddress.postValue(
                        Resource.error(
                            context.getString(R.string.network_error),
                            null
                        )
                    )
                } else if (e is TimeoutCancellationException) {
                    deleteAddress.postValue(
                        Resource.error(
                            context.getString(R.string.timeout_error),
                            null
                        )
                    )
                } else {
                    deleteAddress.postValue(
                        Resource.error(
                            context.getString(R.string.something_went_error),
                            null
                        )
                    )
                }
                Log.e(TAG, "deleteAddress: ${e.localizedMessage}")
            }
        }
    }


    fun getAllAddress(): LiveData<Resource<GetAllBookAddress>> {
        return allAddress
    }

    fun getAddAddress(): LiveData<Resource<AddNewAddress>> {
        return addAddress
    }

    fun getEditAddress(): LiveData<Resource<GeneralResponse>> {
        return editAddress
    }


    fun getDeleteAddress(): LiveData<Resource<GeneralResponse>> {
        return deleteAddress
    }

}