package com.hung.feature_listing.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.hung.core.presentation.DefaultErrorEvent
import com.hung.core.ui.BaseScreen
import com.hung.core.ui.EventHandler
import com.hung.core.ui.theme.MainApplicationTheme
import com.hung.feature_listing.presentation.RealEstateListLoadingState
import com.hung.feature_listing.presentation.RealEstateListPresentationEvent
import com.hung.feature_listing.presentation.RealEstateListPresentationState
import com.hung.feature_listing.presentation.RealEstateListViewModel
import com.hung.feature_listing.presentation.model.PricePresentationModel
import com.hung.feature_listing.presentation.model.RealEstatePresentationModel
import com.hung.feature_listing.ui.component.LocalSnackbarHostState
import com.hung.feature_listing.ui.component.RealEstateItem
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

private const val SKELETON_ITEM_COUNT = 4

@Composable
internal fun RealEstateListScreen(onNavigationBack: () -> Unit) {
    val snackbarState = LocalSnackbarHostState.current
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val eventHandler = remember {
        EventHandler { event ->
            when (event) {
                is DefaultErrorEvent -> {
                    scope.launch { snackbarState.showSnackbar(context.getString(R.string.generic_error_msg)) }
                }

                RealEstateListPresentationEvent.BookmarkedSuccess -> {
                    scope.launch { snackbarState.showSnackbar(context.getString(R.string.real_estate_list_screen_bookmarked_success_msg)) }
                }

                RealEstateListPresentationEvent.RemovedBookmarkSuccess -> {
                    scope.launch { snackbarState.showSnackbar(context.getString(R.string.real_estate_list_screen_removed_bookmark_success_msg)) }
                }
            }
        }
    }
    val onBack by rememberUpdatedState(onNavigationBack)
    BaseScreen<RealEstateListPresentationState, RealEstateListViewModel>(eventHandler) {
        val onAction = remember {
            { action: RealEstateListScreenUiAction ->
                when (action) {
                    RealEstateListScreenUiAction.Back -> onBack()
                    RealEstateListScreenUiAction.PullToRefresh -> viewModel.onRefreshAction()
                    is RealEstateListScreenUiAction.Bookmark -> viewModel.onBookmarkAction(
                        action.id,
                        action.marked
                    )
                }
            }
        }
        ScreenContent { state ->
            val realEstateList: LazyPagingItems<RealEstatePresentationModel> =
                state.pagingRealEstates.collectAsLazyPagingItems()
            Content(
                realEstates = realEstateList,
                loadingState = state.loadingState,
                onAction = onAction
            )
        }
    }
}

@Composable
private fun Content(
    realEstates: LazyPagingItems<RealEstatePresentationModel>,
    modifier: Modifier = Modifier,
    loadingState: RealEstateListLoadingState = RealEstateListLoadingState.Idle,
    onAction: (RealEstateListScreenUiAction) -> Unit,
) {
    val onActionHandling by rememberUpdatedState(onAction)
    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        modifier = modifier
            .fillMaxSize(),
        topBar = {
            TopBar(
                modifier = Modifier.fillMaxWidth(),
                onBackClick = {
                    onActionHandling(RealEstateListScreenUiAction.Back)
                },
            )
        },
        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            PullToRefreshBox(
                modifier = Modifier.weight(1f),
                isRefreshing = loadingState == RealEstateListLoadingState.Refreshing,
                onRefresh = { onActionHandling(RealEstateListScreenUiAction.PullToRefresh) }
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .imePadding(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp)
                ) {
                    // Loading default 4 skeleton items
                    if (loadingState == RealEstateListLoadingState.Loading) {
                        items(SKELETON_ITEM_COUNT) {
                            RealEstateItem(
                                modifier = Modifier
                                    .fillParentMaxWidth()
                                    .animateItem()
                            )
                        }
                    } else {
                        items(
                            realEstates.itemCount,
                            realEstates.itemKey { it.id }
                        ) { index ->
                            val realEstate = realEstates[index] ?: return@items
                            RealEstateItem(
                                modifier = Modifier
                                    .fillParentMaxWidth()
                                    .animateItem(),
                                realEstate = realEstate,
                            ) { isBookmarked ->
                                onActionHandling(
                                    RealEstateListScreenUiAction.Bookmark(
                                        realEstate.id,
                                        isBookmarked
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        windowInsets = WindowInsets(top = 0.dp),
        title = {
            Text(
                text = stringResource(R.string.real_estate_list_screen_title),
                fontWeight = FontWeight.Bold,
            )
        },
        modifier = modifier,
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
        navigationIcon = {
            IconButton(
                onClick = onBackClick
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = "Back icon"
                )
            }
        }
    )
}

@Preview
@Composable
private fun PreviewContent() {
    MainApplicationTheme {
        val mock = flowOf(
            PagingData.from(
                listOf(
                    RealEstatePresentationModel(
                        id = "1",
                        title = "asddas",
                        firstImageUrl = "",
                        bookmarked = false,
                        address = "",
                        price = PricePresentationModel.Available(10.0, "CHF")
                    )
                )
            )
        ).collectAsLazyPagingItems()
        Content(
            realEstates = mock,
            loadingState = RealEstateListLoadingState.Idle,
            onAction = {},
        )
    }
}