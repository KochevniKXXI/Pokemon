package ru.nomad.pokemon.core.network.retrofit

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.sync.Semaphore
import retrofit2.http.GET
import retrofit2.http.Url
import ru.nomad.pokemon.core.network.NetworkDataSource
import ru.nomad.pokemon.core.network.model.PokemonDto
import ru.nomad.pokemon.core.network.model.NetworkResponse
import javax.inject.Inject
import javax.inject.Singleton

internal interface RetrofitPokeApi {
    @GET("pokemon")
    suspend fun getPokemonList(): NetworkResponse

    @GET
    suspend fun getPokemon(@Url url: String): PokemonDto
}

@Singleton
internal class RetrofitDataSource @Inject constructor(
    private val retrofitPokeApi: RetrofitPokeApi
) : NetworkDataSource {
    override fun getPokemonList(): Flow<List<PokemonDto>> = flow {
        val pokemonList = retrofitPokeApi.getPokemonList()
        val semaphore = Semaphore(20)

        coroutineScope {
            val jobs = pokemonList.results.map { pokemon ->
                async {
                    semaphore.acquire()
                    try {
                        retrofitPokeApi.getPokemon(pokemon.url)
                    } finally {
                        semaphore.release()
                    }
                }
            }

            emit(jobs.awaitAll())
        }
    }
}