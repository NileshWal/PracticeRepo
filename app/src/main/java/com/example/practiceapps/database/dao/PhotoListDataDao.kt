package com.example.practiceapps.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.practiceapps.database.AppDatabase
import com.example.practiceapps.database.model.PhotoDetails

@Dao
interface PhotoListDataDao {

    @Insert
    suspend fun insertIntoTable(photoDetails: PhotoDetails)

    @Query("select * from ${AppDatabase.PHOTO_DETAILS_LIST_TABLE} ORDER BY id ASC")
    suspend fun arrangeInAscendingOrder(): List<PhotoDetails>

    @Query("select * from ${AppDatabase.PHOTO_DETAILS_LIST_TABLE} ORDER BY id DESC")
    suspend fun arrangeInDescendingOrder(): List<PhotoDetails>

    @Query("SELECT count(*) from ${AppDatabase.PHOTO_DETAILS_LIST_TABLE}")
    suspend fun getPhotoDetailsListCount(): Int

    @Query("DELETE from ${AppDatabase.PHOTO_DETAILS_LIST_TABLE}")
    suspend fun clearPhotoDetailsListTable()

}