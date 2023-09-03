package com.nilesh.practiceapps.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.nilesh.practiceapps.database.AppDatabase
import com.nilesh.practiceapps.database.model.UserRecordsListDetails

@Dao
interface UserRecordsDataDao {

    @Insert
    fun insertIntoTable(userRecordsListDetails: UserRecordsListDetails)

    @Query("SELECT count(*) from ${AppDatabase.USER_RECORDS_TABLE}")
    fun getTableListCount(): Int

    @Query("SELECT * from ${AppDatabase.USER_RECORDS_TABLE} ORDER BY date_of_birth")
    fun ascendingOrderEntries(): List<UserRecordsListDetails>

    @Query("SELECT * from ${AppDatabase.USER_RECORDS_TABLE} ORDER BY date_of_birth DESC")
    fun descendingOrderEntries(): List<UserRecordsListDetails>

    @Query("DELETE from ${AppDatabase.USER_RECORDS_TABLE}")
    fun clearTable()

}