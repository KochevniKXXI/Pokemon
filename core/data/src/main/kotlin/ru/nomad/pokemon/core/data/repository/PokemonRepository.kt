package ru.nomad.pokemon.core.data.repository

import kotlinx.coroutines.flow.Flow
import ru.nomad.pokemon.core.model.Pokemon

interface PokemonRepository {
    fun getPokemonList(): Flow<List<Pokemon>>
}