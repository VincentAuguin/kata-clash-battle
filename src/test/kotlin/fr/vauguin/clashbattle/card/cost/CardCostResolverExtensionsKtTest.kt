package fr.vauguin.clashbattle.card.cost

import fr.vauguin.clashbattle.card.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

internal class CardCostResolverExtensionsKtTest {

    @Test
    fun `Resolver on previous cost should resolve if the last discarded card has a FixedCostPolicy`() {
        val deck = DefaultDeck()
        deck.discard(CardMagus)
        assertEquals(3, CardMirror.resolveCostOnLastDiscardedCardCost(deck) { 3 })
    }

    @Test
    fun `Resolver on previous cost should throw if the last discarded card has a VariableCostPolicy`() {
        val deck = DefaultDeck()
        deck.discard(CardMirror)
        assertThrows(VariableCostBasedOnVariableCostCalculationNotSupported::class.java) {
            CardMirror.resolveCostOnLastDiscardedCardCost(deck) { 0 }
        }
    }

    @Test
    fun `Resolver on previous cost should throw if there is no discarded card`() {
        val deck = DefaultDeck()
        assertThrows(NoDiscardedCardForCalculation::class.java) {
            CardMirror.resolveCostOnLastDiscardedCardCost(deck) { 0 }
        }
    }

    @Test
    fun `Resolver on previous cost should throw if the cost policy is not variable`() {
        val deck = DefaultDeck()
        assertThrows(CardCostCalculationException::class.java) {
            CardMagus.resolveCostOnLastDiscardedCardCost(deck) { 0 }
        }
    }
}