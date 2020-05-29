package fr.vauguin.clashbattle

import fr.vauguin.clashbattle.card.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

class Main {
    @ExperimentalCoroutinesApi
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            println("👑 Starting the battle !")
            val game = Game(
                setOf(
                    CardGiantSkeleton,
                    CardMagus,
                    CardMirror,
                    CardBabyDragon,
                    CardMusketeer,
                    CardValkyrie,
                    CardZapper,
                    CardPrince
                )
            )
            runBlocking {
                game.startProducingElixirs()
                delay(4_000)
                game.tryToPutCard(CardGiantSkeleton)
                delay(2_000)
                game.tryToPutCard(CardMagus)
                delay(7_000)
                game.tryToPutCard(CardMirror)
                delay(1_000)
                game.stopProducingElixirs()
            }
            println("🏳 Battle is finish !️")
        }
    }
}