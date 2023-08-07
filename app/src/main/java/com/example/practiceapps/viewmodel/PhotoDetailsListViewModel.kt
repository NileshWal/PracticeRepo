package com.example.practiceapps.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practiceapps.database.model.PhotoDetails
import com.example.practiceapps.model.LoaderStatus
import com.example.practiceapps.repository.PhotoDetailsListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class PhotoDetailsListViewModel @Inject constructor(photoDetailsListRepository: PhotoDetailsListRepository) :
    ViewModel() {

    private val mUserListRepository = photoDetailsListRepository
    val userListLiveData: LiveData<MutableList<PhotoDetails>> =
        mUserListRepository.getImageListLivedata()
    val loaderLiveData: LiveData<LoaderStatus> = mUserListRepository.getLoaderLivedata()

    /**
     * This function is used to make the API call from the UserListRepository for User lists.
     *
     * @param offset Determines whether to start returning data. The default value is 0.
     * @param limit This limits the number of results (for better performance and speed).
     *              The default value is 10.
     * */
    fun callUsersApi(offset: Int, limit: Int) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            mUserListRepository.makeRemoteImageListCall(offset, limit)
        }
    }


    /**
     * This function will fetch data in ascending order the UserListRepository.
     * */
    fun makeUserListAscending() = viewModelScope.launch {
        mUserListRepository.fetchAscendingUserListFromDB()
    }

    /**
     * This function will fetch data in descending order from the UserListRepository.
     * */
    fun makeUserListDescending() = viewModelScope.launch {
        mUserListRepository.fetchDescendingUserListFromDB()
    }

}