package com.example.practiceapps.repository

import com.example.practiceapps.database.model.UserRecordsListDetails
import com.example.practiceapps.network.NetworkResultState
import com.example.practiceapps.network.ResponseClass

interface UserRecordsRepository {

    /**
     * This function is used to make the API call for User list.
     *
     * @param offset Determines whether to start returning data. The default value is 0.
     * @param limit This limits the number of results (for better performance and speed).
     *              The default value is 10.
     *
     * */
    suspend fun makeRemoteUserRecordsCall(
        offset: Int,
        limit: Int
    ): NetworkResultState<ResponseClass.UserRecordsResponse>

    /**
     * This function will insert all the data received by API call into the DB in
     * USER_RECORDS_TABLE.
     * */
    suspend fun insertIntoTable(userRecordsListDetails: UserRecordsListDetails)

    /**
     * This function will fetch data in ascending order from USER_RECORDS_TABLE.
     * */
    suspend fun fetchAscendingListFromDB(): List<UserRecordsListDetails>

    /**
     * This function will fetch data in descending order from USER_RECORDS_TABLE.
     * */
    suspend fun fetchDescendingListFromDB(): List<UserRecordsListDetails>

    /**
     * This function will clear the USER_RECORDS_TABLE from DB.
     * */
    suspend fun clearUserRecordsDB()

}