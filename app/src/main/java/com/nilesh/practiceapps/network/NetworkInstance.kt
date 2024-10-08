package com.nilesh.practiceapps.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object NetworkInstance {

    const val NORMAL_BASE_URL = "https://api.slingacademy.com/v1/sample-data/"
    const val COMPOSE_BASE_URL = "https://jsonplaceholder.typicode.com/"

    fun getInstance(baseUrl: String): ApiInterface {
        val retrofitInstance = Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofitInstance.create(ApiInterface::class.java)
    }

    private fun requestLoggerBuilder(): OkHttpClient.Builder {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
    }

}