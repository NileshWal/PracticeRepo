package com.example.practiceapps.repository

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.MutableLiveData
import com.example.practiceapps.database.AppDatabase
import com.example.practiceapps.database.model.UserRecordsListDetails
import com.example.practiceapps.model.LoaderStatus
import com.example.practiceapps.network.ApiInterface
import com.example.practiceapps.utils.LogUtils
import com.example.practiceapps.utils.ResponseStatus
import javax.inject.Inject

class UserRecordsRepository @Inject constructor(
    private val networkInstance: ApiInterface,
    private val appDatabase: AppDatabase
) {

    private val screenName = UserRecordsRepository::class.java.simpleName
    private val _loaderLiveData = MutableLiveData<LoaderStatus>()
    private var _userRecordsLiveData = mutableStateListOf<UserRecordsListDetails>()

    fun getLoaderLivedata() = _loaderLiveData
    fun getUserRecordsLivedata() = _userRecordsLiveData

    /**
     * This function is used to make the API call for Api Entry list.
     *
     * @param offset Determines whether to start returning data. The default value is 0.
     * @param limit This limits the number of results (for better performance and speed).
     *              The default value is 10.
     *
     * */
    suspend fun makeRemoteUserRecordsCall(offset: Int, limit: Int) {
        val userRecordsRequest = networkInstance.fetchUserRecords(offset, limit)
        val userRecordsResult = userRecordsRequest.execute()

        LogUtils.e(screenName, "response code ${userRecordsResult.code()}")

        if (userRecordsResult.isSuccessful && userRecordsResult.code() == 200) {
            LogUtils.e(
                screenName,
                "userRecordsResult ${userRecordsResult.body().toString()}"
            )
            userRecordsResult.body()?.let { userRecordsResponse ->
                if (userRecordsResponse.users.isEmpty()) {
                    _loaderLiveData.postValue(
                        LoaderStatus(
                            false,
                            ResponseStatus.EMPTY_API_LIST
                        )
                    )
                } else {

                    //Clear the DB of already existing API list.
                    clearUserRecordsDB()

                    val parsedArray = mutableStateListOf<UserRecordsListDetails>()
                    userRecordsResponse.users.forEachIndexed { index, usersRecords ->
                        val data = UserRecordsListDetails(
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
                        parsedArray.add(data)
                        _userRecordsLiveData.add(index, data)
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
     * This function will insert all the data received by API call into the DB in USER_RECORDS_TABLE.
     * */
    private suspend fun insertIntoTable(userRecordsListDetails: UserRecordsListDetails) {
        appDatabase.UserRecordsDataDao().insertIntoTable(userRecordsListDetails)
    }

    /**
     * This function will clear the USER_RECORDS_TABLE from DB.
     * */
    private suspend fun clearUserRecordsDB() {
        if (appDatabase.UserRecordsDataDao().fetchUserRecordsCount() > 0) {
            appDatabase.UserRecordsDataDao().clearUserRecordsTable()
        }
    }

}