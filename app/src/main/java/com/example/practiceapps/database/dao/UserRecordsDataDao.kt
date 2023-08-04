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

    @Query("DELETE from ${AppDatabase.USER_RECORDS_TABLE}")
    suspend fun clearUserRecordsTable()

}