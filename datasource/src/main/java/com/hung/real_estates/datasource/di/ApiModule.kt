package com.hung.real_estates.datasource.di

import com.hung.core.network.NetworkClient
import com.hung.real_estates.datasource.remote.RealEstateApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Singleton
    @Provides
    fun providesRealEstateApiService(networkClient: NetworkClient): RealEstateApiService {
        return networkClient.createService(RealEstateApiService::class.java)
    }
}