package fr.vauguin.clashbattle.card

abstract class CardCostCalculationException(card: Card, reason: String) :
    Exception("The card '$card' is unable to calculate its cost because $reason")

class VariableCostBasedOnVariableCostCalculationNotSupported(card: Card, basedOn: Card) :
    CardCostCalculationException(card, "it is based on another variable cost ($basedOn)")

class NoDiscardedCardForCalculation(card: Card) :
    CardCostCalculationException(card, "there is no discarded card in the deck")

class NotVariableCostPolicy(card: Card) :
    CardCostCalculationException(card, "the cost policy is not variable type")