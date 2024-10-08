package com.nilesh.practiceapps.hilt

import android.content.Context
import com.nilesh.practiceapps.database.AppDatabase
import com.nilesh.practiceapps.network.ApiInterface
import com.nilesh.practiceapps.network.NetworkInstance
import com.nilesh.practiceapps.repository.PhotoDetailsListRepository
import com.nilesh.practiceapps.repository.PhotoDetailsListRepositoryImpl
import com.nilesh.practiceapps.repository.UserRecordsRepository
import com.nilesh.practiceapps.repository.UserRecordsRepositoryImpl
import com.nilesh.practiceapps.ui.adapter.PhotoDetailsAdapter
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
    fun providesNetworkInstance(): ApiInterface = NetworkInstance.getInstance(NetworkInstance.NORMAL_BASE_URL)

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