package com.example.practiceapps.hilt

import android.content.Context
import com.example.practiceapps.database.AppDatabase
import com.example.practiceapps.network.ApiInterface
import com.example.practiceapps.network.NetworkInstance
import com.example.practiceapps.repository.PhotoDetailsListRepository
import com.example.practiceapps.repository.UserRecordsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun providesDatabase(@ApplicationContext context: Context): AppDatabase =
        AppDatabase.getInstance(context)

    @Provides
    fun providesNetworkInstance(): ApiInterface = NetworkInstance.getInstance()

    @Provides
    fun providesPhotoDetailsListRepository(
        networkInstance: ApiInterface,
        appDatabase: AppDatabase
    ): PhotoDetailsListRepository =
        PhotoDetailsListRepository(networkInstance, appDatabase)

    @Provides
    fun providesUserRecordsRepository(
        networkInstance: ApiInterface,
        appDatabase: AppDatabase
    ): UserRecordsRepository =
        UserRecordsRepository(networkInstance, appDatabase)
}