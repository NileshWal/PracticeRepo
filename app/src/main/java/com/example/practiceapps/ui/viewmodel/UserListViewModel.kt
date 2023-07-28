package com.example.practiceapps.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.practiceapps.database.model.UserListDetails
import com.example.practiceapps.model.LoaderStatus
import com.example.practiceapps.repository.UserListRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class UserListViewModel(userListRepository: UserListRepository) : ViewModel() {

    private val mAppRepository = userListRepository
    val userListLiveData: MutableLiveData<MutableList<UserListDetails>> =
        mAppRepository.getUserListLivedata()
    val loaderLiveData: MutableLiveData<LoaderStatus> = mAppRepository.getLoaderLivedata()

    /**
     * This function is used to make the API call from the UserListRepository for User lists.
     *
     * @param pageNumber The page number for which the data is to be fetched.
     * */
    fun callUsersApi(pageNumber: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                mAppRepository.makeRemoteUserApiCall(pageNumber)
            }
        }
    }

    /**
     * This function will fetch data in ascending order the UserListRepository.
     * */
    fun makeUserListAscending() {
        viewModelScope.launch {
            mAppRepository.fetchAscendingUserListFromDB()
        }
    }

    /**
     * This function will fetch data in descending order from the UserListRepository.
     * */
    fun makeUserListDescending() {
        viewModelScope.launch {
            mAppRepository.fetchDescendingUserListFromDB()
        }
    }

    //ViewModelFactory
    class UserListViewModelFactory(private val appDatabase: UserListRepository) :
        ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(UserListViewModel::class.java)) {
                return UserListViewModel(appDatabase) as T
            }
            throw IllegalArgumentException("Unknown class name")
        }
    }

}