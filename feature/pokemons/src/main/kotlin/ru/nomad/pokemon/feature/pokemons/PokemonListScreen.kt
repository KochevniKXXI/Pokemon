package ru.nomad.pokemon.feature.pokemons

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.MutableStateFlow
import ru.nomad.pokemon.core.designsystem.component.EmptyWidget
import ru.nomad.pokemon.core.designsystem.component.ErrorWidget
import ru.nomad.pokemon.core.designsystem.component.LoadingWidget
import ru.nomad.pokemon.core.designsystem.theme.PokemonTheme
import ru.nomad.pokemon.core.model.Pokemon
import ru.nomad.pokemon.feature.pokemons.preview.PokemonPreviewParameterProvider
import ru.nomad.pokemon.feature.pokemons.component.PokemonCard
import ru.nomad.pokemon.core.designsystem.R as designsystemR

@Composable
fun PokemonListScreen(
    modifier: Modifier = Modifier,
    viewModel: PokemonListViewModel = viewModel()
) {
    val lazyPagingPokemon = viewModel.uiState.collectAsLazyPagingItems()

    PokemonListScreen(
        lazyPagingPokemon = lazyPagingPokemon,
        modifier = modifier
    )
}

@Composable
private fun PokemonListScreen(
    lazyPagingPokemon: LazyPagingItems<Pokemon>,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = { PokemonListTopAppBar() },
        modifier = modifier
    ) { paddingValues ->
        val refreshLoadState = lazyPagingPokemon.loadState.refresh
        val contentModifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()

        when (refreshLoadState) {
            is LoadState.Loading -> {
                LoadingWidget(modifier = contentModifier)
            }

            is LoadState.Error -> {
                ErrorWidget(
                    message = refreshLoadState.error.message,
                    modifier = contentModifier
                )
            }

            is LoadState.NotLoading -> {
                if (refreshLoadState.endOfPaginationReached && lazyPagingPokemon.itemCount == 0) {
                    EmptyWidget(modifier = contentModifier)
                } else {
                    PokemonGrid(
                        lazyPagingPokemon = lazyPagingPokemon,
                        modifier = Modifier
                            .padding(paddingValues)
                            .fillMaxSize()
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PokemonListTopAppBar(
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        title = {
            Image(
                painter = painterResource(designsystemR.drawable.text_logo),
                contentDescription = null,
                modifier = Modifier
                    .padding(vertical = dimensionResource(designsystemR.dimen.s_space))
            )
        },
        modifier = modifier
    )
}

@Composable
private fun PokemonGrid(
    lazyPagingPokemon: LazyPagingItems<Pokemon>,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(dimensionResource(designsystemR.dimen.m_space)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(designsystemR.dimen.s_space)),
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(designsystemR.dimen.s_space)),
        modifier = modifier
    ) {
        items(lazyPagingPokemon.itemCount) { index ->
            lazyPagingPokemon[index]?.let { pokemon ->
                PokemonCard(pokemon = pokemon)
            }
        }

        lazyPagingPokemon.apply {
            val appendLoadState = loadState.append

            when (appendLoadState) {
                is LoadState.Loading -> {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        LoadingWidget(modifier = Modifier.fillMaxWidth())
                    }
                }

                is LoadState.Error -> {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        ErrorWidget(
                            message = appendLoadState.error.message,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                is LoadState.NotLoading -> {}
            }
        }
    }
}

@PreviewLightDark
@PreviewScreenSizes
@Composable
private fun PokemonListScreenPreview(
    @PreviewParameter(PokemonPreviewParameterProvider::class)
    pagingPokemon: PagingData<Pokemon>
) {
    PokemonTheme {
        PokemonListScreen(
            lazyPagingPokemon = MutableStateFlow(pagingPokemon).collectAsLazyPagingItems()
        )
    }
}