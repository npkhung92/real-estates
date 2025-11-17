package com.hung.feature_listing.ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal fun FavoriteButton(
    isFavorite: Boolean,
    modifier: Modifier = Modifier,
    onCheck: (Boolean) -> Unit = {},
) {
    IconButton(
        modifier = modifier,
        onClick = { onCheck(!isFavorite) }
    ) {
        Icon(
            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
            contentDescription = "Favorite button"
        )
    }
}