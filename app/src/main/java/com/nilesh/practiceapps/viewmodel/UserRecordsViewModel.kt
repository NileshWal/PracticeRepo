package com.nilesh.practiceapps.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nilesh.practiceapps.database.model.UserRecordsListDetails
import com.nilesh.practiceapps.datamodel.LoaderStatus
import com.nilesh.practiceapps.network.NetworkResultState
import com.nilesh.practiceapps.network.ResponseStatus
import com.nilesh.practiceapps.repository.UserRecordsRepository
import com.nilesh.practiceapps.utils.CommonUtils
import com.nilesh.practiceapps.utils.dateFormatParser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class UserRecordsViewModel @Inject constructor(userRecordsRepository: UserRecordsRepository) :
    ViewModel() {

    private val mUserRecordsRepository = userRecordsRepository

    private var _userRecordsMutableStateList = mutableStateListOf<UserRecordsListDetails>()
    var userRecordsSnapshotStateList: SnapshotStateList<UserRecordsListDetails> =
        _userRecordsMutableStateList

    private val _loaderLiveData = MutableLiveData<LoaderStatus>()
    val loaderLiveData: LiveData<LoaderStatus> = _loaderLiveData

    /**
     * This function is used to make the API call for User list.
     *
     * @param offset Determines whether to start returning data. The default value is 0.
     * @param limit This limits the number of results (for better performance and speed).
     *              The default value is 10.
     *
     * */
    fun callUserRecordsApi(offset: Int, limit: Int) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            when (val apiResponse =
                mUserRecordsRepository.makeRemoteUserRecordsCall(offset, limit)) {
                is NetworkResultState.Success -> {
                    //Clear the DB of already existing API list.
                    mUserRecordsRepository.clearUserRecordsDB()
                    val parsedArray = mutableStateListOf<UserRecordsListDetails>()
                    apiResponse.data.let { it ->
                        if (it.users.isEmpty()) {
                            _loaderLiveData.postValue(
                                LoaderStatus(
                                    false,
                                    ResponseStatus.EMPTY_API_LIST
                                )
                            )
                        } else {
                            it.users.forEachIndexed { index, usersRecords ->
                                var data = UserRecordsListDetails(
                                    usersRecords.id,
                                    usersRecords.firstName,
                                    usersRecords.lastName,
                                    usersRecords.gender,
                                    usersRecords.dateOfBirth,
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
                                data = data.also {
                                    it.dateOfBirth = dateFormatParser(
                                        it.dateOfBirth,
                                        CommonUtils.DATE_TIME_FORMAT_API,
                                        CommonUtils.DATE_TIME_FORMAT_REQUIRED
                                    )
                                }
                                parsedArray.add(data)
                                _userRecordsMutableStateList.add(index, data)
                                mUserRecordsRepository.insertIntoTable(data)
                            }
                            _loaderLiveData.postValue(
                                LoaderStatus(
                                    false,
                                    ResponseStatus.NO_ISSUE
                                )
                            )
                        }
                    }
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
     * This function will sort the list from DB.
     * @param isAscending Should the list be in ascending or descending order.
     * */
    fun orderUserList(isAscending: Boolean) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                if (isAscending) {
                    makeUserListAscending()
                } else {
                    makeUserListDescending()
                }
            }
        }
    }

    /**
     * This function will fetch data in ascending order the UserRecordsRepository.
     * */
    private suspend fun makeUserListAscending() {
        val ascendingList = mUserRecordsRepository.fetchAscendingListFromDB()
        if (_userRecordsMutableStateList.size > 0) {
            _userRecordsMutableStateList.clear()
        }
        _userRecordsMutableStateList.addAll(ascendingList.toMutableList())
        _loaderLiveData.postValue(LoaderStatus(false, ResponseStatus.NO_ISSUE))
    }

    /**
     * This function will fetch data in descending order from the UserRecordsRepository.
     * */
    private suspend fun makeUserListDescending() {
        val descendingList = mUserRecordsRepository.fetchDescendingListFromDB()
        if (_userRecordsMutableStateList.size > 0) {
            _userRecordsMutableStateList.clear()
        }
        _userRecordsMutableStateList.addAll(descendingList.toMutableList())
        _loaderLiveData.postValue(LoaderStatus(false, ResponseStatus.NO_ISSUE))
    }

    /**
     * This function will return the initial letter for Male or Female.
     * @param gender The gender received from API result.
     * */
    fun genderShortForm(gender: String?): String =
        gender?.let {
            if (it.lowercase() == "female") {
                "F"
            } else if (it.lowercase() == "male") {
                "M"
            } else {
                "NA"
            }
        } ?: "NA"

}