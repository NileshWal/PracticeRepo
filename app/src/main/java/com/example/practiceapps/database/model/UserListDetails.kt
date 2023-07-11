package com.example.practiceapps.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.practiceapps.database.AppDatabase


@Entity(tableName = AppDatabase.USER_DETAIL_TABLE)
data class UserListDetails(
    @ColumnInfo(name = "id") var id: Int? = null,
    @ColumnInfo(name = "email") var email: String? = null,
    @ColumnInfo(name = "first_name") var firstName: String? = null,
    @ColumnInfo(name = "last_name") var lastName: String? = null,
    @ColumnInfo(name = "avatar") var avatar: String? = null
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "srNo")
    var srNo: Int? = null
}
