package ru.nomad.pokemon.core.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import ru.nomad.pokemon.core.data.model.asModel
import ru.nomad.pokemon.core.model.Pokemon
import ru.nomad.pokemon.core.network.NetworkDataSource
import ru.nomad.pokemon.core.network.model.PokemonDto
import ru.nomad.pokemon.core.network.util.DEFAULT_LIMIT
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class PokemonPagingSource @Inject constructor(
    private val networkDataSource: NetworkDataSource,
    private val limit: Int? = null,
) : PagingSource<Int, Pokemon>() {
    override fun getRefreshKey(state: PagingState<Int, Pokemon>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Pokemon> {
        return try {
            val nextPageNumber = params.key ?: 1
            val response = if (nextPageNumber == 1) {
                networkDataSource.getPokemonList(limit = limit)
            } else {
                networkDataSource.getPokemonList(
                    offset = (limit ?: DEFAULT_LIMIT) * (nextPageNumber - 1),
                    limit = limit
                )
            }

            LoadResult.Page(
                data = response.results.map(PokemonDto::asModel),
                prevKey = null,
                nextKey = if (response.next != null) {
                    nextPageNumber + 1
                } else {
                    null
                }
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}