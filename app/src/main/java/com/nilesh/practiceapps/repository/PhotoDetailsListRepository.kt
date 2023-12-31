package com.nilesh.practiceapps.repository

import com.nilesh.practiceapps.database.model.PhotoDetails
import com.nilesh.practiceapps.network.NetworkResultState
import com.nilesh.practiceapps.network.ResponseClass

interface PhotoDetailsListRepository {

    /**
     * This function is used to make the API call for Image lists.
     *
     * @param offset Determines whether to start returning data. The default value is 0.
     * @param limit This limits the number of results (for better performance and speed).
     *              The default value is 10.
     * */
    suspend fun makeRemotePhotoDetailsListCall(
        offset: Int,
        limit: Int
    ): NetworkResultState<ResponseClass.PhotoListResponse>

    /**
     * This function is used to make the API call for Products lists.
     *
     * @param offset Determines whether to start returning data. The default value is 0.
     * @param limit This limits the number of results (for better performance and speed).
     *              The default value is 10.
     * */
    suspend fun makeRemoteProductsListCall(
        offset: Int,
        limit: Int
    ): NetworkResultState<ResponseClass.ProductsResponse>

    /**
     * This function will insert all the data received by API call into the DB in
     * PHOTO_DETAILS_LIST_TABLE.
     *
     * @param photoDetails The ImageListPhotos object.
     * */
    suspend fun insertIntoTable(photoDetails: PhotoDetails)

    /**
     * This function will make DB call to fetch data in ascending order from
     * PHOTO_DETAILS_LIST_TABLE.
     * */
    suspend fun fetchAscendingListFromDB(): List<PhotoDetails>

    /**
     * This function will make DB call to fetch data in descending order form
     * PHOTO_DETAILS_LIST_TABLE.
     * */
    suspend fun fetchDescendingListFromDB(): List<PhotoDetails>

    /**
     * This function will clear the PHOTO_DETAILS_LIST_TABLE from DB.
     * */
    suspend fun clearPhotoDetailsListTable()

}