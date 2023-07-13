package com.example.practiceapps.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.practiceapps.database.AppDatabase
import com.example.practiceapps.database.model.UserListDetails
import com.example.practiceapps.model.LoaderStatus
import com.example.practiceapps.network.NetworkInstance
import com.example.practiceapps.network.ResponseClass
import com.example.practiceapps.utils.LogUtils
import com.example.practiceapps.utils.ResponseStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class UserListViewModel(appDatabase: AppDatabase) : ViewModel() {

    private val screenName = UserListViewModel::class.java.simpleName
    private val mAppDatabase = appDatabase

    private val _userListLiveData = MutableLiveData<MutableList<UserListDetails>>()
    val userListLiveData: MutableLiveData<MutableList<UserListDetails>> = _userListLiveData

    private val _loaderLiveData = MutableLiveData<LoaderStatus>()
    val loaderLiveData: MutableLiveData<LoaderStatus> = _loaderLiveData

    /**
     * This function is used to make the API call for User lists.
     *
     * @param pageNumber The page number for which the data is to be fetched.
     * */
    fun callUsersApi(pageNumber: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val userListRequest =
                    NetworkInstance.getInstance(NetworkInstance.USER_LIST_BASE_URL)
                        .getUserList(pageNumber)
                val userListResult = userListRequest.execute()
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
                            if (mAppDatabase.userListDataDao().getUserListCount() > 0) {
                                mAppDatabase.userListDataDao().clearUserListTable()
                            }

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
                                mAppDatabase.userListDataDao().insertIntoTable(data)
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
        }
    }

    /**
     * This function will make DB call to fetch data in ascending order.
     * */
    fun makeUserListAscending() {
        viewModelScope.launch {
            val ascendingList = mAppDatabase.userListDataDao().arrangeInAscendingOrder()
            _userListLiveData.postValue(ascendingList.toMutableList())
        }
    }

    /**
     * This function will make DB call to fetch data in descending order.
     * */
    fun makeUserListDescending() {
        viewModelScope.launch {
            val descendingList = mAppDatabase.userListDataDao().arrangeInDescendingOrder()
            _userListLiveData.postValue(descendingList.toMutableList())
        }
    }

    //ViewModelFactory
    class UserListViewModelFactory(private val appDatabase: AppDatabase) :
        ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(UserListViewModel::class.java)) {
                return UserListViewModel(appDatabase) as T
            }
            throw IllegalArgumentException("Unknown class name")
        }
    }

}