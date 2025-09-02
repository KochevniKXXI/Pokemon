package ru.nomad.pokemon.core.network.model

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@Serializable
@OptIn(InternalSerializationApi::class)
data class NetworkResponse(
    val results: List<NetworkResource>,
)

@Serializable
@OptIn(InternalSerializationApi::class)
data class NetworkResource(
    val name: String?,
    val url: String,
)
