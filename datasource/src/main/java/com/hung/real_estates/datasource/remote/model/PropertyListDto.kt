package com.hung.real_estates.datasource.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class PropertyListDto(
    val results: List<PropertyDto>? = null
)