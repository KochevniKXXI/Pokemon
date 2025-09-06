package ru.nomad.pokemon.core.model

data class Pokemon(
    val name: String,
    val image: String?,
) {

    enum class Type(val color: Int) {
        NORMAL(0xFF9FA19F.toInt()),
        FIGHTING(0xFFFF8000.toInt()),
        FLYING(0xFF81B9EF.toInt()),
        POISON(0xFF9040CC.toInt()),
        GROUND(0xFF915121.toInt()),
        ROCK(0xFFAFA981.toInt()),
        BUG(0xFF91A119.toInt()),
        GHOST(0xFF704170.toInt()),
        STEEL(0xFF60A1B8.toInt()),
        FIRE(0xFFE62829.toInt()),
        WATER(0xFF2980EF.toInt()),
        GRASS(0xFF42A129.toInt()),
        ELECTRIC(0xFFFAC000.toInt()),
        PSYCHIC(0xFFF14179.toInt()),
        ICE(0xFF3FD7FD.toInt()),
        DRAGON(0xFF5061E1.toInt()),
        DARK(0xFF50413F.toInt()),
        FAIRY(0xFFF170F1.toInt()),
    }
}
