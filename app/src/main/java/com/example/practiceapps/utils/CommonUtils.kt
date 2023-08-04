package com.example.practiceapps.utils

import android.content.Context
import android.net.ConnectivityManager
import android.widget.Toast


object CommonUtils {

    const val DATE_TIME_FORMAT_API = "yyyy-MM-dd'T'HH:mm:ss"
    const val DATE_TIME_FORMAT_REQUIRED = "yyyy-MM-dd"

    fun isConnected(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = connectivityManager.activeNetworkInfo
        return netInfo != null && netInfo.isConnected
    }

    fun showToastMessage(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

}