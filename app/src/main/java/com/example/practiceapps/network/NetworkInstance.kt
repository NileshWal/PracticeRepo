package com.example.practiceapps.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object NetworkInstance {

    const val API_ENTRIES_BASE_URL = "https://api.publicapis.org/"
    const val IMAGE_LIST_BASE_URL = "https://api.slingacademy.com/"

    fun getInstance(url: String): ApiInterface {
        val retrofitInstance = Retrofit.Builder().baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofitInstance.create(ApiInterface::class.java)
    }

    private fun requestLoggerBuilder(): OkHttpClient.Builder {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
    }

}