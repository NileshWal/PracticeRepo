package com.example.practiceapps.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.practiceapps.database.AppDatabase
import com.example.practiceapps.database.model.PhotoDetails
import com.example.practiceapps.network.ApiInterface
import com.example.practiceapps.network.NetworkResultState
import com.example.practiceapps.network.ResponseClass
import com.example.practiceapps.network.ResponseStatus
import com.example.practiceapps.utils.LogUtils
import javax.inject.Inject


class PhotoDetailsListRepository @Inject constructor(
    private val networkInstance: ApiInterface,
    private val appDatabase: AppDatabase
) {

    private val screenName = PhotoDetailsListRepository::class.java.simpleName

    private val _imageListMutableLiveData = MutableLiveData<MutableList<ResponseClass.Photos>>()
    val imageListLiveData: LiveData<MutableList<ResponseClass.Photos>> =
        _imageListMutableLiveData

    /**
     * This function is used to make the API call for Image lists.
     *
     * @param offset Determines whether to start returning data. The default value is 0.
     * @param limit This limits the number of results (for better performance and speed).
     *              The default value is 10.
     * */
    fun makeRemoteImageListCall(
        offset: Int,
        limit: Int
    ): NetworkResultState<ResponseClass.PhotoListResponse> {
        val imageListRequest = networkInstance.fetchImageList(offset, limit)
        try {
            val imageListResult = imageListRequest.execute()
            LogUtils.e(screenName, "response code ${imageListResult.code()}")
            if (imageListResult.isSuccessful && imageListResult.code() == 200) {
                LogUtils.e(
                    screenName, "imageListResult ${imageListResult.body().toString()}"
                )
                imageListResult.body()?.let {
                    _imageListMutableLiveData.postValue(it.photos)
                    return NetworkResultState.Success(ResponseStatus.NO_ISSUE.toString(), it)
                } ?: return NetworkResultState.Error(ResponseStatus.EMPTY_API_LIST.toString())
            } else {
                return NetworkResultState.Error(ResponseStatus.API_ERROR.toString())
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return NetworkResultState.Error(ResponseStatus.API_ERROR.toString())
        }
    }

    /**
     * This function will insert all the data received by API call into the DB in PHOTO_DETAILS_LIST_TABLE.
     *
     * @param photoDetails The ImageListPhotos object.
     * */
    suspend fun insertIntoTable(photoDetails: PhotoDetails) =
        appDatabase.imageListDataDao().insertIntoTable(photoDetails)

    /**
     * This function will make DB call to fetch data in ascending order from PHOTO_DETAILS_LIST_TABLE.
     * */
    suspend fun fetchAscendingUserListFromDB(): List<PhotoDetails> =
        appDatabase.imageListDataDao().arrangeInAscendingOrder()

    /**
     * This function will make DB call to fetch data in descending order form PHOTO_DETAILS_LIST_TABLE.
     * */
    suspend fun fetchDescendingUserListFromDB(): List<PhotoDetails> =
        appDatabase.imageListDataDao().arrangeInDescendingOrder()

    /**
     * This function will clear the PHOTO_DETAILS_LIST_TABLE from DB.
     * */
    suspend fun clearImageListDB() {
        if (appDatabase.imageListDataDao().getImageListCount() > 0) {
            appDatabase.imageListDataDao().clearImageListTable()
        }
    }

}