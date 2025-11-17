package com.hung.feature_listing.domain.repository

import androidx.paging.PagingData
import com.hung.feature_listing.domain.model.BookmarkRequestDomainModel
import com.hung.feature_listing.domain.model.RealEstateDomainModel
import kotlinx.coroutines.flow.Flow

interface RealEstateRepository {
    suspend fun getRealEstates(): Flow<PagingData<RealEstateDomainModel>>
    suspend fun refreshRealEstates()
    suspend fun bookmarkRealEstate(request: BookmarkRequestDomainModel)
}