package com.android.talabaty.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.android.talabaty.model.*
import com.android.talabaty.retrofit.ApiHelper
import com.android.talabaty.dbUtil.Resource
import kotlinx.coroutines.launch

class AllMainViewModel(private val apiHelper: ApiHelper?, application: Application) :
    AndroidViewModel(application) {
    private val TAG = "AllMainViewModel"
    private val homePageCategories = MutableLiveData<Resource<HomePageCategories>>()


     fun homePageCategories() {
        viewModelScope.launch {

            homePageCategories.postValue(Resource.loading(null))
            try {
                val usersFromApi = apiHelper?.getHomePageCategories()

                if (usersFromApi!!.status && usersFromApi.code == 200)
                    homePageCategories.postValue(Resource.success(usersFromApi))
                else {
                    homePageCategories.postValue(Resource.error(usersFromApi.message, null))

                }


            } catch (e: Exception) {
                Log.e(TAG, "homePageCategories: ${e.message}")
                homePageCategories.postValue(Resource.error("Something Went Wrong", null))
            }
        }
    }



    fun getHomePageCategories(): LiveData<Resource<HomePageCategories>> {
        return homePageCategories
    }


}