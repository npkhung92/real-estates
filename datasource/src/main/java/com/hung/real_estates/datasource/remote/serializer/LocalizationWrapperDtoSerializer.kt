package com.hung.real_estates.datasource.remote.serializer

import com.hung.real_estates.datasource.remote.model.LocalizationContentDto
import com.hung.real_estates.datasource.remote.model.LocalizationWrapperDto
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.put

object LocalizationWrapperDtoSerializer : KSerializer<LocalizationWrapperDto> {

    private const val PRIMARY_KEY = "primary"
    private const val LANGUAGES_KEY = "languages"
    private const val DESCRIPTOR = "LocalizationWrapperDto"

    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor(DESCRIPTOR) {
            element<String>(PRIMARY_KEY)
            element<Map<String, LocalizationContentDto>>(LANGUAGES_KEY)
        }

    override fun deserialize(decoder: Decoder): LocalizationWrapperDto {
        val input = decoder as? JsonDecoder
            ?: error("This serializer works only with JSON")

        val jsonObject = input.decodeJsonElement().jsonObject

        val primary = jsonObject[PRIMARY_KEY]!!.jsonPrimitive.content

        val languages = jsonObject
            .filterKeys { it != PRIMARY_KEY }
            .mapValues { (_, value) ->
                input.json.decodeFromJsonElement<LocalizationContentDto>(value)
            }

        return LocalizationWrapperDto(primary, languages)
    }

    override fun serialize(encoder: Encoder, value: LocalizationWrapperDto) {
        val jsonEncoder = encoder as? JsonEncoder
            ?: error("Only JSON supported")

        val json = buildJsonObject {
            put(PRIMARY_KEY, value.primary)
            value.languages.forEach { (lang, content) ->
                put(lang, jsonEncoder.json.encodeToJsonElement(content))
            }
        }
        jsonEncoder.encodeJsonElement(json)
    }
}