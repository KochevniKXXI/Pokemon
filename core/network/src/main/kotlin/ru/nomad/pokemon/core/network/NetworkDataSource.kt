package ru.nomad.pokemon.core.network

import ru.nomad.pokemon.core.network.model.ApiResourceList
import ru.nomad.pokemon.core.network.model.PokemonDto

interface NetworkDataSource {
    suspend fun getPokemonResources(
        offset: Int? = null,
        limit: Int? = null
    ): ApiResourceList

    suspend fun getPokemon(url: String): PokemonDto
}