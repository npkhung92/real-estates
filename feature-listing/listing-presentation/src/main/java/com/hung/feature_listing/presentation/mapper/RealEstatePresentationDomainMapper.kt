package com.hung.feature_listing.presentation.mapper

import com.hung.feature_listing.domain.model.AddressDomainModel
import com.hung.feature_listing.domain.model.PriceDomainModel
import com.hung.feature_listing.domain.model.RealEstateDomainModel
import com.hung.feature_listing.presentation.model.PricePresentationModel
import com.hung.feature_listing.presentation.model.RealEstatePresentationModel

internal fun RealEstateDomainModel.toPresentation() = RealEstatePresentationModel(
    id = id,
    title = title,
    firstImageUrl = firstImageUrl,
    price = price.toPresentation(),
    address = address.toAddressValue(),
    bookmarked = bookmarked
)

private fun AddressDomainModel.toAddressValue() = if (this is AddressDomainModel.Available) {
    "$street, $locality".takeIf { street.isNotBlank() } ?: locality
} else {
    ""
}

private fun PriceDomainModel.toPresentation() = when (this) {
    is PriceDomainModel.Available -> PricePresentationModel.Available(price, currency)
    PriceDomainModel.NotAvailable -> PricePresentationModel.NotAvailable
}