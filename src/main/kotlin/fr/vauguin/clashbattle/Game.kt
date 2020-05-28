package fr.vauguin.clashbattle

import fr.vauguin.clashbattle.card.Card
import fr.vauguin.clashbattle.elixir.ConsoleElixirDisplay
import fr.vauguin.clashbattle.elixir.DefaultElixirProducer
import fr.vauguin.clashbattle.elixir.ElixirProducer
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class Game {

    var elixirs = INITIAL_ELIXIRS
        set(value) {
            field = value.coerceAtLeast(MINIMUM_ELIXIRS).coerceAtMost(MAXIMUM_ELIXIRS)
            elixirDisplay.update(field)
        }

    private val elixirDisplay = ConsoleElixirDisplay(MAXIMUM_ELIXIRS).also { it.update(elixirs) }

    private var elixirProducer: ElixirProducer =
        DefaultElixirProducer()

    fun setElixirProducer(producer: ElixirProducer) {
        elixirProducer.stopProducing("Change elixirs producer")
        elixirProducer = producer
    }

    suspend fun startProducingElixirs() = elixirProducer.startProducing { quantity -> elixirs += quantity }

    fun stopProducingElixirs() = elixirProducer.stopProducing("Game has explicitly ask to stop production")

    fun putCard(card: Card): Boolean {
        val canPut = (elixirs - card.elixirCost) >= 0
        if (canPut)
            elixirs -= card.elixirCost

        return canPut
    }

    companion object {
        private const val MINIMUM_ELIXIRS = 0
        private const val MAXIMUM_ELIXIRS = 10
        private const val INITIAL_ELIXIRS = 5
    }
}