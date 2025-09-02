package ru.nomad.pokemon.core.data.model

import ru.nomad.pokemon.core.model.Pokemon
import ru.nomad.pokemon.core.network.model.PokemonDto

fun PokemonDto.asModel() = Pokemon(
    name = name.replaceFirstChar(Char::uppercase),
    image = sprites.other.dreamWorld.frontDefault,
)