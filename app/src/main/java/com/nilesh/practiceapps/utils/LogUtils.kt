package com.nilesh.practiceapps.utils

import android.util.Log
import com.nilesh.practiceapps.BuildConfig


object LogUtils {

    fun debug(strTag: String, strMsg: String?) {
        if (BuildConfig.DEBUG) {
            Log.d(strTag, "$strMsg")
        }
    }

    fun error(strTag: String, strMsg: String?) {
        if (BuildConfig.DEBUG) {
            Log.e(strTag, "$strMsg")
        }
    }

}