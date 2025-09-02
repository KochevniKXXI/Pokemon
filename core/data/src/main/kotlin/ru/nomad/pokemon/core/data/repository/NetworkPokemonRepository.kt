package ru.nomad.pokemon.core.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.nomad.pokemon.core.data.model.asModel
import ru.nomad.pokemon.core.model.Pokemon
import ru.nomad.pokemon.core.network.NetworkDataSource
import ru.nomad.pokemon.core.network.model.PokemonDto
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class NetworkPokemonRepository @Inject constructor(
    private val networkDataSource: NetworkDataSource
) : PokemonRepository {
    override fun getPokemonList(): Flow<List<Pokemon>> = networkDataSource.getPokemonList()
        .map { it.map(PokemonDto::asModel) }
}
