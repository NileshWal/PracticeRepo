package com.nilesh.practiceapps.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nilesh.practiceapps.network.NetworkInstance
import com.nilesh.practiceapps.network.ResponseClass
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class UserListViewModel @Inject constructor() : ViewModel() {

    private val _dataList = MutableLiveData<List<ResponseClass.UserData>>()
    val dataList: LiveData<List<ResponseClass.UserData>> = _dataList

    fun fetchUserList() {
        viewModelScope.launch(Dispatchers.IO) {
            val call = NetworkInstance.getInstance(NetworkInstance.COMPOSE_BASE_URL).fetchUserList()
            if (call.isNotEmpty()) {
                _dataList.postValue(call)
            } else {
                //TODO: Code a proper error case handling.
            }
        }
    }

}