package com.nilesh.practiceapps.utils

import android.util.Log
import org.conscrypt.BuildConfig


object LogUtils {

    fun debug(strTag: String, strMsg: String?) {
        if (!BuildConfig.DEBUG) return
        Log.d(strTag, "$strMsg")
    }

    fun error(strTag: String, strMsg: String?) {
        if (!BuildConfig.DEBUG) return
        Log.e(strTag, "$strMsg")
    }

}