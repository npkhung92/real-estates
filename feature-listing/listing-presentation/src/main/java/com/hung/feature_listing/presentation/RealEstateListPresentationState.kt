package com.hung.feature_listing.presentation

import androidx.paging.PagingData
import com.hung.core.presentation.PresentationState
import com.hung.feature_listing.presentation.model.RealEstatePresentationModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class RealEstateListPresentationState(
    val loadingState: RealEstateListLoadingState = RealEstateListLoadingState.Idle,
    val pagingRealEstates: Flow<PagingData<RealEstatePresentationModel>> = emptyFlow()
) : PresentationState

sealed interface RealEstateListLoadingState {
    object Idle : RealEstateListLoadingState
    object Loading : RealEstateListLoadingState
    object Refreshing : RealEstateListLoadingState
}