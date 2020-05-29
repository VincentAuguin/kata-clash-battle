package fr.vauguin.clashbattle.card

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class DefaultDeckTest {

    lateinit var deck: DefaultDeck

    @BeforeEach
    fun setUp() {
        deck = DefaultDeck(setOf(CardMusketeer, CardBabyDragon, CardMirror, CardMagus, CardGiantSkeleton, CardZapper))
    }

    @Test
    fun `Deck should have the same size as the initial content if no card has been discard`() {
        val content = setOf(CardMagus, CardMirror, CardGiantSkeleton)
        deck = DefaultDeck(content)
        assertEquals(content.count(), deck.count())
    }

    @Test
    fun `Deck should have the same size as the initial content plus all the discarded cards`() {
        val content = setOf(CardMagus, CardMirror)
        deck = DefaultDeck(content)
        deck.discard(CardGiantSkeleton)
        assertEquals(content.count() + 1, deck.count())
    }

    @Test
    fun `Deck should provide the last discarded card`() {
        deck.discard(CardMagus)
        assertEquals(CardMagus, deck.getLastDiscarded())
    }

    @Test
    fun `Deck should provide null if no card has been discarded`() {
        deck = DefaultDeck(setOf(CardMagus, CardMirror))
        assertEquals(null, deck.getLastDiscarded())
    }
}