package ru.nomad.pokemon.core.network.di

import android.content.Context
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.Cache
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import ru.nomad.pokemon.core.network.retrofit.RetrofitPokeApi
import javax.inject.Singleton

private const val POKEAPI_BASE_URL = "https://pokeapi.co/api/v2/"
private const val CACHE_MAX_SIZE = 10L * 1024 * 1024 // 10 MB

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
    fun providesCache(
        @ApplicationContext context: Context,
    ): Cache = Cache(
        directory = context.cacheDir,
        maxSize = CACHE_MAX_SIZE
    )

    @Provides
    @Singleton
    fun providesOkHttpClient(cache: Cache): OkHttpClient = OkHttpClient.Builder()
        .cache(cache)
        .build()

    @Provides
    @Singleton
    fun providesPokeApi(
        okHttpClient: OkHttpClient,
        json: Json
    ): RetrofitPokeApi = Retrofit.Builder()
        .baseUrl(POKEAPI_BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()
        .create(RetrofitPokeApi::class.java)
}