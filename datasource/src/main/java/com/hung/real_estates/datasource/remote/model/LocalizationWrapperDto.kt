package com.hung.real_estates.datasource.remote.model

import com.hung.real_estates.datasource.remote.serializer.LocalizationWrapperDtoSerializer
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@Serializable(with = LocalizationWrapperDtoSerializer::class)
@OptIn(ExperimentalSerializationApi::class)
data class LocalizationWrapperDto(
    val primary: String? = null,
    @JsonNames("de")
    val languages: Map<String, LocalizationContentDto> = emptyMap()
)

@Serializable
data class LocalizationContentDto(
    val attachments: List<AttachmentDto>? = null,
    val text: TextDto? = null
)

@Serializable
data class AttachmentDto(
    val type: String? = null,
    val url: String? = null,
    val file: String? = null,
)

@Serializable
data class TextDto(val title: String? = null)