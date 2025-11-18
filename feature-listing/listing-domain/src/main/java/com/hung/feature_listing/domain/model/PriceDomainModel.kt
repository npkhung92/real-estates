package com.hung.feature_listing.domain.model

sealed interface PriceDomainModel {
    data object NotAvailable : PriceDomainModel
    data class Available(val price: Double, val currency: String) : PriceDomainModel
}