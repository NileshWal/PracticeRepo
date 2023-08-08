package com.example.practiceapps.utils

import android.content.Context
import android.net.ConnectivityManager
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


object CommonUtils {

    private const val DATE_TIME_FORMAT_API = "yyyy-MM-dd'T'HH:mm:ss"
    private const val DATE_TIME_FORMAT_REQUIRED = "yyyy-MM-dd"

    fun isConnected(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = connectivityManager.activeNetworkInfo
        return netInfo != null && netInfo.isConnected
    }

    fun showToastMessage(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    fun formattedDate(dateOfBirth: String?): String {
        val sdf =
            SimpleDateFormat(
                DATE_TIME_FORMAT_API,
                Locale.getDefault()
            )
        val output = SimpleDateFormat(
            DATE_TIME_FORMAT_REQUIRED,
            Locale.getDefault()
        )
        val d: Date = dateOfBirth?.let { sdf.parse(it) } ?: Date()
        return output.format(d)
    }

}