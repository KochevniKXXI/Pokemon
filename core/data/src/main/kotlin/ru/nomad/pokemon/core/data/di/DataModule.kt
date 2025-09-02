package ru.nomad.pokemon.core.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.nomad.pokemon.core.data.repository.NetworkPokemonRepository
import ru.nomad.pokemon.core.data.repository.PokemonRepository

@Module
@InstallIn(SingletonComponent::class)
internal interface DataModule {

    @Binds
    fun bindsPokemonRepository(pokemonRepository: NetworkPokemonRepository): PokemonRepository
}