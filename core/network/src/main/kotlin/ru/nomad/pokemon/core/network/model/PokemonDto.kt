package ru.nomad.pokemon.core.network.model

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@OptIn(InternalSerializationApi::class)
data class PokemonDto(
    val name: String,
    val sprites: Sprites = Sprites(),
    val types: Set<Type> = emptySet()
) {

    @Serializable
    data class Sprites(
        val other: Other = Other(),
    ) {

        @Serializable
        data class Other(
            @SerialName("official-artwork")
            val officialArtwork: OfficialArtwork = OfficialArtwork(),
        ) {

            @Serializable
            data class OfficialArtwork(
                @SerialName("front_default")
                val frontDefault: String? = null,
            )
        }
    }

    @Serializable
    data class Type(
        val type: ApiResource
    )
}
