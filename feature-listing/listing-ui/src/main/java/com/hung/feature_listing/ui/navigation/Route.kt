package com.hung.feature_listing.ui.navigation

import kotlinx.serialization.Serializable

sealed interface Route {
    @Serializable
    data object RealEstateList : Route
}