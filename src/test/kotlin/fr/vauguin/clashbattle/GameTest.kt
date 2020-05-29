package fr.vauguin.clashbattle

import fr.vauguin.clashbattle.card.*
import fr.vauguin.clashbattle.elixir.ElixirProducer
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(MockKExtension::class)
internal class GameTest {
    lateinit var game: Game

    @BeforeEach
    fun setUp() {
        game = Game(setOf(CardMagus, CardGiantSkeleton, CardMirror, CardBabyDragon, CardZapper))
    }

    @Test
    fun `Game should provide 5 elixirs at the beginning`() {
        assertEquals(5, game.elixirs)
    }

    @Test
    fun `Game should coerce at least 0 elixir`() {
        game.elixirs = -1
        assertEquals(0, game.elixirs)
        game.elixirs = Int.MIN_VALUE
        assertEquals(0, game.elixirs)
    }

    @Test
    fun `Game should coerce at most 10 elixirs`() {
        game.elixirs = 11
        assertEquals(10, game.elixirs)
        game.elixirs = Int.MAX_VALUE
        assertEquals(10, game.elixirs)
    }

    @Test
    fun `Game should not throw when starting and stopping to produce elixirs`() = runBlockingTest {
        assertDoesNotThrow {
            launch {
                game.startProducingElixirs()
                delay(1_000)
                game.stopProducingElixirs()
            }
        }
    }

    @Test
    fun `Game should stop elixir production when the ElixirProducer is changed`() {
        val producer1 = mockk<ElixirProducer>(relaxed = true)
        game.setElixirProducer(producer1)
        val producer2 = mockk<ElixirProducer>(relaxed = true)
        game.setElixirProducer(producer2)
        verify { producer1.stopProducing(any()) }
    }

    @Test
    fun `Game should allow to put a card if elixirs is enough`() {
        assertTrue(game.tryToPutCard(CardMagus))
    }

    @Test
    fun `Game should not allow to put a card if elixirs is not enough`() {
        assertFalse(game.tryToPutCard(CardGiantSkeleton))
    }

    @Test
    fun `Game should substract the cost of the put card from elixirs`() {
        game.tryToPutCard(CardMagus)
        assertEquals(2, game.elixirs)
    }

    @Test
    fun `Game should not substract the cost if the card to put is not allowed`() {
        game.tryToPutCard(CardMusketeer)
        assertEquals(5, game.elixirs)
    }

    @Test
    fun `Game should be able to calculate a card's cost based on the previous put card`() {
        game.elixirs = 10
        game.tryToPutCard(CardMagus)
        game.tryToPutCard(CardMirror)
        assertEquals(3, game.elixirs)
    }

    @Test
    fun `Game should not allow to put a card if the calculation cost is impossible`() {
        assertFalse(game.tryToPutCard(CardMirror))
    }

    @Test
    fun `Game should have a hand with the first 4 cards of the deck`() {
        assertEquals(4, game.getHandCards().count())
    }

    @Test
    fun `Game should throw if there is no card is no card in the deck when drawing`() {
        assertThrows<DeckIsEmpty> {
            game = Game()
        }
    }

    @Test
    fun `Game should discard the put card from the hand to the deck`() {
        val card = game.getHandCards().elementAt(0)
        game.tryToPutCard(card)
        assertFalse(game.getHandCards().contains(card))
    }

    @Test
    fun `Game should draw a card after a card is put`() {
        game.tryToPutCard(game.getHandCards().elementAt(0))
        assertEquals(4, game.getHandCards().size)
    }

    @Test
    fun `Game should not allow to put a card if the deck is empty`() {
        game = Game(setOf(CardMagus, CardMirror, CardZapper, CardBabyDragon))
        assertFalse(game.tryToPutCard(CardMagus))
    }

    @Test
    fun `Game should not allow to put card if the card is not in the hand`() {
        game = Game(setOf(CardMagus, CardMirror, CardZapper, CardBabyDragon, CardGiantSkeleton))
        assertFalse(game.tryToPutCard(CardGiantSkeleton))
    }
}