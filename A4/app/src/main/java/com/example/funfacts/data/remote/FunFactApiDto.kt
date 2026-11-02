package com.example.funfacts.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data Transfer Object for deserializing fun facts from uselessfacts.jsph.pl API.
 */
@Serializable
data class FunFactApiDto(
    val id: String = "",
    val text: String = "",
    val source: String? = null,
    @SerialName("source_url") val sourceUrl: String? = null,
    val language: String? = null,
    val permalink: String? = null
)
