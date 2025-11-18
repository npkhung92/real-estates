package com.hung.feature_listing.presentation.model

sealed interface PricePresentationModel {
    data object NotAvailable : PricePresentationModel
    data class Available(val price: Double, val currency: String) : PricePresentationModel
}