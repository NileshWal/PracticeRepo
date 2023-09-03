package com.nilesh.practiceapps.network

import com.google.gson.annotations.SerializedName


class ResponseClass {

    data class UserRecordsResponse(
        @SerializedName("success") var success: Boolean? = null,
        @SerializedName("time") var time: String? = null,
        @SerializedName("message") var message: String? = null,
        @SerializedName("total_users") var totalUsers: Int? = null,
        @SerializedName("offset") var offset: Int? = null,
        @SerializedName("limit") var limit: Int? = null,
        @SerializedName("users") var users: ArrayList<Users> = arrayListOf()
    )

    data class Users(
        @SerializedName("id") var id: Int? = null,
        @SerializedName("first_name") var firstName: String? = null,
        @SerializedName("last_name") var lastName: String? = null,
        @SerializedName("gender") var gender: String? = null,
        @SerializedName("date_of_birth") var dateOfBirth: String? = null,
        @SerializedName("email") var email: String? = null,
        @SerializedName("phone") var phone: String? = null,
        @SerializedName("street") var street: String? = null,
        @SerializedName("city") var city: String? = null,
        @SerializedName("state") var state: String? = null,
        @SerializedName("country") var country: String? = null,
        @SerializedName("zipcode") var zipcode: String? = null,
        @SerializedName("job") var job: String? = null,
        @SerializedName("longitude") var longitude: Double? = null,
        @SerializedName("latitude") var latitude: Double? = null
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

    data class ProductsResponse(
        @SerializedName("success") var success: Boolean? = null,
        @SerializedName("message") var message: String? = null,
        @SerializedName("offset") var offset: Int? = null,
        @SerializedName("limit") var limit: Int? = null,
        @SerializedName("products") var products: ArrayList<Products> = arrayListOf()
    )

    data class Products(
        @SerializedName("photo_url") var photoUrl: String? = null,
        @SerializedName("description") var description: String? = null,
        @SerializedName("id") var id: Int? = null,
        @SerializedName("created_at") var createdAt: String? = null,
        @SerializedName("updated_at") var updatedAt: String? = null,
        @SerializedName("name") var name: String? = null,
        @SerializedName("price") var price: Double? = null,
        @SerializedName("category") var category: String? = null
    )

}