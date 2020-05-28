package fr.vauguin.clashbattle

import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class Game {
    var elixirs = INITIAL_ELIXIRS
        set(value) {
            field = value.coerceAtLeast(MINIMUM_ELIXIRS).coerceAtMost(MAXIMUM_ELIXIRS)
        }

    private var elixirProducer: ElixirProducer = DefaultElixirProducer()

    fun setElixirProducer(producer: ElixirProducer) {
        elixirProducer.stopProducing("Change elixirs producer")
        elixirProducer = producer
    }

    suspend fun startProducingElixirs() = elixirProducer.startProducing { quantity ->
        elixirs += quantity
    }

    fun stopProducingElixirs() = elixirProducer.stopProducing("Game has explicitly ask to stop production")

    companion object {
        private const val MINIMUM_ELIXIRS = 0
        private const val MAXIMUM_ELIXIRS = 10
        private const val INITIAL_ELIXIRS = 5
    }
}