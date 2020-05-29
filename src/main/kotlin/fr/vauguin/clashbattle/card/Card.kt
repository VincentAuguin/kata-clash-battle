package fr.vauguin.clashbattle.card

import fr.vauguin.clashbattle.card.cost.CardCostPolicy
import fr.vauguin.clashbattle.card.cost.FixedCostPolicy
import fr.vauguin.clashbattle.card.cost.VariableCostPolicy

sealed class Card constructor(val name: String, val costPolicy: CardCostPolicy) {
    override fun toString(): String {
        return name
    }
}

object CardMagus: Card("Magus", FixedCostPolicy(3))
object CardGiantSkeleton: Card("Giant Skeleton", FixedCostPolicy(6))
object CardMirror: Card("Mirror", VariableCostPolicy { other ->
    when (val policy = other.costPolicy) {
        is FixedCostPolicy -> policy.cost + 1
        else -> throw VariableCostBasedOnVariableCostCalculationNotSupported(CardMirror, other)
    }
})