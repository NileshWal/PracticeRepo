package com.example.practiceapps.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.practiceapps.database.AppDatabase


@Entity(tableName = AppDatabase.USER_RECORDS_TABLE)
data class UserRecordsListDetails(
    @ColumnInfo(name = "id") var id: Int? = null,
    @ColumnInfo(name = "first_name") var firstName: String? = null,
    @ColumnInfo(name = "last_name") var lastName: String? = null,
    @ColumnInfo(name = "gender") var gender: String? = null,
    @ColumnInfo(name = "date_of_birth") var dateOfBirth: String? = null,
    @ColumnInfo(name = "email") var email: String? = null,
    @ColumnInfo(name = "phone") var phone: String? = null,
    @ColumnInfo(name = "street") var street: String? = null,
    @ColumnInfo(name = "city") var city: String? = null,
    @ColumnInfo(name = "state") var state: String? = null,
    @ColumnInfo(name = "country") var country: String? = null,
    @ColumnInfo(name = "zipcode") var zipcode: String? = null,
    @ColumnInfo(name = "job") var job: String? = null,
    @ColumnInfo(name = "longitude") var longitude: Double? = null,
    @ColumnInfo(name = "latitude") var latitude: Double? = null
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "srNo")
    var srNo: Int? = null
}
