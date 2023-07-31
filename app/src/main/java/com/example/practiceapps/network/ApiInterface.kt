package com.example.practiceapps.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiInterface {

    @GET("v1/sample-data/photos")
    fun fetchImageList(
        @Query("offset") offset: Int = 0,
        @Query("limit") limit: Int = 10
    ): Call<ResponseClass.PhotoListResponse>

    @GET("/entries")
    fun fetchApiEntries(): Call<ResponseClass.ApiEntriesResponse>

}