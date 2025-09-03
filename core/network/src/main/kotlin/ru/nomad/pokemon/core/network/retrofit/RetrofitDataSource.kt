package ru.nomad.pokemon.core.network.retrofit

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.sync.Semaphore
import okhttp3.internal.toImmutableList
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url
import ru.nomad.pokemon.core.network.NetworkDataSource
import ru.nomad.pokemon.core.network.model.NetworkResource
import ru.nomad.pokemon.core.network.model.PokemonDto
import ru.nomad.pokemon.core.network.model.NetworkResponse
import ru.nomad.pokemon.core.network.util.DEFAULT_LIMIT
import javax.inject.Inject
import javax.inject.Singleton

internal interface RetrofitPokeApi {
    @GET("pokemon")
    suspend fun getPokemonResources(
        @Query("offset") offset: Int? = null,
        @Query("limit") limit: Int? = null
    ): NetworkResponse<NetworkResource>

    @GET
    suspend fun getPokemon(@Url url: String): PokemonDto
}

@Singleton
internal class RetrofitDataSource @Inject constructor(
    private val retrofitPokeApi: RetrofitPokeApi
) : NetworkDataSource {
    override suspend fun getPokemonList(
        offset: Int?,
        limit: Int?
    ): NetworkResponse<PokemonDto> {
        val pokemonResources = retrofitPokeApi.getPokemonResources(offset, limit)
        val semaphore = Semaphore(DEFAULT_LIMIT)
        val results = mutableListOf<PokemonDto>()

        coroutineScope {
            val jobs = pokemonResources.results.map { pokemon ->
                async(Dispatchers.IO) {
                    semaphore.acquire()
                    try {
                        retrofitPokeApi.getPokemon(pokemon.url)
                    } finally {
                        semaphore.release()
                    }
                }
            }

            results.addAll(jobs.awaitAll())
        }

        return NetworkResponse(
            pokemonResources.count,
            pokemonResources.next,
            pokemonResources.previous,
            results.toImmutableList()
        )
    }
}