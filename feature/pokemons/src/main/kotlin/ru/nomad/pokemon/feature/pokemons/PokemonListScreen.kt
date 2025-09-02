package ru.nomad.pokemon.feature.pokemons

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.FixedScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import ru.nomad.pokemon.core.designsystem.component.EmptyWidget
import ru.nomad.pokemon.core.designsystem.component.ErrorWidget
import ru.nomad.pokemon.core.designsystem.component.LoadingWidget
import ru.nomad.pokemon.core.designsystem.theme.PokemonTheme
import ru.nomad.pokemon.core.model.Pokemon
import ru.nomad.pokemon.core.designsystem.R as designsystemR

@Composable
fun PokemonListScreen(
    modifier: Modifier = Modifier,
    viewModel: PokemonListViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    PokemonListScreen(
        uiState = uiState,
        modifier = modifier
    )
}

@Composable
private fun PokemonListScreen(
    uiState: PokemonListUiState,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = { PokemonListTopAppBar() },
        modifier = modifier
    ) { paddingValues ->
        val contentModifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()

        when (uiState) {
            PokemonListUiState.Loading -> {
                LoadingWidget(
                    modifier = contentModifier
                )
            }

            is PokemonListUiState.Error -> {
                ErrorWidget(
                    message = uiState.throwable.message,
                    modifier = contentModifier
                )
            }

            is PokemonListUiState.Success -> {
                PokemonGrid(
                    pokemonList = uiState.pokemonList,
                    modifier = contentModifier
                )
            }

            PokemonListUiState.Empty -> {
                EmptyWidget(
                    modifier = contentModifier
                )
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
    pokemonList: List<Pokemon>,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(dimensionResource(designsystemR.dimen.m_space)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(designsystemR.dimen.s_space)),
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(designsystemR.dimen.s_space)),
        modifier = modifier
    ) {
        items(pokemonList) { pokemon ->
            PokemonCard(
                pokemon = pokemon
            )
        }
    }
}

@Composable
private fun PokemonCard(
    pokemon: Pokemon,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .aspectRatio(1f)
    ) {
        val imageModifier = Modifier
            .weight(1f)
            .fillMaxWidth()
        val imageScale = FixedScale(0.5f)

        if (LocalInspectionMode.current) {
            Image(
                painter = painterResource(designsystemR.drawable.pokemon_image_preview_placeholder),
                contentDescription = null,
                contentScale = imageScale,
                modifier = imageModifier
            )
        } else {
            val context = LocalContext.current

            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(pokemon.image)
                    .decoderFactory(SvgDecoder.Factory())
                    .build(),
                contentDescription = null,
                contentScale = imageScale,
                modifier = imageModifier
            )
        }

        Text(
            text = pokemon.name,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(dimensionResource(designsystemR.dimen.s_space))
        )
    }
}

@PreviewLightDark
@Composable
private fun PokemonListScreenPreview() {
    PokemonTheme {
        PokemonListScreen(
            uiState = PokemonListUiState.Success(
                pokemonList = listOf(
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
}