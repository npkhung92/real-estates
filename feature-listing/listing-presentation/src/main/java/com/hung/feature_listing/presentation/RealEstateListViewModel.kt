package com.hung.feature_listing.presentation

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
        retrieveRealEstates()
        if (isInitialLoadTriggered.compareAndSet(false, true)) {
            onRefreshAction(true)
        }
    }

    private fun retrieveRealEstates() {
        launchUseCase(
            onError = { exception ->
                sendEvent(DefaultErrorEvent(exception?.message))
            },
            onSuccess = { result ->
                updateState {
                    it.copy(
                        realEstates = result.map { realEstate -> realEstate.toPresentation() }
                    )
                }
            },
            request = Unit,
            useCaseBlock = getRealEstatesUseCase
        )
    }

    fun onRefreshAction(isInitialLoading: Boolean = false) {
        launchUseCase(
            onError = { exception ->
                sendEvent(DefaultErrorEvent(exception?.message))
                updateState { it.copy(loadingState = RealEstateListLoadingState.Idle) }
            },
            onSuccess = {
                updateState { it.copy(loadingState = RealEstateListLoadingState.Idle) }
            },
            onStart = {
                updateState { it.copy(loadingState = if (isInitialLoading) RealEstateListLoadingState.Loading else RealEstateListLoadingState.Refreshing) }
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