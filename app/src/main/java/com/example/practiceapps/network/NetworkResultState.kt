package com.example.practiceapps.network

sealed class NetworkResultState<T>(data: T? = null, message: String? = null) {

    class Success<T>(message: String, data: T) : NetworkResultState<T>(data, message)
    class Error<T>(message: String, data: T? = null) : NetworkResultState<T>(data, message)

}