package com.example.practiceapps.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.practiceapps.database.dao.PublicApisDataDao
import com.example.practiceapps.database.dao.UserListDataDao
import com.example.practiceapps.database.model.PublicApisListDetails
import com.example.practiceapps.database.model.UserListDetails


@Database(entities = [UserListDetails::class, PublicApisListDetails::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userListDataDao(): UserListDataDao
    abstract fun publicApisDataDao(): PublicApisDataDao

    companion object {

        private lateinit var appDatabase: AppDatabase

        private const val AAP_DB_NAME = "APP_DATABASE"
        const val USER_DETAIL_TABLE = "USER_DETAIL"
        const val PUBLIC_API_TABLE = "PUBLIC_API_ENTRIES"

        fun getInstance(context: Context): AppDatabase {
            if (!::appDatabase.isInitialized) {
                appDatabase =
                    Room.databaseBuilder(
                        context,
                        AppDatabase::class.java,
                        AAP_DB_NAME
                    ).build()
                /*.allowMainThreadQueries().build()*/
            }
            return appDatabase
        }
    }

}