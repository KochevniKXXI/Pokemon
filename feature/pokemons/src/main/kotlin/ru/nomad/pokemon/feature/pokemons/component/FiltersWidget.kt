package ru.nomad.pokemon.feature.pokemons.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import ru.nomad.pokemon.core.designsystem.theme.PokemonTheme
import ru.nomad.pokemon.core.model.Pokemon
import ru.nomad.pokemon.core.designsystem.R as designsystemR

@Composable
internal fun ColumnScope.FiltersWidget(
    selectedTypes: Set<Pokemon.Type>,
    onTypeClick: (Pokemon.Type) -> Unit,
    onApplyClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(designsystemR.dimen.s_space)),
        modifier = modifier
    ) {
        Pokemon.Type.entries.forEach { type ->
            FilterChip(
                selected = selectedTypes.contains(type),
                onClick = {
                    onTypeClick(type)
                },
                label = {
                    Text(type.name)
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(type.color),
                    selectedLabelColor = Color.White
                )
            )
        }
    }

    Button(
        onClick = onApplyClick,
        modifier = Modifier
            .align(Alignment.CenterHorizontally)
    ) {
        Text(
            text = stringResource(designsystemR.string.apply)
        )
    }
}

@Preview
@Composable
private fun PreviewFiltersWidget() {
    PokemonTheme {
        Column {
            FiltersWidget(
                selectedTypes = setOf(
                    Pokemon.Type.WATER,
                    Pokemon.Type.FIGHTING,
                    Pokemon.Type.BUG,
                    Pokemon.Type.FAIRY
                ),
                onTypeClick = {},
                onApplyClick = {}
            )
        }
    }
}