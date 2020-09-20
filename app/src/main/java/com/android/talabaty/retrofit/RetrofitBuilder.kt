package com.android.talabaty.retrofit

import com.android.talabaty.util.MyPreferences
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit


object RetrofitBuilder {

    private val BASE_URL = "http://tatbeqakum.salehly.com/public/api/"

    private fun getRetrofit(): Retrofit {
        try {


            val builder = OkHttpClient.Builder()
                .callTimeout(2, TimeUnit.MINUTES)
                .connectTimeout(2, TimeUnit.MINUTES)
                .readTimeout(2, TimeUnit.MINUTES)
                .writeTimeout(2, TimeUnit.MINUTES)

            builder.addInterceptor { chain ->
                val request: Request =
                    chain.request().newBuilder()
                        .addHeader("Authorization", token())
                        .addHeader("Accept-Language", Locale.getDefault().language)
                        .addHeader("Accept", "application/json")

                        .build()
                chain.proceed(request)
            }

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        } catch (ex: Exception) {
            return getRetrofit()
        }
    }

    val apiService: ApiService = getRetrofit().create(ApiService::class.java)
    private fun token(): String {
        return "Bearer ${MyPreferences.getStr("userToken")} "
    }


}