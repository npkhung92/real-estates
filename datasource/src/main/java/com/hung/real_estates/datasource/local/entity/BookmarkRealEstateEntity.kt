package com.hung.real_estates.datasource.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "bookmark_real_estates",
    primaryKeys = ["real_estate_id"],
    foreignKeys = [
        ForeignKey(
            entity = RealEstateEntity::class,
            parentColumns = ["id"],
            childColumns = ["real_estate_id"],
        )
    ]
)
data class BookmarkRealEstateEntity(
    @ColumnInfo(name = "real_estate_id")
    val realEstateId: String,
)