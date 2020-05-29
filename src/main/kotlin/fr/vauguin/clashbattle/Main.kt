package fr.vauguin.clashbattle

import fr.vauguin.clashbattle.card.CardGiantSkeleton
import fr.vauguin.clashbattle.card.CardMagus
import fr.vauguin.clashbattle.card.CardMirror
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
                println("👑 Starting the battle !")
                game.startProducingElixirs()
                delay(4_000)
                game.tryToPutCard(CardGiantSkeleton)
                delay(2_000)
                game.tryToPutCard(CardMagus)
                delay(7_000)
                game.tryToPutCard(CardMirror)
                delay(1_000)
                game.stopProducingElixirs()
                println("🏳 Battle is finish !️")
            }
        }
    }
}