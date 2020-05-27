package fr.vauguin.clashbattle

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import kotlin.coroutines.coroutineContext

@ExperimentalCoroutinesApi
class Game {
    var elixirs = INITIAL_ELIXIRS
        set(value) {
            field = value.coerceAtLeast(MINIMUM_ELIXIRS).coerceAtMost(MAXIMUM_ELIXIRS)
        }

    private var elixirsProduction: ReceiveChannel<Int>? = null

    suspend fun startProducingElixirs() {
        if (elixirsProduction != null)
            return

        CoroutineScope(coroutineContext).run {
            produce<Int> { produceElixirs() }.also { channel ->
                elixirsProduction = channel
                storeElixirs(channel)
            }
        }
    }

    private suspend fun ProducerScope<Int>.produceElixirs() {
        while (isActive) {
            delay(INITIAL_ELIXIRS_CYCLE_MILLIS)
            send(ELIXIRS_PRODUCED_PER_CYCLE)
        }
        close()
    }

    private suspend fun CoroutineScope.storeElixirs(channel: ReceiveChannel<Int>) = launch {
        for (quantity in channel)
            elixirs += quantity
    }

    companion object {
        private const val MINIMUM_ELIXIRS = 0
        private const val MAXIMUM_ELIXIRS = 10
        private const val INITIAL_ELIXIRS = 5
        private const val INITIAL_ELIXIRS_CYCLE_MILLIS = 1_000L
        private const val ELIXIRS_PRODUCED_PER_CYCLE = 1
    }
}