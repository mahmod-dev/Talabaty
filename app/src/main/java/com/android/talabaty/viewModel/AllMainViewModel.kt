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

class AllMainViewModel(private val apiHelper: ApiHelper?, var context: Context) :
    ViewModel() {
    private val TAG = "AllMainViewModel"
    private val homePageCategories = MutableLiveData<Resource<HomePageCategories>>()
    private val adds = MutableLiveData<Resource<Ads>>()
    private val offers = MutableLiveData<Resource<GetOffers>>()
    private val searchProducts = MutableLiveData<Resource<SearchProduct>>()
    private val searchOffers = MutableLiveData<Resource<SearchOffer>>()


    fun homePageCategories() {
            viewModelScope.launch {

            homePageCategories.postValue(Resource.loading(null))
            try {
                withTimeout(20_000) {

                    val usersFromApi = apiHelper?.getHomePageCategories()

                    if (usersFromApi!!.status && usersFromApi.code == 200)
                        homePageCategories.postValue(Resource.success(usersFromApi))
                    else {
                        homePageCategories.postValue(Resource.error(usersFromApi.message, null))
                    }
                }

            } catch (e: TimeoutCancellationException) {
                homePageCategories.postValue(
                    Resource.error(
                        context.getString(R.string.timeout_error),
                        null
                    )
                )
            } catch (e: Exception) {
                if (e is IOException) {
                    homePageCategories.postValue(
                        Resource.error(
                            context.getString(R.string.network_error),
                            null
                        )
                    )
                } else if (e is TimeoutCancellationException) {
                    homePageCategories.postValue(
                        Resource.error(
                            context.getString(R.string.timeout_error),
                            null
                        )
                    )
                } else {
                    homePageCategories.postValue(
                        Resource.error(
                            context.getString(R.string.something_went_error),
                            null
                        )
                    )
                }
                Log.e(TAG, "homePageCategories: ${e.localizedMessage}")
            }
        }
    }

    fun allAdds() {
        viewModelScope.launch {

            adds.postValue(Resource.loading(null))
            try {
                withTimeout(20_000) {

                    val usersFromApi = apiHelper?.getAds()

                    if (usersFromApi!!.status && usersFromApi.code == 200)
                        adds.postValue(Resource.success(usersFromApi))
                    else {
                        adds.postValue(Resource.error(usersFromApi.message, null))

                    }
                }

            } catch (e: TimeoutCancellationException) {
                adds.postValue(Resource.error(context.getString(R.string.timeout_error), null))
            } catch (e: Exception) {
                if (e is IOException) {
                    adds.postValue(Resource.error(context.getString(R.string.network_error), null))
                } else if (e is TimeoutCancellationException) {
                    adds.postValue(Resource.error(context.getString(R.string.timeout_error), null))

                } else {
                    adds.postValue(
                        Resource.error(
                            context.getString(R.string.something_went_error),
                            null
                        )
                    )
                }
                Log.e(TAG, "allAdds: ${e.message}")
            }
        }
    }

    fun allOffers() {
        viewModelScope.launch {

            offers.postValue(Resource.loading(null))
            try {
                withTimeout(20_000) {
                    val usersFromApi = apiHelper?.getOffers()
                    if (usersFromApi!!.status && usersFromApi.code == 200)
                        offers.postValue(Resource.success(usersFromApi))
                    else {
                        offers.postValue(Resource.error(usersFromApi.message, null))

                    }
                }
            } catch (e: TimeoutCancellationException) {
                offers.postValue(Resource.error(context.getString(R.string.timeout_error), null))
            } catch (e: Exception) {
                if (e is IOException) {
                    offers.postValue(
                        Resource.error(
                            context.getString(R.string.network_error),
                            null
                        )
                    )

                } else if (e is TimeoutCancellationException) {
                    offers.postValue(
                        Resource.error(
                            context.getString(R.string.timeout_error),
                            null
                        )
                    )

                } else {
                    offers.postValue(
                        Resource.error(
                            context.getString(R.string.something_went_error),
                            null
                        )
                    )
                }
                Log.e(TAG, "allOffers: ${e.message}")
            }
        }
    }

    fun searchProducts(text:String) {
        viewModelScope.launch {

            searchProducts.postValue(Resource.loading(null))
            try {
                withTimeout(20_000) {
                    val usersFromApi = apiHelper?.searchProducts(text)
                    if (usersFromApi!!.status && usersFromApi.code == 200)
                        searchProducts.postValue(Resource.success(usersFromApi))
                    else {
                        searchProducts.postValue(Resource.error(usersFromApi.message, null))

                    }
                }
            } catch (e: TimeoutCancellationException) {
                searchProducts.postValue(Resource.error(context.getString(R.string.timeout_error), null))
            } catch (e: Exception) {
                if (e is IOException) {
                    searchProducts.postValue(
                        Resource.error(
                            context.getString(R.string.network_error),
                            null
                        )
                    )

                } else if (e is TimeoutCancellationException) {
                    searchProducts.postValue(
                        Resource.error(
                            context.getString(R.string.timeout_error),
                            null
                        )
                    )

                } else {
                    searchProducts.postValue(
                        Resource.error(
                            context.getString(R.string.something_went_error),
                            null
                        )
                    )
                }
                Log.e(TAG, "searchProducts: ${e.message}")
            }
        }
    }

    fun searchOffers(text:String) {
        viewModelScope.launch {

            searchOffers.postValue(Resource.loading(null))
            try {
                withTimeout(20_000) {
                    val usersFromApi = apiHelper?.searchOffers(text)
                    if (usersFromApi!!.status && usersFromApi.code == 200)
                        searchOffers.postValue(Resource.success(usersFromApi))
                    else {
                        searchOffers.postValue(Resource.error(usersFromApi.message, null))

                    }
                }
            } catch (e: TimeoutCancellationException) {
                searchOffers.postValue(Resource.error(context.getString(R.string.timeout_error), null))
            } catch (e: Exception) {
                if (e is IOException) {
                    searchOffers.postValue(
                        Resource.error(
                            context.getString(R.string.network_error),
                            null
                        )
                    )

                } else if (e is TimeoutCancellationException) {
                    searchOffers.postValue(
                        Resource.error(
                            context.getString(R.string.timeout_error),
                            null
                        )
                    )

                } else {
                    searchOffers.postValue(
                        Resource.error(
                            context.getString(R.string.something_went_error),
                            null
                        )
                    )
                }
                Log.e(TAG, "searchOffers: ${e.message}")
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


    fun getSearchProducts(): LiveData<Resource<SearchProduct>> {
        return searchProducts
    }

    fun getSearchOffers(): LiveData<Resource<SearchOffer>> {
        return searchOffers
    }

}