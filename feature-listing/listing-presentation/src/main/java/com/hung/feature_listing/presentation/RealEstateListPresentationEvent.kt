package com.hung.feature_listing.presentation

import com.hung.core.presentation.PresentationEvent


sealed interface RealEstateListPresentationEvent : PresentationEvent {
    data object BookmarkedSuccess : RealEstateListPresentationEvent
    data object RemovedBookmarkSuccess : RealEstateListPresentationEvent
}