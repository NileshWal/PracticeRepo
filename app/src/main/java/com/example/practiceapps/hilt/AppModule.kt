package com.example.practiceapps.hilt

import android.content.Context
import com.example.practiceapps.database.AppDatabase
import com.example.practiceapps.network.ApiInterface
import com.example.practiceapps.network.NetworkInstance
import com.example.practiceapps.repository.PhotoDetailsListRepository
import com.example.practiceapps.repository.PhotoDetailsListRepositoryImpl
import com.example.practiceapps.repository.UserRecordsRepository
import com.example.practiceapps.repository.UserRecordsRepositoryImpl
import com.example.practiceapps.ui.adapter.PhotoDetailsAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesDatabase(@ApplicationContext context: Context): AppDatabase =
        AppDatabase.getInstance(context)

    @Provides
    @Singleton
    fun providesNetworkInstance(): ApiInterface = NetworkInstance.getInstance()

    @Provides
    @Singleton
    fun providesPhotoDetailsListRepository(
        networkInstance: ApiInterface,
        appDatabase: AppDatabase
    ): PhotoDetailsListRepository =
        PhotoDetailsListRepositoryImpl(networkInstance, appDatabase)

    @Provides
    @Singleton
    fun providesUserRecordsRepository(
        networkInstance: ApiInterface,
        appDatabase: AppDatabase
    ): UserRecordsRepository =
        UserRecordsRepositoryImpl(networkInstance, appDatabase)

    @Provides
    @Singleton
    fun providesPhotoDetailsAdapter(@ApplicationContext context: Context): PhotoDetailsAdapter =
        PhotoDetailsAdapter(context, mutableListOf())
}