package com.hung.real_estates.di

import androidx.paging.PagingData
import com.hung.feature_listing.domain.model.BookmarkRequestDomainModel
import com.hung.feature_listing.domain.model.RealEstateDomainModel
import com.hung.feature_listing.domain.repository.RealEstateRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun providesRealEstateRepository(
    ): RealEstateRepository = object : RealEstateRepository {
        override suspend fun getRealEstates(): Flow<PagingData<RealEstateDomainModel>> = flowOf(
            PagingData.from(
                listOf(
                    RealEstateDomainModel(
                        id = "104123262",
                        title = "LuxuriÃ¶ses Einfamilienhaus mit Pool - Musterinserat",
                        price = "9999999 CHF",
                        address = "Musterstrasse 999, La BrÃ©vine",
                        firstImageUrl = "https://media2.homegate.ch/listings/heia/104123262/image/6b53db714891bfe2321cc3a6d4af76e1.jpg",
                        bookmarked = false,
                    )
                )
            )
        )

        override suspend fun refreshRealEstates() {
            delay(2000)
        }

        override suspend fun bookmarkRealEstate(request: BookmarkRequestDomainModel) {
            delay(2000)
        }
    }
}