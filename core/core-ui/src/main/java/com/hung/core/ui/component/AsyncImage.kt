package com.hung.core.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImagePainter
import coil3.compose.LocalPlatformContext
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.rememberConstraintsSizeResolver
import coil3.request.ImageRequest
import coil3.request.crossfade

/**
 * A convenience Composable for loading and displaying remote images using Coil v3.
 *
 * This function wraps [SubcomposeAsyncImage] to provide:
 * - Optional shimmer placeholder while loading
 * - Consistent sizing via a remembered constraints size resolver
 *
 * @param url The primary image URL to load.
 * @param modifier The [Modifier] to be applied to the image container.
 * @param contentDescription Text used by accessibility services. Use null for decorative images.
 * @param contentScale How to scale the image within its bounds. Defaults to [ContentScale.Fit].
 * @param showShimmerLoading Whether to show a shimmer placeholder while the image is loading.
 */
@Composable
fun AsyncImage(
    url: String,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    contentScale: ContentScale = ContentScale.Fit,
    showShimmerLoading: Boolean = true,
) {
    val sizeResolver = rememberConstraintsSizeResolver()
    var finalUrl by remember { mutableStateOf(url) }
    val context = LocalPlatformContext.current
    SubcomposeAsyncImage(
        model = ImageRequest.Builder(context)
            .data(finalUrl)
            .crossfade(true)
            .size(sizeResolver)
            .build(),
        contentDescription = contentDescription,
        modifier = modifier,
        loading = {
            if (showShimmerLoading) {
                ShimmerBackground(Modifier.fillMaxSize())
            }
        },
        success = { successState ->
            Image(
                painter = successState.painter,
                contentDescription = contentDescription,
                modifier = Modifier
                    .fillMaxSize()
                    .then(sizeResolver),
                contentScale = contentScale
            )
        },
        error = { errorState ->
            ErrorImageState()
        }
    )
}

@Composable
private fun ErrorImageState(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.errorContainer)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Filled.Refresh,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.error,
        )
    }
}