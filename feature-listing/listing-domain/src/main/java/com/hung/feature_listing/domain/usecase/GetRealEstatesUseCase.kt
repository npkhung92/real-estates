package com.hung.feature_listing.domain.usecase

import androidx.paging.PagingData
import com.hung.core.domain.OneTimeUseCase
import com.hung.feature_listing.domain.model.RealEstateDomainModel
import com.hung.feature_listing.domain.repository.RealEstateRepository
import kotlinx.coroutines.flow.Flow

class GetRealEstatesUseCase(
    private val repository: RealEstateRepository
) : OneTimeUseCase<Unit, Flow<PagingData<RealEstateDomainModel>>> {
    override suspend fun invoke(request: Unit): Flow<PagingData<RealEstateDomainModel>> = repository.getRealEstates()
}