package com.android.talabaty.viewModel

import android.util.Log
import androidx.lifecycle.*
import com.android.talabaty.model.*
import com.android.talabaty.retrofit.ApiHelper
import com.android.talabaty.dbUtil.Resource
import kotlinx.coroutines.launch

class CartViewModel(private val apiHelper: ApiHelper?) : ViewModel() {
    private val TAG = "CartViewModel"
    private val addCart = MutableLiveData<Resource<AddProductToCart>>()
    private val deleteCart = MutableLiveData<Resource<GeneralResponse>>()
    private val changeQuantity = MutableLiveData<Resource<ChangeQuantity>>()
    private val addToFav = MutableLiveData<Resource<ProductToFav>>()
    private val deleteFromFav = MutableLiveData<Resource<GeneralResponse>>()
    private val myCart = MutableLiveData<Resource<MyCart>>()
    private val myFav = MutableLiveData<Resource<FavProducts>>()

    fun getCart() {
        viewModelScope.launch {

            myCart.postValue(Resource.loading(null))
            try {
                val usersFromApi = apiHelper?.getMyCart()

                if (usersFromApi!!.status && usersFromApi.code == 200)
                    myCart.postValue(Resource.success(usersFromApi))
                else {
                    myCart.postValue(Resource.error(usersFromApi.message, null))

                }


            } catch (e: Exception) {
                Log.e(TAG, "getCart: ${e.message}")
                myCart.postValue(Resource.error("Something Went Wrong", null))
            }
        }
    }

    fun getFavorite() {
        viewModelScope.launch {

            myFav.postValue(Resource.loading(null))
            try {
                val usersFromApi = apiHelper?.getMyFavProducts()

                if (usersFromApi!!.status && usersFromApi.code == 200)
                    myFav.postValue(Resource.success(usersFromApi))
                else {
                    myFav.postValue(Resource.error(usersFromApi.message, null))

                }


            } catch (e: Exception) {
                Log.e(TAG, "getFavorite: ${e.message}")
                myFav.postValue(Resource.error("Something Went Wrong", null))
            }
        }
    }


    fun addToCart(productId: Int, quantity: Int) {
        viewModelScope.launch {

            addCart.postValue(Resource.loading(null))
            try {
                val usersFromApi = apiHelper?.addProductToCart(productId, quantity)

                if (usersFromApi!!.status && usersFromApi.code == 200)
                    addCart.postValue(Resource.success(usersFromApi))
                else {
                    addCart.postValue(Resource.error(usersFromApi.message, null))

                }


            } catch (e: Exception) {
                Log.e(TAG, "addToCart: ${e.message}")
                addCart.postValue(Resource.error("Something Went Wrong", null))
            }
        }
    }

    fun deleteFromCart(productId: Int) {
        viewModelScope.launch {

            deleteCart.postValue(Resource.loading(null))
            try {

                val usersFromApi = apiHelper?.getDeleteFromCart(productId)
                if (usersFromApi!!.status && usersFromApi.code == 200)
                    deleteCart.postValue(Resource.success(usersFromApi))
                else {
                    deleteCart.postValue(Resource.error(usersFromApi.message, null))

                }

            } catch (e: Exception) {
                Log.e(TAG, "deleteFromCart: ${e.message}")
                deleteCart.postValue(Resource.error("Something Went Wrong", null))
            }
        }
    }

    fun deleteFromFav(productId: Int) {
        viewModelScope.launch {

            deleteFromFav.postValue(Resource.loading(null))
            try {

                val usersFromApi = apiHelper?.getDeleteProductFromFav(productId)
                if (usersFromApi!!.status && usersFromApi.code == 200)
                    deleteFromFav.postValue(Resource.success(usersFromApi))
                else {
                    deleteFromFav.postValue(Resource.error(usersFromApi.message, null))

                }

            } catch (e: Exception) {
                Log.e(TAG, "deleteFromFav: ${e.message}")
                deleteFromFav.postValue(Resource.error("Something Went Wrong", null))
            }
        }
    }

    fun addToFav(productId: Int) {
        viewModelScope.launch {

            addToFav.postValue(Resource.loading(null))
            try {

                val usersFromApi = apiHelper?.getAddProductToFav(productId)
                if (usersFromApi!!.status && usersFromApi.code == 200)
                    addToFav.postValue(Resource.success(usersFromApi))
                else {
                    addToFav.postValue(Resource.error(usersFromApi.message, null))

                }

            } catch (e: Exception) {
                Log.e(TAG, "addToFav: ${e.message}")
                addToFav.postValue(Resource.error("Something Went Wrong", null))
            }
        }
    }


    fun changeQuantity(productId: Int, type: String) {
        viewModelScope.launch {

            changeQuantity.postValue(Resource.loading(null))
            try {

                val usersFromApi = apiHelper?.changeQuantity(productId, type)
                if (usersFromApi!!.status && usersFromApi.code == 200)
                    changeQuantity.postValue(Resource.success(usersFromApi))
                else {
                    changeQuantity.postValue(Resource.error(usersFromApi.message, null))

                }

            } catch (e: Exception) {
                Log.e(TAG, "changeQuantity: ${e.message}")
                changeQuantity.postValue(Resource.error("Something Went Wrong", null))
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
}