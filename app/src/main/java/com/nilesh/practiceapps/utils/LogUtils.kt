package com.nilesh.practiceapps.utils

import android.util.Log
import com.nilesh.practiceapps.BuildConfig


object LogUtils {

    fun d(strTag: String, strMsg: String?) {
        if (!BuildConfig.DEBUG) return
        Log.d(strTag, "$strMsg")
    }

    fun e(strTag: String, strMsg: String?) {
        if (!BuildConfig.DEBUG) return
        Log.e(strTag, "$strMsg")
    }

    fun i(strTag: String, strMsg: String?) {
        if (!BuildConfig.DEBUG) return
        Log.i(strTag, "$strMsg")
    }

    fun w(strTag: String, strMsg: String?) {
        if (!BuildConfig.DEBUG) return
        Log.w(strTag, "$strMsg")
    }
}