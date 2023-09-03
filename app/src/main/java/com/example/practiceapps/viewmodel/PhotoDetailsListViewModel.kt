package com.example.practiceapps.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practiceapps.database.model.PhotoDetails
import com.example.practiceapps.datamodel.LoaderStatus
import com.example.practiceapps.network.NetworkResultState
import com.example.practiceapps.network.ResponseStatus
import com.example.practiceapps.repository.PhotoDetailsListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class PhotoDetailsListViewModel @Inject constructor(photoDetailsListRepository: PhotoDetailsListRepository) :
    ViewModel() {

    private val mPhotoDetailsListRepository = photoDetailsListRepository

    private val _photoDetailsListMutableLiveData = MutableLiveData<MutableList<PhotoDetails>>()
    val photoDetailsListLiveData: LiveData<MutableList<PhotoDetails>> =
        _photoDetailsListMutableLiveData

    private val _loaderMutableLiveData = MutableLiveData<LoaderStatus>()
    val loaderLiveData: LiveData<LoaderStatus> = _loaderMutableLiveData

    /**
     * This function is used to make the API call from the UserListRepository for User lists.
     *
     * @param offset Determines whether to start returning data. The default value is 0.
     * @param limit This limits the number of results (for better performance and speed).
     *              The default value is 10.
     * */
    fun callUsersApi(offset: Int, limit: Int) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            supervisorScope {
                launch {
                    when (mPhotoDetailsListRepository.makeRemoteProductsListCall(offset, limit)) {
                        is NetworkResultState.Success -> {
                        }

                        is NetworkResultState.Error -> {
                        }
                    }
                }

                launch {
                    when (val apiResponse =
                        mPhotoDetailsListRepository.makeRemotePhotoDetailsListCall(offset, limit)) {
                        is NetworkResultState.Success -> {
                            val parsedArray = ArrayList<PhotoDetails>()
                            //Clear the DB of already existing user list.
                            mPhotoDetailsListRepository.clearPhotoDetailsListTable()
                            apiResponse.data.let {
                                if (it.photos.isEmpty()) {
                                    _loaderMutableLiveData.postValue(
                                        LoaderStatus(
                                            false,
                                            ResponseStatus.EMPTY_API_LIST
                                        )
                                    )
                                } else {
                                    it.photos.forEach { imageDetail ->
                                        val data = PhotoDetails(
                                            imageDetail.description,
                                            imageDetail.url,
                                            imageDetail.title,
                                            imageDetail.id,
                                            imageDetail.user
                                        )
                                        parsedArray.add(data)
                                        //Add user details to DB.
                                        mPhotoDetailsListRepository.insertIntoTable(data)
                                    }
                                }
                                //Add user details to livedata.
                                _photoDetailsListMutableLiveData.postValue(parsedArray)
                                _loaderMutableLiveData.postValue(
                                    LoaderStatus(
                                        false,
                                        ResponseStatus.NO_ISSUE
                                    )
                                )
                            }
                        }

                        is NetworkResultState.Error -> {
                            _loaderMutableLiveData.postValue(
                                LoaderStatus(
                                    false,
                                    ResponseStatus.API_ERROR
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    /**
     * This function will sort the list from DB.
     * @param isAscending Should the list be in ascending or descending order.
     * */
    fun orderPhotoDetailsList(isAscending: Boolean) = if (isAscending) {
        makePhotoDetailsListAscending()
    } else {
        makePhotoDetailsListDescending()
    }

    /**
     * This function will fetch data in ascending order the UserListRepository.
     * */
    private fun makePhotoDetailsListAscending() = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            val ascendingList = mPhotoDetailsListRepository.fetchAscendingListFromDB()
            _photoDetailsListMutableLiveData.postValue(ascendingList.toMutableList())
            _loaderMutableLiveData.postValue(LoaderStatus(false, ResponseStatus.NO_ISSUE))
        }
    }

    /**
     * This function will fetch data in descending order from the UserListRepository.
     * */
    private fun makePhotoDetailsListDescending() = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            val descendingList = mPhotoDetailsListRepository.fetchDescendingListFromDB()
            _photoDetailsListMutableLiveData.postValue(descendingList.toMutableList())
            _loaderMutableLiveData.postValue(LoaderStatus(false, ResponseStatus.NO_ISSUE))
        }
    }

}