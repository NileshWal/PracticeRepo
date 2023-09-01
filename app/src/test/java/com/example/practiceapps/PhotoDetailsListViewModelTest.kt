package com.example.practiceapps

import android.content.Context
import com.example.practiceapps.database.AppDatabase
import com.example.practiceapps.network.ApiInterface
import com.example.practiceapps.network.NetworkInstance
import com.example.practiceapps.repository.PhotoDetailsListRepository
import com.example.practiceapps.repository.PhotoDetailsListRepositoryImpl
import com.example.practiceapps.viewmodel.PhotoDetailsListViewModel
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock

class PhotoDetailsListViewModelTest {

    private lateinit var networkInstance: ApiInterface
    private lateinit var appDatabase: AppDatabase
    private lateinit var photoDetailsListRepository: PhotoDetailsListRepository
    private lateinit var viewModel: PhotoDetailsListViewModel

    @Before
    fun setup() {
        appDatabase = AppDatabase.getInstance(mock(Context::class.java))
        networkInstance = NetworkInstance.getInstance()
        photoDetailsListRepository = PhotoDetailsListRepositoryImpl(networkInstance, appDatabase)
        viewModel = PhotoDetailsListViewModel(photoDetailsListRepository)
    }

    @Test
    fun callUsersApiForZeroToTen() {
        viewModel.callUsersApi(0, 10)
    }
}