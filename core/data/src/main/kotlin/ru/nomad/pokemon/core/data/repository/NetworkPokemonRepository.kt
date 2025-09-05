package ru.nomad.pokemon.core.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.nomad.pokemon.core.data.paging.PokemonPagingSource
import ru.nomad.pokemon.core.model.Pokemon
import ru.nomad.pokemon.core.network.NetworkDataSource
import ru.nomad.pokemon.core.data.util.DEFAULT_LIMIT
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class NetworkPokemonRepository @Inject constructor(
    private val networkDataSource: NetworkDataSource
) : PokemonRepository {
    override fun getPokemonList(
        limit: Int?,
        query: String
    ): Flow<PagingData<Pokemon>> {
        return Pager(
            config = PagingConfig(limit ?: DEFAULT_LIMIT)
        ) {
            PokemonPagingSource(
                networkDataSource,
                limit,
                query
            )
        }.flow
    }
}
