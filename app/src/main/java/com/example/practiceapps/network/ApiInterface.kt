package com.example.practiceapps.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiInterface {

    @GET("/api/users")
    fun getUserList(
        @Query("page") pageNumber: Int
    ): Call<ResponseClass.UserResponse>

    @GET("/entries")
    fun getApiEntries(): Call<ResponseClass.ApiEntriesResponse>

}