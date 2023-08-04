package com.example.practiceapps.repository

import androidx.lifecycle.MutableLiveData
import com.example.practiceapps.database.AppDatabase
import com.example.practiceapps.database.model.PhotoDetails
import com.example.practiceapps.model.LoaderStatus
import com.example.practiceapps.network.ApiInterface
import com.example.practiceapps.utils.LogUtils
import com.example.practiceapps.utils.ResponseStatus
import javax.inject.Inject


class PhotoDetailsListRepository @Inject constructor(
    private val networkInstance: ApiInterface,
    private val appDatabase: AppDatabase
) {

    private val screenName = PhotoDetailsListRepository::class.java.simpleName
    private val _loaderLiveData = MutableLiveData<LoaderStatus>()
    private val _imageListLiveData = MutableLiveData<MutableList<PhotoDetails>>()

    fun getLoaderLivedata() = _loaderLiveData
    fun getImageListLivedata() = _imageListLiveData

    /**
     * This function is used to make the API call for Image lists.
     *
     * @param offset Determines whether to start returning data. The default value is 0.
     * @param limit This limits the number of results (for better performance and speed).
     *              The default value is 10.
     * */
    suspend fun makeRemoteImageListCall(offset: Int, limit: Int) {
        val imageListRequest = networkInstance.fetchImageList(offset, limit)
        val imageListResult = imageListRequest.execute()

        LogUtils.e(screenName, "response code ${imageListResult.code()}")

        if (imageListResult.isSuccessful && imageListResult.code() == 200) {
            LogUtils.e(
                screenName, "imageListResult ${imageListResult.body().toString()}"
            )
            imageListResult.body()?.let { imageResponse ->
                if (imageResponse.photos.isEmpty()) {
                    _loaderLiveData.postValue(
                        LoaderStatus(
                            false,
                            ResponseStatus.EMPTY_API_LIST
                        )
                    )
                } else {
                    val parsedArray = ArrayList<PhotoDetails>()

                    //Clear the DB of already existing user list.
                    clearImageListDB()

                    imageResponse.photos.forEach { imageDetail ->
                        val data = PhotoDetails(
                            imageDetail.description,
                            imageDetail.url,
                            imageDetail.title,
                            imageDetail.id,
                            imageDetail.user
                        )
                        parsedArray.add(data)
                        //Add user details to DB.
                        insertIntoTable(data)
                    }
                    //Add user details to livedata.
                    _imageListLiveData.postValue(parsedArray)
                    _loaderLiveData.postValue(
                        LoaderStatus(
                            false,
                            ResponseStatus.NO_ISSUE
                        )
                    )
                }
            }
        } else {
            _loaderLiveData.postValue(
                LoaderStatus(
                    false,
                    ResponseStatus.API_ERROR
                )
            )
        }

    }

    /**
     * This function will insert all the data received by API call into the DB in PHOTO_DETAILS_LIST_TABLE.
     *
     * @param photoDetails The ImageListPhotos object.
     * */
    private suspend fun insertIntoTable(photoDetails: PhotoDetails) {
        appDatabase.imageListDataDao().insertIntoTable(photoDetails)
    }

    /**
     * This function will make DB call to fetch data in ascending order from PHOTO_DETAILS_LIST_TABLE.
     * */
    suspend fun fetchAscendingUserListFromDB() {
        val ascendingList = appDatabase.imageListDataDao().arrangeInAscendingOrder()
        _imageListLiveData.postValue(ascendingList.toMutableList())
        _loaderLiveData.postValue(LoaderStatus(false, ResponseStatus.NO_ISSUE))
    }

    /**
     * This function will make DB call to fetch data in descending order form PHOTO_DETAILS_LIST_TABLE.
     * */
    suspend fun fetchDescendingUserListFromDB() {
        val descendingList = appDatabase.imageListDataDao().arrangeInDescendingOrder()
        _imageListLiveData.postValue(descendingList.toMutableList())
        _loaderLiveData.postValue(LoaderStatus(false, ResponseStatus.NO_ISSUE))
    }

    /**
     * This function will clear the PHOTO_DETAILS_LIST_TABLE from DB.
     * */
    private suspend fun clearImageListDB() {
        if (appDatabase.imageListDataDao().getImageListCount() > 0) {
            appDatabase.imageListDataDao().clearImageListTable()
        }
    }

}