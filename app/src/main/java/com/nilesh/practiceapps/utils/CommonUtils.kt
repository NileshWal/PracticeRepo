package com.nilesh.practiceapps.utils

import android.content.Context
import android.net.ConnectivityManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


object CommonUtils {

    const val BLANK_STRING = ""
    const val DATE_TIME_FORMAT_API = "yyyy-MM-dd'T'HH:mm:ss"
    const val DATE_TIME_FORMAT_REQUIRED = "yyyy-MM-dd"
    const val HTTP_OK_STATUS = 200
    const val USER_RECORDS_LIST_DETAILS = "UserRecordsListDetails"

    /**
     * This function will check if device is connected to internet.
     *
     * @param context Context object.
     * */
    fun isConnected(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = connectivityManager.activeNetworkInfo
        return netInfo != null && netInfo.isConnected
    }

}

/**
 * This function will show toast message for long duration.
 *
 * @param context Context object.
 * @param message String message to be displayed.
 * */
fun Fragment.showToastMessage(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
}

/**
 * This function will parse the date from current pattern to required pattern.
 *
 * @param dateOfBirth Nullable date of birth string to be formatted to desired pattern.
 * @param currentDatePattern Current pattern of String type.
 * @param requiredDatePattern Required pattern of String type.
 * */
fun ViewModel.dateFormatParser(
    dateOfBirth: String?,
    currentDatePattern: String,
    requiredDatePattern: String
): String {
    val dateFormatter = simpleDateFormatInstance(currentDatePattern)
    val parsedDate: Date = dateOfBirth?.let { dateFormatter.parse(it) } ?: Date()
    return simpleDateFormatInstance(requiredDatePattern).format(parsedDate)
}

/**
 * This function will return the instance of SimpleDateFormat with required pattern.
 *
 * @param pattern Pattern of String type for the instance required.
 * */
fun ViewModel.simpleDateFormatInstance(pattern: String): SimpleDateFormat {
    return SimpleDateFormat(pattern, Locale.getDefault())
}


fun String.safeSubString(actualString: String, initialVal: Int, finalVal: Int): String =
    if (actualString.isNotEmpty()
        && initialVal <= actualString.length
        && finalVal <= actualString.length
        && initialVal != 0
        && finalVal != 0
        && initialVal < finalVal
    ) {
        actualString.substring(initialVal, finalVal)
    } else CommonUtils.BLANK_STRING