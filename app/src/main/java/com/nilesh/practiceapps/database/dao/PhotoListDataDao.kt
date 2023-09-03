package com.nilesh.practiceapps.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.nilesh.practiceapps.database.AppDatabase
import com.nilesh.practiceapps.database.model.PhotoDetails

@Dao
interface PhotoListDataDao {

    @Insert
    fun insertIntoTable(photoDetails: PhotoDetails)

    @Query("select * from ${AppDatabase.PHOTO_DETAILS_LIST_TABLE} ORDER BY id ASC")
    fun ascendingOrderEntries(): List<PhotoDetails>

    @Query("select * from ${AppDatabase.PHOTO_DETAILS_LIST_TABLE} ORDER BY id DESC")
    fun descendingOrderEntries(): List<PhotoDetails>

    @Query("SELECT count(*) from ${AppDatabase.PHOTO_DETAILS_LIST_TABLE}")
    fun getTableListCount(): Int

    @Query("DELETE from ${AppDatabase.PHOTO_DETAILS_LIST_TABLE}")
    fun clearTable()

}