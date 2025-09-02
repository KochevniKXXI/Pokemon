package ru.nomad.pokemon.core.network

import kotlinx.coroutines.flow.Flow
import ru.nomad.pokemon.core.network.model.PokemonDto

interface NetworkDataSource {
    fun getPokemonList(): Flow<List<PokemonDto>>
}