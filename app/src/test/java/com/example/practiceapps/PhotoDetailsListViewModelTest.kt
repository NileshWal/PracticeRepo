package com.example.practiceapps

import com.example.practiceapps.repository.PhotoDetailsListRepository
import com.example.practiceapps.viewmodel.PhotoDetailsListViewModel
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class PhotoDetailsListViewModelTest {

    private val photoDetailsListRepository: PhotoDetailsListRepository =
        mock(PhotoDetailsListRepository::class.java)

    private lateinit var viewModel: PhotoDetailsListViewModel

    @Before
    fun setup() {
        viewModel = PhotoDetailsListViewModel(photoDetailsListRepository)
    }

    @Test
    fun callUsersApiForZeroToTen() {
        viewModel.callUsersApi(0, 10)
    }
}