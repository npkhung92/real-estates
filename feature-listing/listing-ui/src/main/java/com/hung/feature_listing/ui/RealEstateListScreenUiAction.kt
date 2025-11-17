package com.hung.feature_listing.ui

sealed interface RealEstateListScreenUiAction {
    data object Back : RealEstateListScreenUiAction
    data object PullToRefresh : RealEstateListScreenUiAction
    data class Bookmark(val id: String, val marked: Boolean) : RealEstateListScreenUiAction
}