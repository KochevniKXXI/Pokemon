package ru.nomad.pokemon.core.network.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import ru.nomad.pokemon.core.network.retrofit.RetrofitPokeApi
import javax.inject.Singleton

private const val POKEAPI_BASE_URL = "https://pokeapi.co/api/v2/"

@Module
@InstallIn(SingletonComponent::class)
internal object NetworkProvidesModule {

    @Provides
    @Singleton
    fun providesNetworkJson(): Json = Json {
        ignoreUnknownKeys = true
    }

    @Provides
    @Singleton
    fun providesPokeApi(
        json: Json
    ): RetrofitPokeApi = Retrofit.Builder()
        .baseUrl(POKEAPI_BASE_URL)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()
        .create(RetrofitPokeApi::class.java)
}