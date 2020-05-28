package fr.vauguin.clashbattle

import fr.vauguin.clashbattle.card.Card
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
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
    fun `Game should allow to put a card if elixirs is enough`() {
        val card = Card("Magus", 3)
        assertTrue(game.putCard(card))
    }

    @Test
    fun `Game should not allow to put a card if elixirs is not enough`() {
        val card = Card("Magus", 6)
        assertFalse(game.putCard(card))
    }

    @Test
    fun `Game should substract the cost of the put card from elixirs`() {
        val card = Card("Magus", 3)
        game.putCard(card)
        assertEquals(2, game.elixirs)
    }
}