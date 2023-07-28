package com.example.practiceapps.ui.viewmodel

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.practiceapps.database.model.PublicApisListDetails
import com.example.practiceapps.model.LoaderStatus
import com.example.practiceapps.repository.PublicApiRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class PublicApiViewModel(publicApiRepository: PublicApiRepository) : ViewModel() {

    private val mPublicApiRepository = publicApiRepository
    var apiEntriesLiveData: SnapshotStateList<PublicApisListDetails> =
        mPublicApiRepository.getApiEntriesLivedata()

    val loaderLiveData: MutableLiveData<LoaderStatus> = publicApiRepository.getLoaderLivedata()

    /**
     * This function is used to make the API call for Api Entry list.
     * */
    fun callApiEntriesApi() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                mPublicApiRepository.makeRemotePublicApiCall()
            }
        }
    }

    //ViewModelFactory
    class PublicApiViewModelFactory(private val appDatabase: PublicApiRepository) :
        ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(PublicApiViewModel::class.java)) {
                return PublicApiViewModel(appDatabase) as T
            }
            throw IllegalArgumentException("Unknown class name")
        }
    }

}