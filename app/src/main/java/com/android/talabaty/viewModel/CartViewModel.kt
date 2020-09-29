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
import kotlinx.coroutines.withTimeout
import java.io.IOException
import java.net.ConnectException

class CartViewModel(private val apiHelper: ApiHelper?, var context: Context) : ViewModel() {
    private val TAG = "CartViewModel"
    private val addCart = MutableLiveData<Resource<AddProductToCart>>()
    private val deleteCart = MutableLiveData<Resource<GeneralResponse>>()
    private val changeQuantity = MutableLiveData<Resource<ChangeQuantity>>()
    private val addToFav = MutableLiveData<Resource<ProductToFav>>()
    private val deleteFromFav = MutableLiveData<Resource<GeneralResponse>>()
    private val myCart = MutableLiveData<Resource<MyCart>>()
    private val myFav = MutableLiveData<Resource<FavProducts>>()
    private val checkout = MutableLiveData<Resource<Checkout>>()

    fun getCart(coupon: String? = null, userAddressId: Int = 0, deliveryMethod:Int = 0,paymentMethod:String? = null) {
        viewModelScope.launch {

            myCart.postValue(Resource.loading(null))
            try {
                withTimeout(20_000) {
                    val usersFromApi = apiHelper?.getMyCart(coupon,paymentMethod, deliveryMethod, userAddressId)

                    if (usersFromApi!!.status && usersFromApi.code == 200)
                        myCart.postValue(Resource.success(usersFromApi))
                    else {
                        myCart.postValue(Resource.error(usersFromApi.message, null))

                    }
                }

            } catch (e: TimeoutCancellationException) {
                myCart.postValue(Resource.error(context.getString(R.string.timeout_error), null))
            } catch (e: Exception) {
                if (e is IOException) {
                    myCart.postValue(
                        Resource.error(
                            context.getString(R.string.network_error),
                            null
                        )
                    )
                } else {
                    myCart.postValue(
                        Resource.error(
                            context.getString(R.string.something_went_error),
                            null
                        )
                    )
                }
                Log.e(TAG, "getCart: ${e.message}")
            }
        }
    }

    fun getFavorite() {
        viewModelScope.launch {

            myFav.postValue(Resource.loading(null))
            try {
                withTimeout(20_000) {
                    val usersFromApi = apiHelper?.getMyFavProducts()

                    if (usersFromApi!!.status && usersFromApi.code == 200)
                        myFav.postValue(Resource.success(usersFromApi))
                    else {
                        myFav.postValue(Resource.error(usersFromApi.message, null))

                    }

                }
            } catch (e: TimeoutCancellationException) {
                myFav.postValue(Resource.error(context.getString(R.string.timeout_error), null))
            } catch (e: Exception) {
                if (e is IOException) {
                    myFav.postValue(Resource.error(context.getString(R.string.network_error), null))
                } else {
                    myFav.postValue(
                        Resource.error(
                            context.getString(R.string.something_went_error),
                            null
                        )
                    )
                }
                Log.e(TAG, "getFavorite: ${e.message}")
            }
        }
    }


    fun addToCart(productId: Int, quantity: Int, color_id: Int, size_id: Int) {
        viewModelScope.launch {

            addCart.postValue(Resource.loading(null))
            try {
                withTimeout(20_000) {
                    val usersFromApi =
                        apiHelper?.addProductToCart(productId, quantity, color_id, size_id)

                    if (usersFromApi!!.status && usersFromApi.code == 200)
                        addCart.postValue(Resource.success(usersFromApi))
                    else {
                        addCart.postValue(Resource.error(usersFromApi.message, null))

                    }

                }
            } catch (e: TimeoutCancellationException) {
                addCart.postValue(Resource.error(context.getString(R.string.timeout_error), null))
            } catch (e: Exception) {
                if (e is IOException) {
                    addCart.postValue(
                        Resource.error(
                            context.getString(R.string.network_error),
                            null
                        )
                    )
                } else {
                    addCart.postValue(
                        Resource.error(
                            context.getString(R.string.something_went_error),
                            null
                        )
                    )
                }
                Log.e(TAG, "addToCart: ${e.message}")
            }
        }
    }

    fun deleteFromCart(productId: Int) {
        viewModelScope.launch {

            deleteCart.postValue(Resource.loading(null))
            try {
                withTimeout(20_000) {
                    val usersFromApi = apiHelper?.getDeleteFromCart(productId)
                    if (usersFromApi!!.status && usersFromApi.code == 200)
                        deleteCart.postValue(Resource.success(usersFromApi))
                    else {
                        deleteCart.postValue(Resource.error(usersFromApi.message, null))

                    }
                }
            } catch (e: TimeoutCancellationException) {
                deleteCart.postValue(
                    Resource.error(
                        context.getString(R.string.timeout_error),
                        null
                    )
                )
            } catch (e: Exception) {
                if (e is IOException) {
                    deleteCart.postValue(
                        Resource.error(
                            context.getString(R.string.network_error),
                            null
                        )
                    )
                } else {
                    deleteCart.postValue(
                        Resource.error(
                            context.getString(R.string.something_went_error),
                            null
                        )
                    )
                }
                Log.e(TAG, "deleteFromCart: ${e.message}")
            }
        }
    }

    fun deleteFromFav(productId: Int) {
        viewModelScope.launch {

            deleteFromFav.postValue(Resource.loading(null))
            try {
                withTimeout(20_000) {
                    val usersFromApi = apiHelper?.getDeleteProductFromFav(productId)
                    if (usersFromApi!!.status && usersFromApi.code == 200)
                        deleteFromFav.postValue(Resource.success(usersFromApi))
                    else {
                        deleteFromFav.postValue(Resource.error(usersFromApi.message, null))

                    }
                }
            } catch (e: TimeoutCancellationException) {
                deleteFromFav.postValue(
                    Resource.error(
                        context.getString(R.string.timeout_error),
                        null
                    )
                )
            } catch (e: Exception) {
                if (e is IOException) {
                    deleteFromFav.postValue(
                        Resource.error(
                            context.getString(R.string.network_error),
                            null
                        )
                    )
                } else {
                    deleteFromFav.postValue(
                        Resource.error(
                            context.getString(R.string.something_went_error),
                            null
                        )
                    )
                }
                Log.e(TAG, "deleteFromFav: ${e.message}")
            }
        }
    }

    fun addToFav(productId: Int) {
        viewModelScope.launch {

            addToFav.postValue(Resource.loading(null))
            try {
                withTimeout(20_000) {
                    val usersFromApi = apiHelper?.getAddProductToFav(productId)
                    if (usersFromApi!!.status && usersFromApi.code == 200)
                        addToFav.postValue(Resource.success(usersFromApi))
                    else {
                        addToFav.postValue(Resource.error(usersFromApi.message, null))

                    }
                }
            } catch (e: TimeoutCancellationException) {
                addToFav.postValue(Resource.error(context.getString(R.string.timeout_error), null))
            } catch (e: Exception) {
                if (e is IOException) {
                    addToFav.postValue(
                        Resource.error(
                            context.getString(R.string.network_error),
                            null
                        )
                    )
                } else {
                    addToFav.postValue(
                        Resource.error(
                            context.getString(R.string.something_went_error),
                            null
                        )
                    )
                }
                Log.e(TAG, "addToFav: ${e.message}")
            }
        }
    }


    fun changeQuantity(productId: Int, type: String) {
        viewModelScope.launch {

            changeQuantity.postValue(Resource.loading(null))
            try {
                withTimeout(20_000) {
                    val usersFromApi = apiHelper?.changeQuantity(productId, type)
                    if (usersFromApi!!.status && usersFromApi.code == 200)
                        changeQuantity.postValue(Resource.success(usersFromApi))
                    else {
                        changeQuantity.postValue(Resource.error(usersFromApi.message, null))

                    }
                }
            } catch (e: TimeoutCancellationException) {
                changeQuantity.postValue(
                    Resource.error(
                        context.getString(R.string.timeout_error),
                        null
                    )
                )
            } catch (e: Exception) {
                if (e is IOException) {
                    changeQuantity.postValue(
                        Resource.error(
                            context.getString(R.string.network_error),
                            null
                        )
                    )
                } else {
                    changeQuantity.postValue(
                        Resource.error(
                            context.getString(R.string.something_went_error),
                            null
                        )
                    )
                }
                Log.e(TAG, "changeQuantity: ${e.message}")
            }
        }
    }

    fun checkout(coupon: String? = null, userAddressId: Int = 0, deliveryMethod:Int ,paymentMethod:String?) {
        viewModelScope.launch {

            checkout.postValue(Resource.loading(null))
            try {
                withTimeout(20_000) {
                    val usersFromApi = apiHelper?.checkout(coupon,paymentMethod, deliveryMethod, userAddressId)

                    if (usersFromApi!!.status && usersFromApi.code == 200)
                        checkout.postValue(Resource.success(usersFromApi))
                    else {
                        checkout.postValue(Resource.error(usersFromApi.message, null))

                    }
                }

            } catch (e: TimeoutCancellationException) {
                checkout.postValue(Resource.error(context.getString(R.string.timeout_error), null))
            } catch (e: Exception) {
                if (e is IOException) {
                    checkout.postValue(
                        Resource.error(
                            context.getString(R.string.network_error),
                            null
                        )
                    )
                } else {
                    checkout.postValue(
                        Resource.error(
                            context.getString(R.string.something_went_error),
                            null
                        )
                    )
                }
                Log.e(TAG, "checkout: ${e.message}")
            }
        }
    }



    fun getAddToCart(): LiveData<Resource<AddProductToCart>> {
        return addCart
    }

    fun getDeleteToCart(): LiveData<Resource<GeneralResponse>> {
        return deleteCart
    }

    fun getChangeQuantity(): LiveData<Resource<ChangeQuantity>> {
        return changeQuantity
    }

    fun getDeleteFav(): LiveData<Resource<GeneralResponse>> {
        return deleteFromFav
    }

    fun getAddToFav(): LiveData<Resource<ProductToFav>> {
        return addToFav
    }

    fun getAllCart(): LiveData<Resource<MyCart>> {
        return myCart
    }

    fun getAllFavorite(): LiveData<Resource<FavProducts>> {
        return myFav
    }

    fun getCheckout(): LiveData<Resource<Checkout>> {
        return checkout
    }

}