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

class StoreDetailsViewModel(private val apiHelper: ApiHelper?, var context: Context) :
    ViewModel() {
    private val TAG = "StoreDetailsViewModel"
    private val categories = MutableLiveData<Resource<Categories>>()
    private val viewStoresProduct = MutableLiveData<Resource<StoreProducts>>()
    private val productDetails = MutableLiveData<Resource<ProductDetails>>()



     fun storeCategories(storeId: Int) {
        viewModelScope.launch {

            categories.postValue(Resource.loading(null))
            try {
                withTimeout(20_000) {
                    val usersFromApi = apiHelper?.getCategories(storeId)

                    if (usersFromApi!!.status && usersFromApi.code == 200)
                        categories.postValue(Resource.success(usersFromApi))
                    else {
                        categories.postValue(Resource.error(usersFromApi.message, null))
                    }
                }
            } catch (e: TimeoutCancellationException) {
                categories.postValue(Resource.error(context.getString(R.string.timeout_error), null))
            } catch (e: Exception) {
                if (e is IOException) {
                    categories.postValue(Resource.error(context.getString(R.string.network_error), null))
                } else {
                    categories.postValue(Resource.error(context.getString(R.string.something_went_error), null))
                }
                Log.e(TAG, "storeCategories: ${e.message}")
            }
        }
    }

    public fun viewStoreProduct(storeId: Int, categoryId: Int) {
        viewModelScope.launch {

            viewStoresProduct.postValue(Resource.loading(null))
            try {
                withTimeout(20_000) {
                //  async {
                val usersFromApi = apiHelper?.getViewStoreProducts(storeId, categoryId)
                if (usersFromApi!!.status && usersFromApi.code == 200)
                    viewStoresProduct.postValue(Resource.success(usersFromApi))
                else {
                    viewStoresProduct.postValue(Resource.error(usersFromApi.message, null))
                }

                // }.await()
            }
            }catch (e: TimeoutCancellationException) {
                viewStoresProduct.postValue(Resource.error(context.getString(R.string.timeout_error), null))
            } catch (e: Exception) {
                if (e is IOException) {
                    viewStoresProduct.postValue(Resource.error(context.getString(R.string.network_error), null))
                } else {
                    viewStoresProduct.postValue(Resource.error(context.getString(R.string.something_went_error), null))
                }
                Log.e(TAG, "viewStoreProduct: ${e.message}")
            }
        }
    }

    public fun productDetails(productId: Int) {
        viewModelScope.launch {

            productDetails.postValue(Resource.loading(null))
            try {
                withTimeout(20_000) {
                    val usersFromApi = apiHelper?.getProductDetails(productId)
                    if (usersFromApi!!.status && usersFromApi.code == 200)
                        productDetails.postValue(Resource.success(usersFromApi))
                    else {
                        productDetails.postValue(Resource.error(usersFromApi.message, null))
                    }

                }
            }catch (e: TimeoutCancellationException) {
                productDetails.postValue(Resource.error(context.getString(R.string.timeout_error), null))
            } catch (e: Exception) {
                if (e is IOException) {
                    productDetails.postValue(Resource.error(context.getString(R.string.network_error), null))
                } else {
                    productDetails.postValue(Resource.error(context.getString(R.string.something_went_error), null))
                }
                Log.e(TAG, "productDetails: ${e.message}")
            }
        }
    }



    fun getAllCategory(): LiveData<Resource<Categories>> {
        return categories
    }

    fun getStoresById(): LiveData<Resource<StoreProducts>> {
        return viewStoresProduct
    }

    fun getProductDetails(): LiveData<Resource<ProductDetails>> {
        return productDetails
    }
}