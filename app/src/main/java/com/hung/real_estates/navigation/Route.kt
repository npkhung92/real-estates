package com.hung.real_estates.navigation

import kotlinx.serialization.Serializable

sealed interface Route {
    @Serializable
    data object RealEstateList : Route

}