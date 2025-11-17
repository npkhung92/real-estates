package com.hung.feature_listing.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.hung.feature_listing.ui.navigation.FeatureNavigation
import com.hung.feature_listing.ui.component.LocalSnackbarHostState

@Composable
fun RealEstateListFeatureEntry(onBackFeature: () -> Unit) {
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Surface(
            modifier = Modifier.padding(innerPadding),
        ) {
            CompositionLocalProvider(LocalSnackbarHostState provides snackbarHostState) {
                FeatureNavigation(onBackFeature)
            }
        }
    }
}