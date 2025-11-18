package com.hung.real_estates.datasource.local.model

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class PriceLocalModel(
    val price: Double = 0.0,
    val currency: String? = null
)

@ProvidedTypeConverter
class PriceLocalModelConverter(private val json: Json) {
    @TypeConverter
    fun from(price: PriceLocalModel): String {
        return json.encodeToString(price)
    }

    @TypeConverter
    fun to(price: String): PriceLocalModel {
        return json.decodeFromString(price)
    }
}
