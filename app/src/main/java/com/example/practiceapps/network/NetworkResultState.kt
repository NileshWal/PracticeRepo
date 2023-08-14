package com.example.practiceapps.network

sealed class NetworkResultState<T> {

    data class Success<T>(val message: String, val data: T) : NetworkResultState<T>()
    data class Error<T>(val message: String, val data: T? = null) : NetworkResultState<T>()

}