package ru.nomad.pokemon.feature.pokemons.component

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.core.graphics.drawable.toBitmap
import androidx.palette.graphics.Palette
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.nomad.pokemon.core.designsystem.theme.PokemonTheme
import ru.nomad.pokemon.core.model.Pokemon
import ru.nomad.pokemon.core.designsystem.R as designsystemR

@Composable
internal fun PokemonCard(
    pokemon: Pokemon,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val defaultColor = CardDefaults.cardColors().containerColor.value.toInt()
    var mutedColor by rememberSaveable { mutableIntStateOf(defaultColor) }
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(pokemon.image) {
        val loader = ImageLoader(context)
        val request = ImageRequest.Builder(context)
            .data(pokemon.image)
            .allowHardware(false)
            .build()

        val result = loader.execute(request)
        if (result is SuccessResult) {
            bitmap = result.drawable.toBitmap()

            val palette = withContext(Dispatchers.Default) {
                Palette.from(bitmap as Bitmap).generate()
            }

            mutedColor = palette.getMutedColor(defaultColor)
        }
    }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        modifier = modifier
            .aspectRatio(1f)
            .background(
                brush = Brush.verticalGradient(listOf(Color(mutedColor), Color.Black)),
                shape = CardDefaults.shape
            )
    ) {
        val imageModifier = Modifier
            .weight(2f)
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(designsystemR.dimen.xl_space))

        Spacer(modifier = Modifier.weight(1f))

//        if (LocalInspectionMode.current) {
        bitmap?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = null,
                modifier = imageModifier
            )
        }
//        } else {
//            AsyncImage(
//                model = ImageRequest.Builder(context)
//                    .data(pokemon.image)
//                    .build(),
//                contentDescription = null,
//                modifier = imageModifier
//            )
//        }

        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            Text(
                text = pokemon.name,
                color = Color.White,
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