package com.hung.feature_listing.presentation.model

data class RealEstatePresentationModel(
    val id: String,
    val firstImageUrl: String,
    val title: String,
    val price: String,
    val address: String,
    val bookmarked: Boolean
)
