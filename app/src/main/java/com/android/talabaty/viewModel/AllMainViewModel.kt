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

class AllMainViewModel(private val apiHelper: ApiHelper?, var context: Context) :
   ViewModel() {
    private val TAG = "AllMainViewModel"
    private val homePageCategories = MutableLiveData<Resource<HomePageCategories>>()
    private val adds = MutableLiveData<Resource<Ads>>()
    private val offers = MutableLiveData<Resource<GetOffers>>()


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


            } catch (e: TimeoutCancellationException) {
                homePageCategories.postValue(Resource.error(context.getString(R.string.timeout_error), null))
            } catch (e: Exception) {
                if (e is IOException) {
                    homePageCategories.postValue(Resource.error(context.getString(R.string.network_error), null))
                } else {
                    homePageCategories.postValue(Resource.error(context.getString(R.string.something_went_error), null))
                }
                Log.e(TAG, "homePageCategories: ${e.message}")
            }
        }
    }

    fun allAdds() {
        viewModelScope.launch {

            adds.postValue(Resource.loading(null))
            try {
                val usersFromApi = apiHelper?.getAds()

                if (usersFromApi!!.status && usersFromApi.code == 200)
                    adds.postValue(Resource.success(usersFromApi))
                else {
                    adds.postValue(Resource.error(usersFromApi.message, null))

                }


            } catch (e: TimeoutCancellationException) {
                adds.postValue(Resource.error(context.getString(R.string.timeout_error), null))
            } catch (e: Exception) {
                if (e is IOException) {
                    adds.postValue(Resource.error(context.getString(R.string.network_error), null))
                } else {
                    adds.postValue(Resource.error(context.getString(R.string.something_went_error), null))
                }
                Log.e(TAG, "allAdds: ${e.message}")
            }
        }
    }

    fun allOffers() {
        viewModelScope.launch {

            offers.postValue(Resource.loading(null))
            try {

                val usersFromApi = apiHelper?.getOffers()
                if (usersFromApi!!.status && usersFromApi.code == 200)
                    offers.postValue(Resource.success(usersFromApi))
                else {
                    offers.postValue(Resource.error(usersFromApi.message, null))

                }

            } catch (e: TimeoutCancellationException) {
                offers.postValue(Resource.error(context.getString(R.string.timeout_error), null))
            } catch (e: Exception) {
                if (e is IOException) {
                    offers.postValue(Resource.error(context.getString(R.string.network_error), null))

                } else {
                    offers.postValue(Resource.error(context.getString(R.string.something_went_error), null))
                }
                Log.e(TAG, "allOffers: ${e.message}")
            }
        }
    }





    fun getHomePageCategories(): LiveData<Resource<HomePageCategories>> {
        return homePageCategories
    }

    fun getAdds(): LiveData<Resource<Ads>> {
        return adds
    }
    fun getAllOffers(): LiveData<Resource<GetOffers>> {
        return offers
    }

}