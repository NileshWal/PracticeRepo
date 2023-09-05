package com.nilesh.practiceapps.database.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nilesh.practiceapps.database.AppDatabase

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
) : Parcelable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "srNo")
    var srNo: Int? = null

    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double
    ) {
        srNo = parcel.readValue(Int::class.java.classLoader) as? Int
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(firstName)
        parcel.writeString(lastName)
        parcel.writeString(gender)
        parcel.writeString(dateOfBirth)
        parcel.writeString(email)
        parcel.writeString(phone)
        parcel.writeString(street)
        parcel.writeString(city)
        parcel.writeString(state)
        parcel.writeString(country)
        parcel.writeString(zipcode)
        parcel.writeString(job)
        parcel.writeValue(longitude)
        parcel.writeValue(latitude)
        parcel.writeValue(srNo)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserRecordsListDetails> {
        override fun createFromParcel(parcel: Parcel): UserRecordsListDetails {
            return UserRecordsListDetails(parcel)
        }

        override fun newArray(size: Int): Array<UserRecordsListDetails?> {
            return arrayOfNulls(size)
        }
    }
}
