package com.hung.feature_listing.domain.usecase

import com.hung.core.domain.OneTimeUseCase
import com.hung.feature_listing.domain.model.BookmarkRequestDomainModel
import com.hung.feature_listing.domain.repository.RealEstateRepository

class BookmarkRealEstateUseCase(
    private val repository: RealEstateRepository
) : OneTimeUseCase<BookmarkRequestDomainModel, Unit> {
    override suspend fun invoke(request: BookmarkRequestDomainModel) {
        repository.bookmarkRealEstate(request)
    }
}