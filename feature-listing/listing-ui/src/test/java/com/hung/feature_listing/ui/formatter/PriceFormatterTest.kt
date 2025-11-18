package com.hung.feature_listing.ui.formatter

import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Locale

class PriceFormatterTest {

    @Test
    fun `format should format USD price correctly with US locale`() {
        // Given
        val formatter = PriceFormatter(Locale.US)
        val price = 1234567.89
        val currencyCode = "USD"

        // When
        val result = formatter.format(price, currencyCode)

        // Then
        assertEquals("$1,234,567.89", result)
    }

    @Test
    fun `format should format EUR price correctly with default locale`() {
        // Given
        val formatter = PriceFormatter()
        val price = 500000.0
        val currencyCode = "EUR"

        // When
        val result = formatter.format(price, currencyCode)

        // Then
        // German locale uses comma as decimal separator and dot as thousands separator
        assertEquals("€500,000", result)
    }

    @Test
    fun `format should format CHF price correctly with default locale`() {
        // Given
        val formatter = PriceFormatter()
        val price = 250000.50
        val currencyCode = "CHF"

        // When
        val result = formatter.format(price, currencyCode)

        // Then
        assertEquals("CHF250,000.5", result)
    }

    @Test
    fun `format should format zero price correctly`() {
        // Given
        val formatter = PriceFormatter(Locale.US)
        val price = 0.0
        val currencyCode = "USD"

        // When
        val result = formatter.format(price, currencyCode)

        // Then
        assertEquals("$0", result)
    }

    @Test
    fun `format should format negative price correctly`() {
        // Given
        val formatter = PriceFormatter(Locale.US)
        val price = -1000.0
        val currencyCode = "USD"

        // When
        val result = formatter.format(price, currencyCode)

        // Then
        assertEquals("-$1,000", result)
    }

    @Test
    fun `format should handle large numbers correctly`() {
        // Given
        val formatter = PriceFormatter(Locale.US)
        val price = 999999999.99
        val currencyCode = "USD"

        // When
        val result = formatter.format(price, currencyCode)

        // Then
        assertEquals("$999,999,999.99", result)
    }

    @Test
    fun `format should respect maximum fraction digits of 2`() {
        // Given
        val formatter = PriceFormatter(Locale.US)
        val price = 100.999
        val currencyCode = "USD"

        // When
        val result = formatter.format(price, currencyCode)

        // Then
        // Should round to 2 decimal places
        assertEquals("$101", result)
    }

    @Test
    fun `format should handle minimum fraction digits of 0`() {
        // Given
        val formatter = PriceFormatter(Locale.US)
        val price = 100.0
        val currencyCode = "USD"

        // When
        val result = formatter.format(price, currencyCode)

        // Then
        // Should not show decimal places for whole numbers
        assertEquals("$100", result)
    }
}

