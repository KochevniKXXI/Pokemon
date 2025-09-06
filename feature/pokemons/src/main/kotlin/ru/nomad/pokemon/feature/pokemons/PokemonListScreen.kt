package ru.nomad.pokemon.feature.pokemons

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.SheetValue
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ru.nomad.pokemon.core.designsystem.component.EmptyWidget
import ru.nomad.pokemon.core.designsystem.component.ErrorWidget
import ru.nomad.pokemon.core.designsystem.component.LoadingWidget
import ru.nomad.pokemon.core.designsystem.theme.PokemonTheme
import ru.nomad.pokemon.core.model.Pokemon
import ru.nomad.pokemon.feature.pokemons.component.FiltersWidget
import ru.nomad.pokemon.feature.pokemons.component.PokemonCard
import ru.nomad.pokemon.feature.pokemons.component.PokemonSearchBar
import ru.nomad.pokemon.feature.pokemons.preview.PokemonPreviewParameterProvider
import ru.nomad.pokemon.core.designsystem.R as designsystemR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonListScreen(
    modifier: Modifier = Modifier,
    viewModel: PokemonListViewModel = viewModel()
) {
    val lazyPagingPokemon = viewModel.pokemonPagingData.collectAsLazyPagingItems()
    val query by viewModel.query.collectAsStateWithLifecycle()

    PullToRefreshBox(
        isRefreshing = lazyPagingPokemon.loadState.refresh is LoadState.Loading,
        onRefresh = viewModel::fetchPokemonList
    ) {
        PokemonListScreen(
            lazyPagingPokemon = lazyPagingPokemon,
            query = query,
            selectedTypes = viewModel.selectedTypes,
            onQueryChange = viewModel::onQueryChange,
            onTypeClick = viewModel::onTypeClick,
            onApplyFiltersClick = viewModel::fetchPokemonList,
            modifier = modifier
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PokemonListScreen(
    lazyPagingPokemon: LazyPagingItems<Pokemon>,
    query: String,
    selectedTypes: Set<Pokemon.Type>,
    onQueryChange: (String) -> Unit,
    onTypeClick: (Pokemon.Type) -> Unit,
    onApplyFiltersClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = true
        )
    )
    val scope = rememberCoroutineScope()
    var filtersAreApplied by rememberSaveable { mutableStateOf(selectedTypes.isNotEmpty()) }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetContent = {
            FiltersWidget(
                selectedTypes = selectedTypes,
                onTypeClick = onTypeClick,
                onApplyClick = {
                    onApplyFiltersClick()
                    filtersAreApplied = selectedTypes.isNotEmpty()
                    scope.launch {
                        scaffoldState.bottomSheetState.hide()
                    }
                },
                modifier = Modifier
                    .padding(horizontal = dimensionResource(designsystemR.dimen.m_space))
            )
        },
        topBar = {
            PokemonListTopAppBar(
                query = query,
                filterChecked = scaffoldState.bottomSheetState.isVisible,
                filtersAreApplied = filtersAreApplied,
                onQueryChange = onQueryChange,
                onFilterCheckedChange = { on ->
                    scope.launch {
                        if (on) {
                            scaffoldState.bottomSheetState.expand()
                        } else {
                            scaffoldState.bottomSheetState.hide()
                        }
                    }
                }
            )
        },
        sheetPeekHeight = dimensionResource(R.dimen.bottom_sheet_peek_height),
        modifier = modifier
    ) { paddingValues ->
        val refreshLoadState = lazyPagingPokemon.loadState.refresh
        val contentModifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .verticalScroll(rememberScrollState())

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
                if (lazyPagingPokemon.loadState.append.endOfPaginationReached && lazyPagingPokemon.itemCount == 0) {
                    EmptyWidget(modifier = contentModifier)
                } else {
                    PokemonGrid(
                        lazyPagingPokemon = lazyPagingPokemon,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    )
                }
            }
        }
    }

    BackHandler(enabled = scaffoldState.bottomSheetState.currentValue == SheetValue.Expanded) {
        scope.launch {
            scaffoldState.bottomSheetState.hide()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PokemonListTopAppBar(
    query: String,
    filterChecked: Boolean,
    filtersAreApplied: Boolean,
    onQueryChange: (String) -> Unit,
    onFilterCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(
                color = TopAppBarDefaults.topAppBarColors().containerColor
            )
    ) {
        CenterAlignedTopAppBar(
            title = {
                Image(
                    painter = painterResource(designsystemR.drawable.text_logo),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(vertical = dimensionResource(designsystemR.dimen.s_space))
                )
            }
        )
        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(designsystemR.dimen.s_space)),
            modifier = Modifier
                .padding(horizontal = dimensionResource(designsystemR.dimen.m_space))
                .padding(bottom = dimensionResource(designsystemR.dimen.m_space))
        ) {
            PokemonSearchBar(
                query = query,
                onQueryChange = onQueryChange,
                modifier = Modifier
                    .weight(1f)
            )

            FilledIconToggleButton(
                checked = filterChecked,
                onCheckedChange = onFilterCheckedChange,
                modifier = Modifier
                    .size(dimensionResource(R.dimen.filter_button_size))
            ) {
                BadgedBox(
                    badge = {
                        if (filtersAreApplied) {
                            Badge()
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = null
                    )
                }
            }
        }
    }
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

@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@PreviewScreenSizes
@Composable
private fun PokemonListScreenPreview(
    @PreviewParameter(PokemonPreviewParameterProvider::class)
    pagingPokemon: PagingData<Pokemon>
) {
    PokemonTheme {
        PokemonListScreen(
            lazyPagingPokemon = MutableStateFlow(pagingPokemon).collectAsLazyPagingItems(),
            query = "",
            selectedTypes = emptySet(),
            onQueryChange = {},
            onTypeClick = {},
            onApplyFiltersClick = {}
        )
    }
}