package com.hung.feature_listing.data.mapper

import com.hung.feature_listing.domain.model.AddressDomainModel
import com.hung.feature_listing.domain.model.PriceDomainModel
import com.hung.feature_listing.domain.model.RealEstateDomainModel
import com.hung.real_estates.datasource.local.entity.RealEstateEntity
import com.hung.real_estates.datasource.local.model.AddressLocalModel
import com.hung.real_estates.datasource.local.model.PriceLocalModel
import com.hung.real_estates.datasource.local.model.RealEstateLocalModel
import com.hung.real_estates.datasource.remote.model.AddressDto
import com.hung.real_estates.datasource.remote.model.LocalizationWrapperDto
import com.hung.real_estates.datasource.remote.model.PriceDto
import com.hung.real_estates.datasource.remote.model.PropertyDto

internal fun PropertyDto.toEntity() = RealEstateEntity(
    id = id,
    title = listing?.localization?.toLocalizedTitle(),
    address = listing?.address?.toLocal(),
    price = listing?.price?.toLocal(),
    firstRealEstateUrl = listing?.localization?.toLocalizedUrl()
)

internal fun RealEstateLocalModel.toDomain() = RealEstateDomainModel(
    id = realEstate.id,
    title = realEstate.title.orEmpty(),
    address = realEstate.address?.toDomain() ?: AddressDomainModel.NotAvailable,
    price = realEstate.price?.toDomain() ?: PriceDomainModel.NotAvailable,
    firstImageUrl = realEstate.firstRealEstateUrl.orEmpty(),
    bookmarked = isBookmarked
)

private fun AddressDto.toLocal() = AddressLocalModel(
    street = street,
    locality = locality
)

private fun AddressLocalModel.toDomain() = locality?.let {
    AddressDomainModel.Available(
        street = street.orEmpty(),
        locality = it
    )
} ?: AddressDomainModel.NotAvailable

private fun PriceLocalModel.toDomain() = currency?.let {
    PriceDomainModel.Available(
        price = price,
        currency = it
    )
} ?: PriceDomainModel.NotAvailable

private fun PriceDto.toLocal() = PriceLocalModel(
    price = buy?.price ?: 0.0,
    currency = currency
)

private fun LocalizationWrapperDto.toLocalizedTitle() = this.languages[this.primary]?.text?.title
private fun LocalizationWrapperDto.toLocalizedUrl() = this.languages[this.primary]?.attachments?.firstOrNull()?.url