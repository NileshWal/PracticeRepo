package com.example.practiceapps.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.practiceapps.database.AppDatabase
import com.example.practiceapps.database.model.UserRecordsListDetails

@Dao
interface UserRecordsDataDao {

    @Insert
    suspend fun insertIntoTable(userRecordsListDetails: UserRecordsListDetails)

    @Query("SELECT count(*) from ${AppDatabase.USER_RECORDS_TABLE}")
    suspend fun fetchUserRecordsCount(): Int

    @Query("SELECT * from ${AppDatabase.USER_RECORDS_TABLE} ORDER BY date_of_birth")
    suspend fun fetchUserRecordsAscendingOrder(): List<UserRecordsListDetails>

    @Query("SELECT * from ${AppDatabase.USER_RECORDS_TABLE} ORDER BY date_of_birth DESC")
    suspend fun fetchUserRecordsDescendingOrder(): List<UserRecordsListDetails>

    @Query("DELETE from ${AppDatabase.USER_RECORDS_TABLE}")
    suspend fun clearUserRecordsTable()

}