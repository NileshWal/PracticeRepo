package com.nilesh.practiceapps.datamodel

import com.nilesh.practiceapps.network.ResponseStatus


data class LoaderStatus(val shouldShow: Boolean = false, val responseStatus: ResponseStatus)
