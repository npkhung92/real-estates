package com.hung.feature_listing.ui.formatter

import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

class PriceFormatter(
    private val locale: Locale = Locale.getDefault()
) {
    fun format(price: Double, currencyCode: String): String {
        return NumberFormat.getCurrencyInstance(locale).apply {
            currency = Currency.getInstance(currencyCode)
            maximumFractionDigits = 2
            minimumFractionDigits = 0
            isGroupingUsed = true
        }.format(price)
    }
}