package com.hung.core.ui.component

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import kotlinx.collections.immutable.persistentListOf

private const val SHIMMER_TARGET_VALUE = 1000f
private const val SHIMMER_DEFAULT_DURATION = 1000
private const val SHIMMER_OFFSET_DELTA = 500f

@Composable
private fun shimmerColors() = persistentListOf(
    MaterialTheme.colorScheme.surfaceDim.copy(alpha = 0.2f),
    MaterialTheme.colorScheme.surfaceDim.copy(alpha = 0.4f),
    MaterialTheme.colorScheme.surfaceDim.copy(alpha = 0.6f),
    MaterialTheme.colorScheme.surfaceDim.copy(alpha = 0.4f),
    MaterialTheme.colorScheme.surfaceDim.copy(alpha = 0.2f)
)

/**
 * Animated shimmering background typically used as a placeholder for loading content.
 *
 * Draws a diagonal linear gradient that continuously translates to create a shimmer effect.
 * Apply this as a background to containers that represent content which is still loading.
 *
 * @param modifier [Modifier] to apply to the shimmer box.
 * @param duration Duration of one shimmer cycle in milliseconds. Defaults to [SHIMMER_DEFAULT_DURATION].
 */
@Composable
fun ShimmerBackground(modifier: Modifier = Modifier, duration: Int = SHIMMER_DEFAULT_DURATION) {
    val infiniteTransition = rememberInfiniteTransition(label = "Shimmer")
    val shimmerTranslate by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = SHIMMER_TARGET_VALUE,
        animationSpec = infiniteRepeatable(
            animation = tween(duration, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "ShimmerTranslate"
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors(),
        start = Offset(shimmerTranslate - SHIMMER_OFFSET_DELTA, shimmerTranslate - SHIMMER_OFFSET_DELTA),
        end = Offset(shimmerTranslate, shimmerTranslate)
    )

    Box(modifier = modifier.background(brush))
}