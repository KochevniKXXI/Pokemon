package ru.nomad.pokemon.feature.pokemons

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.nomad.pokemon.core.data.repository.PokemonRepository
import ru.nomad.pokemon.core.model.Pokemon
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    pokemonRepository: PokemonRepository
) : ViewModel() {
    val uiState = pokemonRepository.getPokemonList()
        .map {
            if (it.isNotEmpty()) {
                PokemonListUiState.Success(it)
            } else {
                PokemonListUiState.Empty
            }
        }
        .catch { emit(PokemonListUiState.Error(it)) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = PokemonListUiState.Loading
        )
}

sealed interface PokemonListUiState {
    data object Loading : PokemonListUiState
    data class Error(val throwable: Throwable) : PokemonListUiState
    data class Success(val pokemonList: List<Pokemon>) : PokemonListUiState
    data object Empty : PokemonListUiState
}