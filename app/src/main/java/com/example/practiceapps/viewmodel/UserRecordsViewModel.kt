package com.example.practiceapps.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practiceapps.database.model.UserRecordsListDetails
import com.example.practiceapps.datamodel.LoaderStatus
import com.example.practiceapps.network.NetworkResultState
import com.example.practiceapps.network.ResponseStatus
import com.example.practiceapps.repository.UserRecordsRepository
import com.example.practiceapps.utils.CommonUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class UserRecordsViewModel @Inject constructor(userRecordsRepository: UserRecordsRepository) :
    ViewModel() {

    private val mUserRecordsRepository = userRecordsRepository

    private var _userRecordsLiveData = mutableStateListOf<UserRecordsListDetails>()
    var userRecordsLiveData: SnapshotStateList<UserRecordsListDetails> =
        _userRecordsLiveData

    private val _loaderLiveData = MutableLiveData<LoaderStatus>()
    val loaderLiveData: LiveData<LoaderStatus> = _loaderLiveData

    /**
     * This function is used to make the API call for Api Entry list.
     * */
    fun callUserRecordsApi(offset: Int, limit: Int) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            when (mUserRecordsRepository.makeRemoteUserRecordsCall(offset, limit)) {
                is NetworkResultState.Success -> {
                    //Clear the DB of already existing API list.
                    mUserRecordsRepository.clearUserRecordsDB()
                    val parsedArray = mutableStateListOf<UserRecordsListDetails>()
                    mUserRecordsRepository.getUserRecordsLivedata().value?.let {
                        if (it.isEmpty()) {
                            _loaderLiveData.postValue(
                                LoaderStatus(
                                    false,
                                    ResponseStatus.EMPTY_API_LIST
                                )
                            )
                        } else {
                            it.forEachIndexed { index, usersRecords ->
                                val data = UserRecordsListDetails(
                                    usersRecords.id,
                                    usersRecords.firstName,
                                    usersRecords.lastName,
                                    usersRecords.gender,
                                    CommonUtils.formattedDate(usersRecords.dateOfBirth),
                                    usersRecords.email,
                                    usersRecords.phone,
                                    usersRecords.street,
                                    usersRecords.city,
                                    usersRecords.state,
                                    usersRecords.country,
                                    usersRecords.zipcode,
                                    usersRecords.job,
                                    usersRecords.longitude,
                                    usersRecords.latitude
                                )
                                parsedArray.add(data)
                                _userRecordsLiveData.add(index, data)
                                mUserRecordsRepository.insertIntoTable(data)
                            }
                            _loaderLiveData.postValue(
                                LoaderStatus(
                                    false,
                                    ResponseStatus.NO_ISSUE
                                )
                            )
                        }
                    } ?: _loaderLiveData.postValue(
                        LoaderStatus(
                            false,
                            ResponseStatus.API_ERROR
                        )
                    )
                }

                is NetworkResultState.Error -> {
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

    /**
     * This function will fetch data in ascending order the UserRecordsRepository.
     * */
    fun makeUserListAscending() = viewModelScope.launch {
        val ascendingList = mUserRecordsRepository.fetchUserRecordsAscendingOrder()
        if (_userRecordsLiveData.size > 0) {
            _userRecordsLiveData.clear()
        }
        _userRecordsLiveData.addAll(ascendingList.toMutableList())
        _loaderLiveData.postValue(LoaderStatus(false, ResponseStatus.NO_ISSUE))
    }

    /**
     * This function will fetch data in descending order from the UserRecordsRepository.
     * */
    fun makeUserListDescending() = viewModelScope.launch {
        val descendingList = mUserRecordsRepository.fetchUserRecordsDescendingOrder()
        if (_userRecordsLiveData.size > 0) {
            _userRecordsLiveData.clear()
        }
        _userRecordsLiveData.addAll(descendingList.toMutableList())
        _loaderLiveData.postValue(LoaderStatus(false, ResponseStatus.NO_ISSUE))
    }

}