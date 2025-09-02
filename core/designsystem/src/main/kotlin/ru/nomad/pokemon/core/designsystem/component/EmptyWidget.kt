package ru.nomad.pokemon.core.designsystem.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import ru.nomad.pokemon.core.designsystem.R

@Composable
fun EmptyWidget(
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .padding(horizontal = dimensionResource(R.dimen.m_space))
    ) {
        Text(text = stringResource(R.string.no_available_data))
    }
}