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
fun ErrorWidget(
    message: String?,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .padding(horizontal = dimensionResource(R.dimen.m_space))
    ) {
        Text(text = message ?: stringResource(R.string.unknown_error))
    }
}