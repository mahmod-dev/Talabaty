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

class CardPaymentViewModel(private val apiHelper: ApiHelper?, var context: Context) :
   ViewModel() {
    private val TAG = "CardPaymentViewModel"
    private val addCard = MutableLiveData<Resource<AddPaymentCard>>()
    private val editCard = MutableLiveData<Resource<AddPaymentCard>>()
    private val deleteCard = MutableLiveData<Resource<GeneralResponse>>()
    private val paymentMethod = MutableLiveData<Resource<GetPaymentMethod>>()
    private val myCards = MutableLiveData<Resource<GetMyPaymentCard>>()


     fun addCardPayment(card: AddPaymentCardPost) {
        viewModelScope.launch {

            addCard.postValue(Resource.loading(null))
            try {
                val usersFromApi = apiHelper?.addPaymentCard(card)

                if (usersFromApi!!.status && usersFromApi.code == 200)
                    addCard.postValue(Resource.success(usersFromApi))
                else {
                    addCard.postValue(Resource.error(usersFromApi.message, null))

                }


            } catch (e: TimeoutCancellationException) {
                addCard.postValue(Resource.error(context.getString(R.string.timeout_error), null))
            } catch (e: Exception) {
                if (e is IOException) {
                    addCard.postValue(Resource.error(context.getString(R.string.network_error), null))
                } else {
                    addCard.postValue(Resource.error(context.getString(R.string.something_went_error), null))
                }
                Log.e(TAG, "addCardPayment: ${e.message}")
            }
        }
    }

    fun editCardPayment(card: EditPaymentCardPost) {
        viewModelScope.launch {

            editCard.postValue(Resource.loading(null))
            try {
                val usersFromApi = apiHelper?.editPaymentCard(card)

                if (usersFromApi!!.status && usersFromApi.code == 200)
                    editCard.postValue(Resource.success(usersFromApi))
                else {
                    editCard.postValue(Resource.error(usersFromApi.message, null))

                }


            } catch (e: TimeoutCancellationException) {
                editCard.postValue(Resource.error(context.getString(R.string.timeout_error), null))
            } catch (e: Exception) {
                if (e is IOException) {
                    editCard.postValue(Resource.error(context.getString(R.string.network_error), null))
                } else {
                    editCard.postValue(Resource.error(context.getString(R.string.something_went_error), null))
                }
                Log.e(TAG, "editCardPayment: ${e.message}")
            }
        }
    }

    fun deleteCardPayment(cardId: Int) {
        viewModelScope.launch {

            deleteCard.postValue(Resource.loading(null))
            try {
                val usersFromApi = apiHelper?.deletePaymentCard(cardId)

                if (usersFromApi!!.status && usersFromApi.code == 200)
                    deleteCard.postValue(Resource.success(usersFromApi))
                else {
                    deleteCard.postValue(Resource.error(usersFromApi.message, null))

                }

            } catch (e: TimeoutCancellationException) {
                deleteCard.postValue(Resource.error(context.getString(R.string.timeout_error), null))
            } catch (e: Exception) {
                if (e is IOException) {
                    deleteCard.postValue(Resource.error(context.getString(R.string.network_error), null))
                } else {
                    deleteCard.postValue(Resource.error(context.getString(R.string.something_went_error), null))
                }
                Log.e(TAG, "deleteCardPayment: ${e.message}")
            }
        }
    }


    fun paymentMethod() {
        viewModelScope.launch {

            paymentMethod.postValue(Resource.loading(null))
            try {
                val usersFromApi = apiHelper?.getPaymentMethods()

                if (usersFromApi!!.status && usersFromApi.code == 200)
                    paymentMethod.postValue(Resource.success(usersFromApi))
                else {
                    paymentMethod.postValue(Resource.error(usersFromApi.message, null))

                }


            } catch (e: TimeoutCancellationException) {
                paymentMethod.postValue(Resource.error(context.getString(R.string.timeout_error), null))
            } catch (e: Exception) {
                if (e is IOException) {
                    paymentMethod.postValue(Resource.error(context.getString(R.string.network_error), null))
                } else {
                    paymentMethod.postValue(Resource.error(context.getString(R.string.something_went_error), null))
                }
                Log.e(TAG, "paymentMethod: ${e.message}")
            }
        }
    }

    fun myPaymentCard() {
        viewModelScope.launch {

            myCards.postValue(Resource.loading(null))
            try {
                val usersFromApi = apiHelper?.getMyPaymentCards()

                if (usersFromApi!!.status && usersFromApi.code == 200)
                    myCards.postValue(Resource.success(usersFromApi))
                else {
                    myCards.postValue(Resource.error(usersFromApi.message, null))

                }


            } catch (e: TimeoutCancellationException) {
                myCards.postValue(Resource.error(context.getString(R.string.timeout_error), null))
            } catch (e: Exception) {
                if (e is IOException) {
                    myCards.postValue(Resource.error(context.getString(R.string.network_error), null))
                } else {
                    myCards.postValue(Resource.error(context.getString(R.string.something_went_error), null))
                }
                Log.e(TAG, "myPaymentCard: ${e.message}")
            }
        }
    }



    fun getCardPayment(): LiveData<Resource<AddPaymentCard>> {
        return addCard
    }

    fun getPaymentMethod(): LiveData<Resource<GetPaymentMethod>> {
        return paymentMethod
    }

    fun getMyPaymentCard(): LiveData<Resource<GetMyPaymentCard>> {
        return myCards
    }

    fun getEditPaymentCard(): LiveData<Resource<AddPaymentCard>> {
        return editCard
    }

    fun getDeletePaymentCard(): LiveData<Resource<GeneralResponse>> {
        return deleteCard
    }


}