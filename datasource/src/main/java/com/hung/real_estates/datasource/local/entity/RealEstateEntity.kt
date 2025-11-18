package com.hung.real_estates.datasource.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hung.real_estates.datasource.local.model.AddressLocalModel
import com.hung.real_estates.datasource.local.model.PriceLocalModel

@Entity(
    tableName = "real_estates"
)
data class RealEstateEntity(
    @PrimaryKey val id: String,
    val title: String? = null,
    val address: AddressLocalModel? = null,
    val price: PriceLocalModel? = null,
    @ColumnInfo(name = "first_url") val firstRealEstateUrl: String? = null,
)
