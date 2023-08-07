package com.example.practiceapps.viewmodel

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practiceapps.database.model.UserRecordsListDetails
import com.example.practiceapps.model.LoaderStatus
import com.example.practiceapps.repository.UserRecordsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class UserRecordsViewModel @Inject constructor(userRecordsRepository: UserRecordsRepository) :
    ViewModel() {

    private val mUserRecordsRepository = userRecordsRepository
    var userRecordsLiveData: SnapshotStateList<UserRecordsListDetails> =
        mUserRecordsRepository.getUserRecordsLivedata()

    val loaderLiveData: MutableLiveData<LoaderStatus> = userRecordsRepository.getLoaderLivedata()

    /**
     * This function is used to make the API call for Api Entry list.
     * */
    fun callUserRecordsApi(offset: Int, limit: Int) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            mUserRecordsRepository.makeRemoteUserRecordsCall(offset, limit)
        }
    }

    /**
     * This function will fetch data in ascending order the UserRecordsRepository.
     * */
    fun makeUserListAscending() = viewModelScope.launch {
        mUserRecordsRepository.fetchUserRecordsAscendingOrder()
    }

    /**
     * This function will fetch data in descending order from the UserRecordsRepository.
     * */
    fun makeUserListDescending() = viewModelScope.launch {
        mUserRecordsRepository.fetchUserRecordsDescendingOrder()
    }

}