package fr.vauguin.clashbattle.card.cost

import fr.vauguin.clashbattle.card.*

fun Card.resolveCostOnLastDiscardedCardCost(deck: Deck, resolve: (Card) -> Int): Int {
    if (costPolicy !is VariableCostPolicy) throw NotVariableCostPolicy(
        this
    )

    return deck.getLastDiscarded().let { previous ->
        if (previous == null) throw NoDiscardedCardForCalculation(this)
        if (previous.costPolicy !is FixedCostPolicy) throw VariableCostBasedOnVariableCostCalculationNotSupported(
            this,
            previous
        )
        resolve(previous)
    }
}