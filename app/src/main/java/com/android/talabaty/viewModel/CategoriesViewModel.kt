package com.android.talabaty.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.android.talabaty.model.*
import com.android.talabaty.retrofit.ApiHelper
import com.android.talabaty.dbUtil.Resource
import kotlinx.coroutines.launch

class CategoriesViewModel(private val apiHelper: ApiHelper?, application: Application) :
    AndroidViewModel(application) {
    private val TAG = "CategoriesViewModel"
    private val categories = MutableLiveData<Resource<Categories>>()
    private val viewStoresProduct = MutableLiveData<Resource<StoreProducts>>()



     fun storeCategories(storeId: Int) {
        viewModelScope.launch {

            categories.postValue(Resource.loading(null))
            try {
                val usersFromApi = apiHelper?.getCategories(storeId)

                if (usersFromApi!!.status && usersFromApi.code == 200)
                    categories.postValue(Resource.success(usersFromApi))
                else{
                    categories.postValue(Resource.error(usersFromApi.message, null))

                }



            } catch (e: Exception) {
                Log.e(TAG, "categories: ${e.message}")
                categories.postValue(Resource.error("Something Went Wrong", null))
            }
        }
    }

    public fun viewStoreProduct(storeId: Int, categoryId: Int) {
        viewModelScope.launch {

            viewStoresProduct.postValue(Resource.loading(null))
            try {
                //  async {
                val usersFromApi = apiHelper?.getViewStoreProducts(storeId, categoryId)
                if (usersFromApi!!.status && usersFromApi.code == 200)
                    viewStoresProduct.postValue(Resource.success(usersFromApi))
                else{
                    viewStoresProduct.postValue(Resource.error(usersFromApi.message, null))

                }

                // }.await()


            } catch (e: Exception) {
                Log.e(TAG, "viewStoresProduct: ${e.message}")
                viewStoresProduct.postValue(Resource.error("Something Went Wrong", null))
            }
        }
    }


    fun getAllCategory(): LiveData<Resource<Categories>> {
        return categories
    }

    fun getStoresById(): LiveData<Resource<StoreProducts>> {
        return viewStoresProduct
    }
}