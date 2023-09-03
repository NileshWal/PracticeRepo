package com.nilesh.practiceapps.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object NetworkInstance {

    private const val BASE_URL = "https://api.slingacademy.com/v1/sample-data/"

    fun getInstance(): ApiInterface {
        val retrofitInstance = Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofitInstance.create(ApiInterface::class.java)
    }

    private fun requestLoggerBuilder(): OkHttpClient.Builder {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
    }

}