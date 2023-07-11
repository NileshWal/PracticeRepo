package com.example.practiceapps.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.practiceapps.database.AppDatabase


@Entity(tableName = AppDatabase.PUBLIC_API_TABLE)
data class PublicApisListDetails(
    @ColumnInfo(name = "API") var api: String? = null,
    @ColumnInfo(name = "Description") var description: String? = null,
    @ColumnInfo(name = "Auth") var auth: String? = null,
    @ColumnInfo(name = "HTTPS") var https: Boolean? = null,
    @ColumnInfo(name = "Cors") var cors: String? = null,
    @ColumnInfo(name = "Link") var link: String? = null,
    @ColumnInfo(name = "Category") var category: String? = null
){
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "srNo")
    var srNo: Int? = null
}
