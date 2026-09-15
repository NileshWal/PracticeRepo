package com.nilesh.practiceapps.network

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("photos")
    fun fetchPhotoDetailsList(
        @Query("offset") offset: Int = 0,
        @Query("limit") limit: Int = 10
    ): Call<ResponseClass.PhotoListResponse>

    @GET("products")
    fun fetchProductsList(
        @Query("offset") offset: Int = 0,
        @Query("limit") limit: Int = 10
    ): Call<ResponseClass.ProductsResponse>

    @GET("users")
    suspend fun fetchUserRecords(
        @Query("offset") offset: Int = 0,
        @Query("limit") limit: Int = 10
    ): Response<ResponseClass.UserRecordsResponse>

    @GET("posts")
    suspend fun fetchUserList(): List<ResponseClass.UserData>

}