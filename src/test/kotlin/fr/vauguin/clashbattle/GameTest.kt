package fr.vauguin.clashbattle

import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

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
}