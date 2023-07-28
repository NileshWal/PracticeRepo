package com.example.practiceapps.repository

import androidx.lifecycle.MutableLiveData
import com.example.practiceapps.database.AppDatabase
import com.example.practiceapps.database.model.UserListDetails
import com.example.practiceapps.model.LoaderStatus
import com.example.practiceapps.network.ApiInterface
import com.example.practiceapps.network.ResponseClass
import com.example.practiceapps.utils.LogUtils
import com.example.practiceapps.utils.ResponseStatus


class UserListRepository(
    private val networkInstance: ApiInterface,
    private val appDatabase: AppDatabase
) {

    private val screenName = UserListRepository::class.java.simpleName
    private val _loaderLiveData = MutableLiveData<LoaderStatus>()
    private val _userListLiveData = MutableLiveData<MutableList<UserListDetails>>()

    fun getLoaderLivedata() = _loaderLiveData
    fun getUserListLivedata() = _userListLiveData

    /**
     * This function is used to make the API call for User lists.
     *
     * @param pageNumber The page number for which the data is to be fetched.
     * */
    suspend fun makeRemoteUserApiCall(pageNumber: Int) {
        val userListRequest = networkInstance.getUserList(pageNumber)
        val userListResult = userListRequest.execute()

        LogUtils.e(screenName, "response code ${userListResult.code()}")

        if (userListResult.isSuccessful && userListResult.code() == 200) {
            LogUtils.e(
                screenName, "userListResult ${userListResult.body().toString()}"
            )
            userListResult.body()?.let { userResponse ->
                if (userResponse.data.isEmpty()) {
                    _loaderLiveData.postValue(
                        LoaderStatus(
                            false,
                            ResponseStatus.EMPTY_API_LIST
                        )
                    )
                } else {
                    val duplicateArray = ArrayList<ResponseClass.Data>()
                    duplicateArray.addAll(userResponse.data)
                    duplicateArray.addAll(userResponse.data)
                    val parsedArray = ArrayList<UserListDetails>()

                    //Clear the DB of already existing user list.
                    clearUserListDB()

                    duplicateArray.forEach { userDetail ->
                        val data = UserListDetails(
                            userDetail.id,
                            userDetail.email,
                            userDetail.firstName,
                            userDetail.lastName,
                            userDetail.avatar
                        )
                        parsedArray.add(data)
                        //Add user details to DB.
                        insertIntoTable(data)
                    }
                    //Add user details to livedata.
                    _userListLiveData.postValue(parsedArray)
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
     * This function will insert all the data received by API call into the DB in USER_DETAIL_TABLE.
     * */
    private suspend fun insertIntoTable(userListDetails: UserListDetails) {
        appDatabase.userListDataDao().insertIntoTable(userListDetails)
    }

    /**
     * This function will make DB call to fetch data in ascending order from USER_DETAIL_TABLE.
     * */
    suspend fun fetchAscendingUserListFromDB() {
        val ascendingList = appDatabase.userListDataDao().arrangeInAscendingOrder()
        _userListLiveData.postValue(ascendingList.toMutableList())
        _loaderLiveData.postValue(LoaderStatus(false, ResponseStatus.NO_ISSUE))
    }

    /**
     * This function will make DB call to fetch data in descending order form USER_DETAIL_TABLE.
     * */
    suspend fun fetchDescendingUserListFromDB() {
        val descendingList = appDatabase.userListDataDao().arrangeInDescendingOrder()
        _userListLiveData.postValue(descendingList.toMutableList())
        _loaderLiveData.postValue(LoaderStatus(false, ResponseStatus.NO_ISSUE))
    }

    /**
     * This function will clear the USER_DETAIL_TABLE from DB.
     * */
    private suspend fun clearUserListDB() {
        if (appDatabase.userListDataDao().getUserListCount() > 0) {
            appDatabase.userListDataDao().clearUserListTable()
        }
    }

}