package com.hung.feature_listing.domain.usecase

import com.hung.core.domain.ContinuousUseCase
import com.hung.feature_listing.domain.model.RealEstateDomainModel
import com.hung.feature_listing.domain.repository.RealEstateRepository

class GetRealEstatesUseCase(
    private val repository: RealEstateRepository
) : ContinuousUseCase<Unit, List<RealEstateDomainModel>> {
    override suspend fun invoke(
        request: Unit,
        onResult: (List<RealEstateDomainModel>) -> Unit
    ) {
        repository.getRealEstates().collect {
            onResult(it)
        }
    }
}