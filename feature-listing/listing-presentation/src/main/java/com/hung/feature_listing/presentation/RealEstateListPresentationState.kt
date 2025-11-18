package com.hung.feature_listing.presentation

import com.hung.core.presentation.PresentationState
import com.hung.feature_listing.presentation.model.RealEstatePresentationModel

data class RealEstateListPresentationState(
    val loadingState: RealEstateListLoadingState = RealEstateListLoadingState.Loading,
    val realEstates: List<RealEstatePresentationModel> = emptyList()
) : PresentationState

sealed interface RealEstateListLoadingState {
    object Idle : RealEstateListLoadingState
    object Loading : RealEstateListLoadingState
    object Refreshing : RealEstateListLoadingState
}