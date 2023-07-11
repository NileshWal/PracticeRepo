package com.example.practiceapps.network

import com.google.gson.annotations.SerializedName


class ResponseClass {

    data class UserResponse(
        @SerializedName("page") var page: Int? = null,
        @SerializedName("per_page") var perPage: Int? = null,
        @SerializedName("total") var total: Int? = null,
        @SerializedName("total_pages") var totalPages: Int? = null,
        @SerializedName("data") var data: ArrayList<Data> = arrayListOf(),
        @SerializedName("support") var support: Support? = Support()
    )

    data class Data(
        @SerializedName("id") var id: Int? = null,
        @SerializedName("email") var email: String? = null,
        @SerializedName("first_name") var firstName: String? = null,
        @SerializedName("last_name") var lastName: String? = null,
        @SerializedName("avatar") var avatar: String? = null
    )

    data class Support(
        @SerializedName("url") var url: String? = null,
        @SerializedName("text") var text: String? = null
    )

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

}