package com.nilesh.practiceapps.repository

import com.nilesh.practiceapps.database.AppDatabase
import com.nilesh.practiceapps.database.model.PhotoDetails
import com.nilesh.practiceapps.network.ApiInterface
import com.nilesh.practiceapps.network.NetworkResultState
import com.nilesh.practiceapps.network.ResponseClass
import com.nilesh.practiceapps.network.ResponseStatus
import com.nilesh.practiceapps.utils.CommonUtils
import com.nilesh.practiceapps.utils.LogUtils
import javax.inject.Inject


class PhotoDetailsListRepositoryImpl @Inject constructor(
    private val networkInstance: ApiInterface,
    private val appDatabase: AppDatabase
) : PhotoDetailsListRepository {

    private val screenName = PhotoDetailsListRepositoryImpl::class.java.simpleName

    /**
     * This function is used to make the API call for Image lists.
     *
     * @param offset Determines whether to start returning data. The default value is 0.
     * @param limit This limits the number of results (for better performance and speed).
     *              The default value is 10.
     * */
    override suspend fun makeRemotePhotoDetailsListCall(
        offset: Int,
        limit: Int
    ): NetworkResultState<ResponseClass.PhotoListResponse> {
        val photoDetailsListRequest = networkInstance.fetchPhotoDetailsList(offset, limit)
        try {
            val photoDetailsListResult = photoDetailsListRequest.execute()
            LogUtils.e(screenName, "response code ${photoDetailsListResult.code()}")
            return if (photoDetailsListResult.isSuccessful && photoDetailsListResult.code()
                == CommonUtils.HTTP_OK_STATUS
            ) {
                LogUtils.e(
                    screenName, "photoDetailsListResult ${photoDetailsListResult.body().toString()}"
                )
                return photoDetailsListResult.body()?.let {
                    NetworkResultState.Success(ResponseStatus.NO_ISSUE.toString(), it)
                } ?: NetworkResultState.Error(ResponseStatus.EMPTY_API_LIST.toString())
            } else {
                NetworkResultState.Error(ResponseStatus.API_ERROR.toString())
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return NetworkResultState.Error(ResponseStatus.API_ERROR.toString())
        }
    }

    /**
     * This function is used to make the API call for Products lists.
     *
     * @param offset Determines whether to start returning data. The default value is 0.
     * @param limit This limits the number of results (for better performance and speed).
     *              The default value is 10.
     * */
    override suspend fun makeRemoteProductsListCall(
        offset: Int,
        limit: Int
    ): NetworkResultState<ResponseClass.ProductsResponse> {
        val productsListRequest = networkInstance.fetchProductsList(offset, limit)
        try {
            val productsListResult = productsListRequest.execute()
            LogUtils.e(screenName, "response code ${productsListResult.code()}")
            return if (productsListResult.isSuccessful && productsListResult.code()
                == CommonUtils.HTTP_OK_STATUS
            ) {
                LogUtils.e(
                    screenName, "productsListResult ${productsListResult.body().toString()}"
                )
                return productsListResult.body()?.let {
                    NetworkResultState.Success(ResponseStatus.NO_ISSUE.toString(), it)
                } ?: NetworkResultState.Error(ResponseStatus.EMPTY_API_LIST.toString())
            } else {
                NetworkResultState.Error(ResponseStatus.API_ERROR.toString())
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return NetworkResultState.Error(ResponseStatus.API_ERROR.toString())
        }
    }

    /**
     * This function will insert all the data received by API call into the DB in
     * PHOTO_DETAILS_LIST_TABLE.
     *
     * @param photoDetails The ImageListPhotos object.
     * */
    override suspend fun insertIntoTable(photoDetails: PhotoDetails) =
        appDatabase.photoDetailsListDataDao().insertIntoTable(photoDetails)

    /**
     * This function will make DB call to fetch data in ascending order from
     * PHOTO_DETAILS_LIST_TABLE.
     * */
    override suspend fun fetchAscendingListFromDB(): List<PhotoDetails> =
        appDatabase.photoDetailsListDataDao().ascendingOrderEntries()

    /**
     * This function will make DB call to fetch data in descending order form
     * PHOTO_DETAILS_LIST_TABLE.
     * */
    override suspend fun fetchDescendingListFromDB(): List<PhotoDetails> =
        appDatabase.photoDetailsListDataDao().descendingOrderEntries()

    /**
     * This function will clear the PHOTO_DETAILS_LIST_TABLE from DB.
     * */
    override suspend fun clearPhotoDetailsListTable() {
        if (appDatabase.photoDetailsListDataDao().getTableListCount() > 0) {
            appDatabase.photoDetailsListDataDao().clearTable()
        }
    }

}