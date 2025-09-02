package ru.nomad.pokemon.core.network.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.nomad.pokemon.core.network.NetworkDataSource
import ru.nomad.pokemon.core.network.retrofit.RetrofitDataSource

@Module
@InstallIn(SingletonComponent::class)
internal interface NetworkBindsModule {

    @Binds
    fun bindsNetworkDataSource(networkDataSource: RetrofitDataSource): NetworkDataSource
}