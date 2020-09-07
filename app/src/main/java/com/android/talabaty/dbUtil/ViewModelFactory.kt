package com.android.talabaty.dbUtil

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.talabaty.retrofit.ApiHelper
import com.android.talabaty.viewModel.*

class ViewModelFactory(private val apiHelper: ApiHelper, val application: Application) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StoresViewModel::class.java)) {
            return StoresViewModel(apiHelper,application) as T
        }

        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(apiHelper,application) as T
        }

        if (modelClass.isAssignableFrom(StoreDetailsViewModel::class.java)) {
            return StoreDetailsViewModel(apiHelper,application) as T
        }

        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(apiHelper,application) as T
        }

        if (modelClass.isAssignableFrom(SignUpViewModel::class.java)) {
            return SignUpViewModel(apiHelper,application) as T
        }

        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(apiHelper,application) as T
        }

        if (modelClass.isAssignableFrom(CartViewModel::class.java)) {
            return CartViewModel(apiHelper,application) as T
        }

        if (modelClass.isAssignableFrom(AllMainViewModel::class.java)) {
            return AllMainViewModel(apiHelper,application) as T
        }

        if (modelClass.isAssignableFrom(OrdersViewModel::class.java)) {
            return OrdersViewModel(apiHelper,application) as T
        }

        if (modelClass.isAssignableFrom(CategoryViewModel::class.java)) {
            return CategoryViewModel(apiHelper,application) as T
        }

        if (modelClass.isAssignableFrom(DigitalServiceViewModel::class.java)) {
            return DigitalServiceViewModel(apiHelper,application) as T
        }

        throw IllegalArgumentException("Unknown class name")
    }


}