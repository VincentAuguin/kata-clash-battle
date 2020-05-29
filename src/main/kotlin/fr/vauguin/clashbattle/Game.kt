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
class Game(cards: Set<Card>) {
    init {
        cards.count().let { count ->
            if (count != DESIRED_CARDS_COUNT)
                throw NotInitializedWithDesiredCardsCount(DESIRED_CARDS_COUNT, count)
        }
    }

    var elixirs = INITIAL_ELIXIRS
        set(value) {
            field = value.coerceAtLeast(MINIMUM_ELIXIRS).coerceAtMost(MAXIMUM_ELIXIRS)
            elixirDisplay.update(field)
        }

    private val deck = DefaultDeck(cards)
    private val hand = mutableSetOf<Card>().apply {
        for (i in 1..4) drawCard(deck, this)
        println("🃏 Your hand is $this")
    }

    fun getHandCards() = hand.toSet()

    private val elixirDisplay = ConsoleElixirDisplay(MAXIMUM_ELIXIRS).also { it.update(elixirs) }

    private var elixirProducer: ElixirProducer = DefaultElixirProducer()

    fun setElixirProducer(producer: ElixirProducer) {
        elixirProducer.stopProducing("Change elixirs producer")
        elixirProducer = producer
    }

    suspend fun startProducingElixirs() = elixirProducer.startProducing { quantity -> elixirs += quantity }

    fun stopProducingElixirs() = elixirProducer.stopProducing("Game has explicitly ask to stop production")

    fun tryToPutCard(card: Card) = try {
        val cost = when (val policy = card.costPolicy) {
            is FixedCostPolicy -> policy.cost
            is VariableCostPolicy -> card.resolveCostOnLastDiscardedCardCost(deck, policy.compute)
        }
        putCard(card, cost)
    } catch (e: CardCostCalculationException) {
        false
    } catch (e: DeckIsEmpty) {
        false
    }

    private fun putCard(card: Card, cost: Int): Boolean {
        if ((elixirs - cost) < 0)
            return false

        val removedFromHand = hand.remove(card)
        if (!removedFromHand)
            return false

        drawCard(deck, hand)
        deck.discard(card)
        elixirs -= cost
        println("💢 Putting card '$card' on the field for $cost elixir(s) !")
        println("🃏 Your hand is now $hand")
        return true
    }

    private fun drawCard(from: Deck, to: MutableSet<Card>) {
        when (val card = from.draw()) {
            null -> throw DeckIsEmpty()
            else -> to.add(card)
        }
    }

    companion object {
        private const val MINIMUM_ELIXIRS = 0
        private const val MAXIMUM_ELIXIRS = 10
        private const val INITIAL_ELIXIRS = 5
        private const val DESIRED_CARDS_COUNT = 8
    }
}

class NotInitializedWithDesiredCardsCount(desiredCount: Int, actualCount: Int) :
    Exception("Unable to instantiate the game because it expects $desiredCount cards and $actualCount was/were provided")