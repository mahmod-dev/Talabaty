//package com.android.talabaty.util
//
//import android.app.Application
//import androidx.annotation.NonNull
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.ViewModelProvider
//import com.android.talabaty.retrofit.ApiHelper
//import com.android.talabaty.viewModel.StoresViewModel
//
//class VMFactory(application: Application, apiHelper: ApiHelper?) : ViewModelProvider.NewInstanceFactory() {
//
//    val _application: Application=application
//  //  val _apiHelper: ApiHelper=apiHelper
//
//    @NonNull
//    override fun <T : ViewModel?> create(@NonNull modelClass: Class<T>): T {
//        return  StoresViewModel(_apiHelper,_application) as T
//    }
//}