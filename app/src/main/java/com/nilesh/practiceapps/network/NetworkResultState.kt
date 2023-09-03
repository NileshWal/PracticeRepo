package com.nilesh.practiceapps.network

sealed class NetworkResultState<T> {

    data class Success<T>(val message: String, val data: T) : NetworkResultState<T>()
    data class Error<T>(val message: String) : NetworkResultState<T>()

}