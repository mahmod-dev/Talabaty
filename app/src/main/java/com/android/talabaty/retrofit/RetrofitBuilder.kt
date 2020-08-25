package com.android.talabaty.retrofit

import android.content.Context
import com.android.talabaty.util.MyPreferences
import com.android.talabaty.util.MyPreferences.context
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException


object RetrofitBuilder {

    private val BASE_URL = "http://tatbeqakum.salehly.com/public/api/"

    private fun getRetrofit(): Retrofit {
        val httpClient = OkHttpClient.Builder()

        httpClient.addInterceptor { chain ->
            val request: Request =
                chain.request().newBuilder()
                    .addHeader("Authorization", token()).build()
            chain.proceed(request)
        }

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: ApiService = getRetrofit().create(ApiService::class.java)
    private fun token(): String {
        return "Bearer ${MyPreferences.getStr("userToken")} "
    }


}