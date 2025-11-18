package com.hung.feature_listing.domain.model

sealed interface AddressDomainModel {
    data object NotAvailable : AddressDomainModel
    data class Available(val street: String, val locality: String) : AddressDomainModel
}