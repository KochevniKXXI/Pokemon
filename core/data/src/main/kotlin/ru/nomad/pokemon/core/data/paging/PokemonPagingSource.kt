package ru.nomad.pokemon.core.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.sync.Semaphore
import ru.nomad.pokemon.core.data.model.asModel
import ru.nomad.pokemon.core.data.util.DEFAULT_LIMIT
import ru.nomad.pokemon.core.model.Pokemon
import ru.nomad.pokemon.core.network.NetworkDataSource
import ru.nomad.pokemon.core.network.model.ApiResource
import ru.nomad.pokemon.core.network.model.ApiResourceList
import ru.nomad.pokemon.core.network.model.PokemonDto

internal class PokemonPagingSource(
    private val networkDataSource: NetworkDataSource,
    private val limit: Int? = null,
    private val query: String,
) : PagingSource<Int, Pokemon>() {
    override fun getRefreshKey(state: PagingState<Int, Pokemon>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Pokemon> {
        return try {
            var nextPageNumber = params.key ?: 1
            var response: ApiResourceList? = null
            val apiResources = mutableListOf<ApiResource>()

            try {
                do {
                    response = if (nextPageNumber == 1) {
                        networkDataSource.getPokemonResources(limit = limit)
                    } else {
                        networkDataSource.getPokemonResources(
                            offset = (limit ?: DEFAULT_LIMIT) * (nextPageNumber - 1),
                            limit = limit
                        )
                    }

                    if (query.isNotBlank()) {
                        val filteredApiResources = response.results
                            .filter { it.name?.contains(query, true) ?: false }
                        apiResources.addAll(filteredApiResources)
                    } else {
                        apiResources.addAll(response.results)
                    }

                    nextPageNumber++
                } while (apiResources.size < (limit ?: DEFAULT_LIMIT) && response.next != null)
            } catch (e: Exception) {
                if (apiResources.isEmpty()) throw e
            }

            val semaphore = Semaphore(DEFAULT_LIMIT)
            val pokemonList = mutableListOf<PokemonDto>()

            coroutineScope {
                val jobs = apiResources.map { pokemon ->
                    async(Dispatchers.IO) {
                        semaphore.acquire()
                        try {
                            networkDataSource.getPokemon(pokemon.url)
                        } catch (_: Exception) {
                            PokemonDto(pokemon.name ?: "")
                        } finally {
                            semaphore.release()
                        }
                    }
                }

                pokemonList.addAll(jobs.awaitAll())
            }

            LoadResult.Page(
                data = pokemonList.map(PokemonDto::asModel),
                prevKey = null,
                nextKey = response?.next?.let { nextPageNumber }
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}