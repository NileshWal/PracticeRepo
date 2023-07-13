package com.example.practiceapps.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.practiceapps.database.AppDatabase
import com.example.practiceapps.database.model.PublicApisListDetails

@Dao
interface PublicApisDataDao {

    @Insert
    suspend fun insertIntoTable(publicApisListDetails: PublicApisListDetails)

    @Query("SELECT count(*) from ${AppDatabase.PUBLIC_API_TABLE}")
    suspend fun getApiEntriesCount(): Int

    @Query("DELETE from ${AppDatabase.PUBLIC_API_TABLE}")
    suspend fun clearApiEntriesTable()

}