package fr.vauguin.clashbattle

import fr.vauguin.clashbattle.card.*
import fr.vauguin.clashbattle.card.cost.FixedCostPolicy
import fr.vauguin.clashbattle.card.cost.VariableCostPolicy
import fr.vauguin.clashbattle.card.cost.resolveCostOnLastDiscardedCardCost
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

    private val deck = DefaultDeck()

    private val elixirDisplay = ConsoleElixirDisplay(MAXIMUM_ELIXIRS).also { it.update(elixirs) }

    private var elixirProducer: ElixirProducer = DefaultElixirProducer()

    fun setElixirProducer(producer: ElixirProducer) {
        elixirProducer.stopProducing("Change elixirs producer")
        elixirProducer = producer
    }

    suspend fun startProducingElixirs() = elixirProducer.startProducing { quantity -> elixirs += quantity }

    fun stopProducingElixirs() = elixirProducer.stopProducing("Game has explicitly ask to stop production")

    fun tryToPutCard(card: Card) = try {
        val cost = when(val policy = card.costPolicy) {
            is FixedCostPolicy -> policy.cost
            is VariableCostPolicy -> card.resolveCostOnLastDiscardedCardCost(deck, policy.compute)
        }
        ((elixirs - cost) >= 0).also { canPut -> if (canPut) putCard(card, cost) }
    } catch (e: CardCostCalculationException) {
        false
    }

    private fun putCard(card: Card, cost: Int) {
        elixirs -= cost
        deck.discard(card)
        println("💢 Putting card '$card' on the field for $cost elixir(s) !")
    }

    companion object {
        private const val MINIMUM_ELIXIRS = 0
        private const val MAXIMUM_ELIXIRS = 10
        private const val INITIAL_ELIXIRS = 5
    }
}