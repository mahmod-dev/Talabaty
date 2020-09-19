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
import java.io.IOException

class CouponViewModel(private val apiHelper: ApiHelper?, var context: Context) :
   ViewModel() {
    private val TAG = "CouponViewModel"
    private val getCoupon = MutableLiveData<Resource<GetMyCoupons>>()
    private val addCoupon = MutableLiveData<Resource<AddNewCoupon>>()
    private val deleteCoupon = MutableLiveData<Resource<GeneralResponse>>()
    private val chargeWallet = MutableLiveData<Resource<ChargeWallet>>()


     fun allCoupons() {
        viewModelScope.launch {

            getCoupon.postValue(Resource.loading(null))
            try {
                val usersFromApi = apiHelper?.getMyCoupons()

                if (usersFromApi!!.status && usersFromApi.code == 200)
                    getCoupon.postValue(Resource.success(usersFromApi))
                else {
                    getCoupon.postValue(Resource.error(usersFromApi.message, null))
                }


            } catch (e: TimeoutCancellationException) {
                getCoupon.postValue(Resource.error(context.getString(R.string.timeout_error), null))
            } catch (e: Exception) {
                if (e is IOException) {
                    getCoupon.postValue(Resource.error(context.getString(R.string.network_error), null))
                } else {
                    getCoupon.postValue(Resource.error(context.getString(R.string.something_went_error), null))
                }
                Log.e(TAG, "allCoupons: ${e.message}")
            }
        }
    }

    fun addCoupon(coupon: String) {
        viewModelScope.launch {

            addCoupon.postValue(Resource.loading(null))
            try {
                val usersFromApi = apiHelper?.addNewCoupon(coupon)

                if (usersFromApi!!.status && usersFromApi.code == 200)
                    addCoupon.postValue(Resource.success(usersFromApi))
                else {
                    addCoupon.postValue(Resource.error(usersFromApi.message, null))

                }
            } catch (e: Exception) {
                addCoupon.postValue(Resource.error(context.getString(R.string.something_went_error), null))
                Log.e(TAG, "addCoupon: ${e.message}")
            }
        }
    }

    fun deleteCoupon(couponId: Int) {
        viewModelScope.launch {

            deleteCoupon.postValue(Resource.loading(null))
            try {

                val usersFromApi = apiHelper?.deleteCoupon(couponId)
                if (usersFromApi!!.status && usersFromApi.code == 200)
                    deleteCoupon.postValue(Resource.success(usersFromApi))
                else {
                    deleteCoupon.postValue(Resource.error(usersFromApi.message, null))

                }

            } catch (e: TimeoutCancellationException) {
                deleteCoupon.postValue(Resource.error(context.getString(R.string.timeout_error), null))
            } catch (e: Exception) {
                if (e is IOException) {
                    deleteCoupon.postValue(Resource.error(context.getString(R.string.network_error), null))

                } else {
                    deleteCoupon.postValue(Resource.error(context.getString(R.string.something_went_error), null))
                }
                Log.e(TAG, "deleteCoupon: ${e.message}")
            }
        }
    }

    fun chargeWallet(amount: Int) {
        viewModelScope.launch {

            chargeWallet.postValue(Resource.loading(null))
            try {

                val usersFromApi = apiHelper?.chargeWallet(amount)
                if (usersFromApi!!.status && usersFromApi.code == 200)
                    chargeWallet.postValue(Resource.success(usersFromApi))
                else {
                    chargeWallet.postValue(Resource.error(usersFromApi.message, null))
                }

            } catch (e: TimeoutCancellationException) {
                chargeWallet.postValue(Resource.error(context.getString(R.string.timeout_error), null))
            } catch (e: Exception) {
                if (e is IOException) {
                    chargeWallet.postValue(Resource.error(context.getString(R.string.network_error), null))

                } else {
                    chargeWallet.postValue(Resource.error(context.getString(R.string.something_went_error), null))
                }
                Log.e(TAG, "chargeWallet: ${e.message}")
            }
        }
    }



    fun getAllCoupons(): LiveData<Resource<GetMyCoupons>> {
        return getCoupon
    }

    fun getAddCoupon(): LiveData<Resource<AddNewCoupon>> {
        return addCoupon
    }
    fun getDeleteCoupon(): LiveData<Resource<GeneralResponse>> {
        return deleteCoupon
    }
    fun getChargeWallet(): LiveData<Resource<ChargeWallet>> {
        return chargeWallet
    }

}