package ru.nomad.pokemon.feature.pokemons

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.nomad.pokemon.core.data.repository.PokemonRepository
import ru.nomad.pokemon.core.model.Pokemon
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val pokemonRepository: PokemonRepository
) : ViewModel() {
    private val _query = MutableStateFlow("")
    val query = _query.asStateFlow()

    private val _pokemonPagingData = MutableStateFlow<PagingData<Pokemon>>(
        PagingData.empty(
            LoadStates(
                LoadState.Loading,
                LoadState.NotLoading(false),
                LoadState.NotLoading(false)
            )
        )
    )
    val pokemonPagingData = _pokemonPagingData.asStateFlow()

    init {
        searchPokemon()
    }

    fun onQueryChange(newQuery: String) {
        _query.value = newQuery
        searchPokemon()
    }

    private fun searchPokemon() {
        viewModelScope.launch {
            pokemonRepository.getPokemonList(
                query = query.value
            ).cachedIn(viewModelScope)
                .collect { _pokemonPagingData.value = it }
        }
    }
}