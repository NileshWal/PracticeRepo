package com.nilesh.practiceapps.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nilesh.practiceapps.database.AppDatabase

@Entity(tableName = AppDatabase.PHOTO_DETAILS_LIST_TABLE)
data class PhotoDetails(
    @ColumnInfo(name = "description") var description: String? = null,
    @ColumnInfo(name = "url") var url: String? = null,
    @ColumnInfo(name = "title") var title: String? = null,
    @ColumnInfo(name = "id") var id: Int? = null,
    @ColumnInfo(name = "user") var user: Int? = null
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "srNo")
    var srNo: Int? = null
}
