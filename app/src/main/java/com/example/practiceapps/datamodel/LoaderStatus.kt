package com.example.practiceapps.datamodel

import com.example.practiceapps.network.ResponseStatus


data class LoaderStatus(val shouldShow: Boolean = false, val responseStatus: ResponseStatus)
