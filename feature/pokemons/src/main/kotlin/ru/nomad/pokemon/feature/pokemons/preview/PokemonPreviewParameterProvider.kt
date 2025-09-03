package ru.nomad.pokemon.feature.pokemons.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import ru.nomad.pokemon.core.model.Pokemon

class PokemonPreviewParameterProvider : PreviewParameterProvider<PagingData<Pokemon>> {
    override val values: Sequence<PagingData<Pokemon>>
        get() = sequenceOf(
            PagingData.Companion.empty(
                LoadStates(
                    refresh = LoadState.Loading,
                    prepend = LoadState.NotLoading(false),
                    append = LoadState.NotLoading(false)
                )
            ),

            PagingData.Companion.empty(
                LoadStates(
                    refresh = LoadState.Error(Throwable()),
                    prepend = LoadState.NotLoading(true),
                    append = LoadState.NotLoading(true)
                )
            ),

            PagingData.Companion.empty(
                LoadStates(
                    refresh = LoadState.NotLoading(true),
                    prepend = LoadState.NotLoading(true),
                    append = LoadState.NotLoading(true)
                )
            ),

            PagingData.Companion.from(
                data = listOf(
                    Pokemon(
                        name = "Pikachu",
                        image = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/dream-world/25.svg"
                    ),
                    Pokemon(
                        name = "Pikachu",
                        image = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/dream-world/25.svg"
                    ),
                    Pokemon(
                        name = "Pikachu",
                        image = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/dream-world/25.svg"
                    ),
                    Pokemon(
                        name = "Pikachu",
                        image = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/dream-world/25.svg"
                    ),
                    Pokemon(
                        name = "Pikachu",
                        image = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/dream-world/25.svg"
                    )
                )
            )
        )
}