package com.example.practiceapps.repository

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.MutableLiveData
import com.example.practiceapps.database.AppDatabase
import com.example.practiceapps.database.model.PublicApisListDetails
import com.example.practiceapps.model.LoaderStatus
import com.example.practiceapps.network.ApiInterface
import com.example.practiceapps.utils.LogUtils
import com.example.practiceapps.utils.ResponseStatus

class PublicApiRepository(
    private val networkInstance: ApiInterface,
    private val appDatabase: AppDatabase
) {

    private val screenName = PublicApiRepository::class.java.simpleName
    private val _loaderLiveData = MutableLiveData<LoaderStatus>()
    private var _apiEntriesLiveData = mutableStateListOf<PublicApisListDetails>()

    fun getLoaderLivedata() = _loaderLiveData
    fun getApiEntriesLivedata() = _apiEntriesLiveData

    /**
     * This function is used to make the API call for Api Entry list.
     * */
    suspend fun makeRemotePublicApiCall() {
        val apiEntriesRequest = networkInstance.getApiEntries()
        val apiEntriesResult = apiEntriesRequest.execute()

        LogUtils.e(screenName, "response code ${apiEntriesResult.code()}")

        if (apiEntriesResult.isSuccessful && apiEntriesResult.code() == 200) {
            LogUtils.e(
                screenName,
                "apiEntriesResult ${apiEntriesResult.body().toString()}"
            )
            apiEntriesResult.body()?.let { apiEntriesResponse ->
                if (apiEntriesResponse.entries.isEmpty()) {
                    _loaderLiveData.postValue(
                        LoaderStatus(
                            false,
                            ResponseStatus.EMPTY_API_LIST
                        )
                    )
                } else {

                    //Clear the DB of already existing API list.
                    clearPublicApiDB()

                    val parsedArray = mutableStateListOf<PublicApisListDetails>()
                    apiEntriesResponse.entries.forEachIndexed { index, apiEntries ->
                        val data = PublicApisListDetails(
                            apiEntries.api,
                            apiEntries.description,
                            apiEntries.auth,
                            apiEntries.https,
                            apiEntries.cors,
                            apiEntries.link,
                            apiEntries.category
                        )
                        parsedArray.add(data)
                        _apiEntriesLiveData.add(index, data)
                        insertIntoTable(data)
                    }
                    _loaderLiveData.postValue(
                        LoaderStatus(
                            false,
                            ResponseStatus.NO_ISSUE
                        )
                    )
                }
            }
        } else {
            _loaderLiveData.postValue(
                LoaderStatus(
                    false,
                    ResponseStatus.API_ERROR
                )
            )
        }
    }

    /**
     * This function will insert all the data received by API call into the DB in PUBLIC_API_TABLE.
     * */
    private suspend fun insertIntoTable(publicApisListDetails: PublicApisListDetails) {
        appDatabase.publicApisDataDao().insertIntoTable(publicApisListDetails)
    }

    /**
     * This function will clear the PUBLIC_API_TABLE from DB.
     * */
    private suspend fun clearPublicApiDB() {
        if (appDatabase.publicApisDataDao().getApiEntriesCount() > 0) {
            appDatabase.publicApisDataDao().clearApiEntriesTable()
        }
    }

}