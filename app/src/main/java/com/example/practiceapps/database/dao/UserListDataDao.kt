package com.example.practiceapps.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.practiceapps.database.AppDatabase
import com.example.practiceapps.database.model.UserListDetails

@Dao
interface UserListDataDao {

    @Insert
    fun insertIntoTable(userListDetails: UserListDetails)

    @Query("select * from ${AppDatabase.USER_DETAIL_TABLE} ORDER BY first_name ASC")
    fun arrangeInAscendingOrder(): List<UserListDetails>

    @Query("select * from ${AppDatabase.USER_DETAIL_TABLE} ORDER BY first_name DESC")
    fun arrangeInDescendingOrder(): List<UserListDetails>

    @Query("SELECT count(*) from ${AppDatabase.USER_DETAIL_TABLE}")
    fun getUserListCount(): Int

    @Query("DELETE from ${AppDatabase.USER_DETAIL_TABLE}")
    fun clearUserListTable()

}