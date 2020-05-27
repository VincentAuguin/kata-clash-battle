package fr.vauguin.clashbattle

import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class GameTest {
    lateinit var game: Game

    @BeforeEach
    fun setUp() {
        game = Game()
    }
}