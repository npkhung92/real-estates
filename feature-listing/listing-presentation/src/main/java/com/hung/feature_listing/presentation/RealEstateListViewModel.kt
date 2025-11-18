package com.hung.feature_listing.presentation

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.hung.core.presentation.BaseViewModel
import com.hung.core.presentation.DefaultErrorEvent
import com.hung.feature_listing.domain.model.BookmarkRequestDomainModel
import com.hung.feature_listing.domain.usecase.BookmarkRealEstateUseCase
import com.hung.feature_listing.domain.usecase.GetRealEstatesUseCase
import com.hung.feature_listing.domain.usecase.RefreshRealEstatesUseCase
import com.hung.feature_listing.presentation.mapper.toPresentation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.map
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

@HiltViewModel
@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class RealEstateListViewModel @Inject constructor(
    private val getRealEstatesUseCase: GetRealEstatesUseCase,
    private val refreshRealEstatesUseCase: RefreshRealEstatesUseCase,
    private val bookmarkRealEstateUseCase: BookmarkRealEstateUseCase,
) : BaseViewModel<RealEstateListPresentationState>(RealEstateListPresentationState()) {
    /** Guards the one-time initial refresh triggered from [onEnter]. */
    private val isInitialLoadTriggered = AtomicBoolean(false)
    override fun onEnter() {
        if (isInitialLoadTriggered.compareAndSet(false, true)) {
            retrieveRealEstates()
            onRefreshAction()
        }
    }

    private fun retrieveRealEstates() {
        launchUseCase(
            onError = { exception ->
                sendEvent(DefaultErrorEvent(exception?.message))
                updateState { it.copy(loadingState = RealEstateListLoadingState.Idle) }
            },
            onSuccess = { pagingData ->
                updateState {
                    it.copy(
                        loadingState = RealEstateListLoadingState.Idle, pagingRealEstates = pagingData
                            .map { paging -> paging.map { realEstate -> realEstate.toPresentation() } }
                            .cachedIn(viewModelScope))
                }
            },
            onStart = {
                updateState { it.copy(loadingState = RealEstateListLoadingState.Loading) }
            },
            request = Unit,
            useCaseBlock = getRealEstatesUseCase
        )
    }

    fun onRefreshAction() {
        launchUseCase(
            onError = { exception ->
                sendEvent(DefaultErrorEvent(exception?.message))
                updateState { it.copy(loadingState = RealEstateListLoadingState.Idle) }
            },
            onSuccess = {
                updateState { it.copy(loadingState = RealEstateListLoadingState.Idle) }
            },
            onStart = {
                updateState { it.copy(loadingState = RealEstateListLoadingState.Refreshing) }
            },
            request = Unit,
            useCaseBlock = refreshRealEstatesUseCase
        )
    }

    fun onBookmarkAction(id: String, marked: Boolean) {
        launchUseCase(
            onError = { exception ->
                sendEvent(DefaultErrorEvent(exception?.message))
            },
            onSuccess = {
                sendEvent(
                    if (marked) {
                        RealEstateListPresentationEvent.BookmarkedSuccess
                    } else {
                        RealEstateListPresentationEvent.RemovedBookmarkSuccess
                    }
                )
            },
            request = BookmarkRequestDomainModel(realEstateId = id, marked = marked),
            useCaseBlock = bookmarkRealEstateUseCase
        )
    }
}