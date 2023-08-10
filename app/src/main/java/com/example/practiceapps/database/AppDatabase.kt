package com.example.practiceapps.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.practiceapps.database.dao.PhotoListDataDao
import com.example.practiceapps.database.dao.UserRecordsDataDao
import com.example.practiceapps.database.model.PhotoDetails
import com.example.practiceapps.database.model.UserRecordsListDetails


@Database(
    entities = [PhotoDetails::class, UserRecordsListDetails::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun photoDetailsListDataDao(): PhotoListDataDao
    abstract fun userRecordsDataDao(): UserRecordsDataDao

    companion object {

        private lateinit var appDatabase: AppDatabase
        private const val AAP_DB_NAME = "APP_DATABASE"
        const val PHOTO_DETAILS_LIST_TABLE = "PHOTO_DETAILS_LIST"
        const val USER_RECORDS_TABLE = "USER_RECORDS"

        fun getInstance(context: Context): AppDatabase {
            if (!::appDatabase.isInitialized) {
                appDatabase =
                    Room.databaseBuilder(
                        context,
                        AppDatabase::class.java,
                        AAP_DB_NAME
                    ).build()
            }
            return appDatabase
        }
    }

}