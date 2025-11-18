package com.hung.feature_listing.domain.repository

import com.hung.feature_listing.domain.model.BookmarkRequestDomainModel
import com.hung.feature_listing.domain.model.RealEstateDomainModel
import kotlinx.coroutines.flow.Flow

interface RealEstateRepository {
    suspend fun getRealEstates(): Flow<List<RealEstateDomainModel>>
    suspend fun refreshRealEstates()
    suspend fun bookmarkRealEstate(request: BookmarkRequestDomainModel)
}