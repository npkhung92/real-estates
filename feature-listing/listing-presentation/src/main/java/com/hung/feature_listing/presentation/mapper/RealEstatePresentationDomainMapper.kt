package com.hung.feature_listing.presentation.mapper

import com.hung.feature_listing.domain.model.RealEstateDomainModel
import com.hung.feature_listing.presentation.model.RealEstatePresentationModel

internal fun RealEstateDomainModel.toPresentation() = RealEstatePresentationModel(
    id = id,
    title = title,
    firstImageUrl = firstImageUrl,
    price = price,
    address = address,
    bookmarked = bookmarked
)