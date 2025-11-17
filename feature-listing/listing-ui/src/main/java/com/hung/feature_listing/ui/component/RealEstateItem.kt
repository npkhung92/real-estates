package com.hung.feature_listing.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hung.core.ui.component.AsyncImage
import com.hung.core.ui.component.ShimmerBackground
import com.hung.core.ui.theme.MainApplicationTheme
import com.hung.feature_listing.presentation.model.RealEstatePresentationModel

@Composable
internal fun RealEstateItem(
    modifier: Modifier = Modifier,
    realEstate: RealEstatePresentationModel? = null,
    onBookmarkCheck: (Boolean) -> Unit = {}
) {
    val itemModifier = modifier.aspectRatio(1.5f)
    if (realEstate == null) {
        LoadingSkeletonItem(modifier = itemModifier)
        return
    }
    Card(
        modifier = itemModifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        RealEstateImage(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            imageUrl = realEstate.firstImageUrl,
            price = realEstate.price,
            isBookmarked = realEstate.bookmarked,
            onBookmarkCheck = onBookmarkCheck
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, start = 16.dp, end = 16.dp),
            text = realEstate.title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = "Address icon"
            )
            Text(
                modifier = Modifier.weight(1f),
                text = realEstate.address,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun RealEstateImage(
    imageUrl: String,
    price: String,
    isBookmarked: Boolean,
    modifier: Modifier = Modifier,
    onBookmarkCheck: (Boolean) -> Unit
) {
    Box(modifier = modifier) {
        AsyncImage(
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillWidth,
            url = imageUrl,
            contentDescription = "Real estate first image"
        )
        Text(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(bottom = 24.dp)
                .background(
                    shape = RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp),
                    color = MaterialTheme.colorScheme.tertiaryContainer
                )
                .padding(12.dp),
            text = price,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.tertiary,
        )
        FavoriteButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 24.dp)
                .background(
                    shape = RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp),
                    color = MaterialTheme.colorScheme.primaryContainer
                ),
            isFavorite = isBookmarked,
            onCheck = onBookmarkCheck
        )
    }
}

@Composable
private fun LoadingSkeletonItem(modifier: Modifier = Modifier) {
    ShimmerBackground(
        modifier = modifier.clip(shape = MaterialTheme.shapes.medium),
    )
}

@Preview
@Composable
private fun PreviewItem() {
    MainApplicationTheme {
        RealEstateItem(
            realEstate = RealEstatePresentationModel(
                id = "1",
                title = "asddas",
                firstImageUrl = "",
                bookmarked = false,
                address = "asdnaslkdjna, asdkas",
                price = "12 chf"
            ),
        )
    }
}