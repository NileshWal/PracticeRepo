package com.nilesh.practiceapps

import android.content.Context
import com.nilesh.practiceapps.database.AppDatabase
import com.nilesh.practiceapps.network.ApiInterface
import com.nilesh.practiceapps.network.NetworkInstance
import com.nilesh.practiceapps.repository.PhotoDetailsListRepository
import com.nilesh.practiceapps.repository.PhotoDetailsListRepositoryImpl
import com.nilesh.practiceapps.viewmodel.PhotoDetailsListViewModel
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
        networkInstance = NetworkInstance.getInstance(NetworkInstance.NORMAL_BASE_URL)
        photoDetailsListRepository = PhotoDetailsListRepositoryImpl(networkInstance, appDatabase)
        viewModel = PhotoDetailsListViewModel(photoDetailsListRepository)
    }

    @Test
    fun callUsersApiForZeroToTen() {
        viewModel.callUsersApi(0, 10)
    }
}