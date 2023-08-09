package com.example.practiceapps.network

sealed class NetworkResultState<T>(data: T? = null, message: String? = null) {

    data class Success<T>(val message: String, val data: T) : NetworkResultState<T>(data, message)
    data class Error<T>(val message: String, val data: T? = null) : NetworkResultState<T>(data, message)

}