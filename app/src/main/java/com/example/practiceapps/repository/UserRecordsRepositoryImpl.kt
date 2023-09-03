package com.example.practiceapps.repository

import com.example.practiceapps.database.AppDatabase
import com.example.practiceapps.database.model.UserRecordsListDetails
import com.example.practiceapps.network.ApiInterface
import com.example.practiceapps.network.NetworkResultState
import com.example.practiceapps.network.ResponseClass
import com.example.practiceapps.network.ResponseStatus
import com.example.practiceapps.utils.CommonUtils
import com.example.practiceapps.utils.LogUtils
import javax.inject.Inject

class UserRecordsRepositoryImpl @Inject constructor(
    private val networkInstance: ApiInterface,
    private val appDatabase: AppDatabase
) : UserRecordsRepository {

    private val screenName = UserRecordsRepositoryImpl::class.java.simpleName

    /**
     * This function is used to make the API call for User list.
     *
     * @param offset Determines whether to start returning data. The default value is 0.
     * @param limit This limits the number of results (for better performance and speed).
     *              The default value is 10.
     *
     * */
    override suspend fun makeRemoteUserRecordsCall(
        offset: Int,
        limit: Int
    ): NetworkResultState<ResponseClass.UserRecordsResponse> {
        try {
            val userRecordsResponse = networkInstance.fetchUserRecords(offset, limit)
            LogUtils.e(screenName, "userRecordsResponse code ${userRecordsResponse.code()}")
            return if (userRecordsResponse.isSuccessful && userRecordsResponse.code()
                == CommonUtils.HTTP_OK_STATUS
            ) {
                LogUtils.e(
                    screenName, "userRecordsResponse ${userRecordsResponse.body().toString()}"
                )
                return userRecordsResponse.body()?.let {
                    NetworkResultState.Success(ResponseStatus.NO_ISSUE.toString(), it)
                } ?: NetworkResultState.Error(ResponseStatus.API_ERROR.toString())
            } else {
                NetworkResultState.Error(ResponseStatus.API_ERROR.toString())
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return NetworkResultState.Error(ResponseStatus.API_ERROR.toString())
        }
    }

    /**
     * This function will insert all the data received by API call into the DB in
     * USER_RECORDS_TABLE.
     * */
    override suspend fun insertIntoTable(userRecordsListDetails: UserRecordsListDetails) =
        appDatabase.userRecordsDataDao().insertIntoTable(userRecordsListDetails)

    /**
     * This function will fetch data in ascending order from USER_RECORDS_TABLE.
     * */
    override suspend fun fetchAscendingListFromDB() =
        appDatabase.userRecordsDataDao().ascendingOrderEntries()

    /**
     * This function will fetch data in descending order from USER_RECORDS_TABLE.
     * */
    override suspend fun fetchDescendingListFromDB() =
        appDatabase.userRecordsDataDao().descendingOrderEntries()

    /**
     * This function will clear the USER_RECORDS_TABLE from DB.
     * */
    override suspend fun clearUserRecordsDB() {
        if (appDatabase.userRecordsDataDao().getTableListCount() > 0) {
            appDatabase.userRecordsDataDao().clearTable()
        }
    }

}