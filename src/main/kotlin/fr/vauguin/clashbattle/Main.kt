package fr.vauguin.clashbattle

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

class Main {
    @ExperimentalCoroutinesApi
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val game = Game()
            runBlocking {
                game.startProducingElixirs()
                delay(3_000)
                game.stopProducingElixirs()
            }
        }
    }
}