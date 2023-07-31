package com.example.practiceapps.network

import com.google.gson.annotations.SerializedName


class ResponseClass {

    data class ApiEntriesResponse(
        @SerializedName("count") var count: Int? = null,
        @SerializedName("entries") var entries: ArrayList<Entries> = arrayListOf()
    )

    data class Entries(
        @SerializedName("API") var api: String? = null,
        @SerializedName("Description") var description: String? = null,
        @SerializedName("Auth") var auth: String? = null,
        @SerializedName("HTTPS") var https: Boolean? = null,
        @SerializedName("Cors") var cors: String? = null,
        @SerializedName("Link") var link: String? = null,
        @SerializedName("Category") var category: String? = null
    )

    data class PhotoListResponse(
        @SerializedName("success") var success: Boolean? = null,
        @SerializedName("message") var message: String? = null,
        @SerializedName("offset") var offset: Int? = null,
        @SerializedName("limit") var limit: Int? = null,
        @SerializedName("photos") var photos: ArrayList<Photos> = arrayListOf()
    )

    data class Photos(
        @SerializedName("description") var description: String? = null,
        @SerializedName("url") var url: String? = null,
        @SerializedName("title") var title: String? = null,
        @SerializedName("id") var id: Int? = null,
        @SerializedName("user") var user: Int? = null
    )

}