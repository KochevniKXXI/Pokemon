package ru.nomad.pokemon.feature.pokemons.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import ru.nomad.pokemon.core.designsystem.R as designsystemR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PokemonSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    SearchBar(
        inputField = {
            SearchBarDefaults.InputField(
                query = query,
                onQueryChange = onQueryChange,
                onSearch = {
                    keyboardController?.hide()
                },
                expanded = false,
                onExpandedChange = {},
                placeholder = {
                    Text(
                        text = stringResource(designsystemR.string.search)
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null
                    )
                },
            )
        },
        expanded = false,
        onExpandedChange = {},
        modifier = modifier,
        content = {}
    )
}