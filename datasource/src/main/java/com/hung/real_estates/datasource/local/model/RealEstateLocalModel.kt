package com.hung.real_estates.datasource.local.model

import androidx.room.Embedded
import com.hung.real_estates.datasource.local.entity.RealEstateEntity

data class RealEstateLocalModel(
    @Embedded val realEstate: RealEstateEntity,
    val isBookmarked: Boolean
)