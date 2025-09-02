package ru.nomad.pokemon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import ru.nomad.pokemon.core.designsystem.theme.PokemonTheme
import ru.nomad.pokemon.feature.pokemons.PokemonListScreen

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PokemonTheme {
                PokemonListScreen()
            }
        }
    }
}