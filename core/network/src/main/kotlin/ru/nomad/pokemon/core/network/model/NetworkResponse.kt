package ru.nomad.pokemon.core.network.model

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@Serializable
@OptIn(InternalSerializationApi::class)
data class NetworkResponse<T>(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<T>,
)

@Serializable
@OptIn(InternalSerializationApi::class)
data class NetworkResource(
    val name: String?,
    val url: String,
)
