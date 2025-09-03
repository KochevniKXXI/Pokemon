package ru.nomad.pokemon.feature.pokemons.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import ru.nomad.pokemon.core.designsystem.theme.PokemonTheme
import ru.nomad.pokemon.core.model.Pokemon
import ru.nomad.pokemon.core.designsystem.R as designsystemR

@Composable
internal fun PokemonCard(
    pokemon: Pokemon,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .aspectRatio(1f)
    ) {
        val imageModifier = Modifier
            .weight(2f)
            .fillMaxWidth()

        Spacer(modifier = Modifier.weight(1f))

        if (LocalInspectionMode.current) {
            Image(
                painter = painterResource(designsystemR.drawable.pokemon_image_preview_placeholder),
                contentDescription = null,
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
                modifier = imageModifier
            )
        }

        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            Text(
                text = pokemon.name,
                modifier = Modifier
                    .padding(dimensionResource(designsystemR.dimen.s_space))
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun PokemonCardPreview() {
    PokemonTheme {
        PokemonCard(
            pokemon = Pokemon(
                name = "Pikachu",
                image = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/dream-world/25.svg"
            )
        )
    }
}