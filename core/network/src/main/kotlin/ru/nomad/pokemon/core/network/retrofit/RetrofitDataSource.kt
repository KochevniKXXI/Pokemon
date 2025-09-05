package ru.nomad.pokemon.core.network.retrofit

import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url
import ru.nomad.pokemon.core.network.NetworkDataSource
import ru.nomad.pokemon.core.network.model.ApiResourceList
import ru.nomad.pokemon.core.network.model.PokemonDto
import javax.inject.Inject
import javax.inject.Singleton

internal interface RetrofitPokeApi {
    @GET("pokemon")
    suspend fun getPokemonResources(
        @Query("offset") offset: Int? = null,
        @Query("limit") limit: Int? = null
    ): ApiResourceList

    @GET
    suspend fun getPokemon(@Url url: String): PokemonDto
}

@Singleton
internal class RetrofitDataSource @Inject constructor(
    private val retrofitPokeApi: RetrofitPokeApi
) : NetworkDataSource {
    override suspend fun getPokemonResources(
        offset: Int?,
        limit: Int?
    ): ApiResourceList = retrofitPokeApi.getPokemonResources(offset, limit)

    override suspend fun getPokemon(url: String): PokemonDto = retrofitPokeApi.getPokemon(url)
}