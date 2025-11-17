package com.hung.feature_listing.domain.usecase

import com.hung.core.domain.OneTimeUseCase
import com.hung.feature_listing.domain.repository.RealEstateRepository

class RefreshRealEstatesUseCase(
    private val repository: RealEstateRepository
) : OneTimeUseCase<Unit, Unit> {
    override suspend fun invoke(request: Unit) {
        repository.refreshRealEstates()
    }
}