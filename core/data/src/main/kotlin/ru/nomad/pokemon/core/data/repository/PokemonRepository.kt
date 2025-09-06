package ru.nomad.pokemon.core.data.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.nomad.pokemon.core.model.Pokemon

interface PokemonRepository {
    fun getPokemonList(
        limit: Int? = null,
        query: String = "",
        selectedTypes: Set<Pokemon.Type> = emptySet()
    ): Flow<PagingData<Pokemon>>
}