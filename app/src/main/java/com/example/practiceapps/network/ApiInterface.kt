package com.example.practiceapps.network

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("photos")
    fun fetchImageList(
        @Query("offset") offset: Int = 0,
        @Query("limit") limit: Int = 10
    ): Call<ResponseClass.PhotoListResponse>

    @GET("users")
    suspend fun fetchUserRecords(
        @Query("offset") offset: Int = 0,
        @Query("limit") limit: Int = 10
    ): Response<ResponseClass.UserRecordsResponse>

}