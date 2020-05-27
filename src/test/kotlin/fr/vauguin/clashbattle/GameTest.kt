package fr.vauguin.clashbattle

import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(MockKExtension::class)
class GameTest {
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
    fun `Game should increment elixirs for each passed seconds`() = runBlockingTest {
        game.elixirs = 0
        val production = launch { game.startProducingElixirs() }
        advanceTimeBy(2_000)
        production.cancel("End of test")
        assertEquals(2, game.elixirs)
    }

    @Test
    fun `Game should not increment elixirs if a second is not completely passed`() = runBlockingTest {
        game.elixirs = 0
        val production = launch { game.startProducingElixirs() }
        advanceTimeBy(999)
        production.cancel("End of test")
        assertEquals(0, game.elixirs)
    }

    @Test
    fun `Game should not have multiple elixirs production in parallel`() = runBlockingTest {
        game.elixirs = 0
        val production1 = launch { game.startProducingElixirs() }
        val production2 = launch { game.startProducingElixirs() }
        advanceTimeBy(1_000)
        production1.cancel("End of test")
        production2.cancel("End of test")
        assertEquals(1, game.elixirs)
    }
}