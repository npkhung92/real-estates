package com.hung.real_estates.di

import com.hung.feature_listing.domain.repository.RealEstateRepository
import com.hung.feature_listing.domain.usecase.BookmarkRealEstateUseCase
import com.hung.feature_listing.domain.usecase.GetRealEstatesUseCase
import com.hung.feature_listing.domain.usecase.RefreshRealEstatesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun providesGetRealEstateListUseCase(repository: RealEstateRepository) = GetRealEstatesUseCase(repository)

    @Provides
    fun providesRefreshRealEstateListUseCase(repository: RealEstateRepository) = RefreshRealEstatesUseCase(repository)

    @Provides
    fun providesBookmarkRealEstateUseCase(repository: RealEstateRepository) = BookmarkRealEstateUseCase(repository)
}