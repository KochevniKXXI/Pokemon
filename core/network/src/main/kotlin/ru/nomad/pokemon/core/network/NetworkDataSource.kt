package ru.nomad.pokemon.core.network

import ru.nomad.pokemon.core.network.model.NetworkResponse
import ru.nomad.pokemon.core.network.model.PokemonDto

interface NetworkDataSource {
    suspend fun getPokemonList(
        offset: Int? = null,
        limit: Int? = null
    ): NetworkResponse<PokemonDto>
}