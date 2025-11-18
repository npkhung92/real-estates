package com.hung.real_estates.datasource.local.model

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class AddressLocalModel(
    val locality: String? = null,
    val street: String? = null
)

@ProvidedTypeConverter
class AddressLocalModelConverter(private val json: Json) {
    @TypeConverter
    fun from(address: AddressLocalModel): String {
        return json.encodeToString(address)
    }

    @TypeConverter
    fun to(addressValue: String): AddressLocalModel {
        return json.decodeFromString(addressValue)
    }
}