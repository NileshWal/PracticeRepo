package com.example.practiceapps.ui.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.practiceapps.database.AppDatabase
import com.example.practiceapps.database.model.PublicApisListDetails
import com.example.practiceapps.model.LoaderStatus
import com.example.practiceapps.network.NetworkInstance
import com.example.practiceapps.utils.LogUtils
import com.example.practiceapps.utils.ResponseStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class PublicApiViewModel(appDatabase: AppDatabase) : ViewModel() {

    private val screenName = PublicApiViewModel::class.java.simpleName
    private val mAppDatabase = appDatabase
    var apiEntriesLiveData = mutableStateListOf<PublicApisListDetails>()

    private val _loaderLiveData = MutableLiveData<LoaderStatus>()
    val loaderLiveData: MutableLiveData<LoaderStatus> = _loaderLiveData

    /**
     * This function is used to make the API call for Api Entry list.
     * */
    fun callApiEntriesApi() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val apiEntriesRequest =
                    NetworkInstance.getInstance(NetworkInstance.API_ENTRIES_BASE_URL)
                        .getApiEntries()
                val apiEntriesResult = apiEntriesRequest.execute()
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
                            if (mAppDatabase.publicApisDataDao().getApiEntriesCount() > 0) {
                                mAppDatabase.publicApisDataDao().clearApiEntriesTable()
                            }

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
                                apiEntriesLiveData.add(index, data)
                                mAppDatabase.publicApisDataDao().insertIntoTable(data)
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
        }
    }

    //ViewModelFactory
    class PublicApiViewModelFactory(private val appDatabase: AppDatabase) :
        ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(PublicApiViewModel::class.java)) {
                return PublicApiViewModel(appDatabase) as T
            }
            throw IllegalArgumentException("Unknown class name")
        }
    }

}