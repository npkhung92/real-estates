package com.hung.feature_listing.domain.model

data class RealEstateDomainModel(
    val id: String,
    val firstImageUrl: String,
    val title: String,
    val price: String,
    val address: String,
    val bookmarked: Boolean
)