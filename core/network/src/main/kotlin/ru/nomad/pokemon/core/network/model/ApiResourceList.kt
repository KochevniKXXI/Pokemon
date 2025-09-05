package ru.nomad.pokemon.core.network.model

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@Serializable
@OptIn(InternalSerializationApi::class)
data class ApiResourceList(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<ApiResource>,
)

@Serializable
@OptIn(InternalSerializationApi::class)
data class ApiResource(
    val name: String?,
    val url: String,
)
