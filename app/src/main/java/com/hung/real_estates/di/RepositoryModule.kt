package com.hung.real_estates.di

import com.hung.core.network.NetworkClient
import com.hung.feature_listing.data.RealEstateDataRepository
import com.hung.feature_listing.domain.repository.RealEstateRepository
import com.hung.real_estates.datasource.local.dao.BookmarkRealEstateDao
import com.hung.real_estates.datasource.local.dao.RealEstateDao
import com.hung.real_estates.datasource.remote.RealEstateApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun providesRealEstateRepository(
        networkClient: NetworkClient,
        remoteService: RealEstateApiService,
        realEstateDao: RealEstateDao,
        bookmarkRealEstateDao: BookmarkRealEstateDao,
    ): RealEstateRepository = RealEstateDataRepository(
        networkClient = networkClient,
        remoteService = remoteService,
        realEstateDao = realEstateDao,
        bookmarkRealEstateDao = bookmarkRealEstateDao,
    )
}