package com.hung.real_estates.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hung.feature_listing.ui.RealEstateListFeatureEntry

@Composable
internal fun MainNavigation(onQuitApplication: () -> Unit) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Route.RealEstateList) {
        composable<Route.RealEstateList> {
            RealEstateListFeatureEntry(onQuitApplication)
        }
    }
}