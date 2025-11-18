package com.hung.real_estates.datasource.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PropertyDto(
    val id: String,
    val listing: ListingDto? = null
)

@Serializable
data class ListingDto(
    val id: String,
    @SerialName("prices")
    val price: PriceDto? = null,
    val address: AddressDto? = null,
    val localization: LocalizationWrapperDto? = null
)

@Serializable
data class PriceDto(
    val currency: String? = null,
    val buy: BuyPriceDto? = null
)

@Serializable
data class BuyPriceDto(
    val area: String? = null,
    val price: Double? = null
)

@Serializable
data class AddressDto(
    val locality: String? = null,
    val street: String? = null
)