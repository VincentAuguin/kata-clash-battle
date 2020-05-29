package fr.vauguin.clashbattle.card.cost

import fr.vauguin.clashbattle.card.Card

sealed class CardCostPolicy
data class FixedCostPolicy(val cost: Int) : CardCostPolicy()
data class VariableCostPolicy(val compute: (card: Card) -> Int) : CardCostPolicy()