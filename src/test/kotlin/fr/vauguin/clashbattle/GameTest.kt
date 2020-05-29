package fr.vauguin.clashbattle

import fr.vauguin.clashbattle.card.CardGiantSkeleton
import fr.vauguin.clashbattle.card.CardMagus
import fr.vauguin.clashbattle.card.CardMirror
import fr.vauguin.clashbattle.elixir.DefaultElixirProducer
import fr.vauguin.clashbattle.elixir.ElixirProducer
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.impl.annotations.SpyK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(MockKExtension::class)
internal class GameTest {
    lateinit var game: Game

    @BeforeEach
    fun setUp() {
        game = Game()
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
}