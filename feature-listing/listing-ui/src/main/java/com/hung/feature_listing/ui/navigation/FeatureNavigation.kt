package com.hung.feature_listing.ui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hung.feature_listing.ui.RealEstateListScreen

const val TRANSITION_TIME_IN_MILLIS = 300

@Composable
internal fun FeatureNavigation(onQuitFeature: () -> Unit) {
    val navController = rememberNavController()
    val isLtr = LocalLayoutDirection.current == LayoutDirection.Ltr
    NavHost(
        navController = navController, startDestination = Route.RealEstateList,
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { if (isLtr) it else -it },
                animationSpec = tween(TRANSITION_TIME_IN_MILLIS)
            )
        },
        exitTransition = {
            fadeOut(animationSpec = tween(TRANSITION_TIME_IN_MILLIS))
        },
        popExitTransition = {
            slideOutHorizontally(
                targetOffsetX = { if (isLtr) it else -it },
                animationSpec = tween(TRANSITION_TIME_IN_MILLIS)
            )
        },
        popEnterTransition = {
            fadeIn(animationSpec = tween(TRANSITION_TIME_IN_MILLIS))
        },
    ) {
        composable<Route.RealEstateList> {
            RealEstateListScreen(onQuitFeature)
        }
    }
}