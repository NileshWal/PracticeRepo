package com.example.practiceapps.repository

import androidx.lifecycle.MutableLiveData
import com.example.practiceapps.database.AppDatabase
import com.example.practiceapps.database.model.UserRecordsListDetails
import com.example.practiceapps.network.ApiInterface
import com.example.practiceapps.network.NetworkResultState
import com.example.practiceapps.network.ResponseClass
import com.example.practiceapps.network.ResponseStatus
import com.example.practiceapps.utils.LogUtils
import javax.inject.Inject

class UserRecordsRepository @Inject constructor(
    private val networkInstance: ApiInterface,
    private val appDatabase: AppDatabase
) {

    private val screenName = UserRecordsRepository::class.java.simpleName

    private val _userRecordsMutableLiveData = MutableLiveData<MutableList<ResponseClass.Users>>()
    fun getUserRecordsLivedata() = _userRecordsMutableLiveData

    /**
     * This function is used to make the API call for Api Entry list.
     *
     * @param offset Determines whether to start returning data. The default value is 0.
     * @param limit This limits the number of results (for better performance and speed).
     *              The default value is 10.
     *
     * */
    suspend fun makeRemoteUserRecordsCall(
        offset: Int,
        limit: Int
    ): NetworkResultState<ResponseClass.UserRecordsResponse> {
        val userRecordsRequest = networkInstance.fetchUserRecords(offset, limit)
        return try {
            LogUtils.e(screenName, "response userRecordsRequest $userRecordsRequest")
            _userRecordsMutableLiveData.postValue(userRecordsRequest.users)
            NetworkResultState.Success(
                ResponseStatus.NO_ISSUE.toString(),
                userRecordsRequest
            )
        } catch (e: Exception) {
            e.printStackTrace()
            NetworkResultState.Error(ResponseStatus.API_ERROR.toString())
        }
    }

    /**
     * This function will insert all the data received by API call into the DB in USER_RECORDS_TABLE.
     * */
    suspend fun insertIntoTable(userRecordsListDetails: UserRecordsListDetails) =
        appDatabase.UserRecordsDataDao().insertIntoTable(userRecordsListDetails)

    /**
     * This function will fetch data in ascending order from USER_RECORDS_TABLE.
     * */
    suspend fun fetchUserRecordsAscendingOrder() =
        appDatabase.UserRecordsDataDao().fetchUserRecordsAscendingOrder()

    /**
     * This function will fetch data in descending order from USER_RECORDS_TABLE.
     * */
    suspend fun fetchUserRecordsDescendingOrder() =
        appDatabase.UserRecordsDataDao().fetchUserRecordsDescendingOrder()

    /**
     * This function will clear the USER_RECORDS_TABLE from DB.
     * */
    suspend fun clearUserRecordsDB() {
        if (appDatabase.UserRecordsDataDao().fetchUserRecordsCount() > 0) {
            appDatabase.UserRecordsDataDao().clearUserRecordsTable()
        }
    }

}