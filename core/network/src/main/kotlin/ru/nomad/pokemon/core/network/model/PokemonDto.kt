package ru.nomad.pokemon.core.network.model

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@OptIn(InternalSerializationApi::class)
data class PokemonDto(
    val name: String,
    val sprites: Sprites,
) {

    @Serializable
    @OptIn(InternalSerializationApi::class)
    data class Sprites(
        val other: Other,
    ) {

        @Serializable
        @OptIn(InternalSerializationApi::class)
        data class Other(
            @SerialName("dream_world")
            val dreamWorld: DreamWorld,
        ) {

            @Serializable
            @OptIn(InternalSerializationApi::class)
            data class DreamWorld(
                @SerialName("front_default")
                val frontDefault: String?,
            )
        }
    }
}
