package fr.vauguin.clashbattle

class Game {
    var elixirs = INITIAL_ELIXIRS
        set(value) {
            field = value.coerceAtLeast(MINIMUM_ELIXIRS).coerceAtMost(MAXIMUM_ELIXIRS)
        }

    companion object {
        private const val MINIMUM_ELIXIRS = 0
        private const val MAXIMUM_ELIXIRS = 10
        private const val INITIAL_ELIXIRS = 5
    }
}