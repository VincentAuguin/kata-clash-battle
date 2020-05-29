package fr.vauguin.clashbattle.elixir

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import java.time.Duration
import kotlin.coroutines.coroutineContext

interface ElixirProducer {
    suspend fun startProducing(onProduced: (quantity: Int) -> Unit)
    fun stopProducing(reason: String)
}

@ExperimentalCoroutinesApi
class DefaultElixirProducer constructor(
    private val elixirsProducedByCycle: Int = DEFAULT_ELIXIRS_PRODUCED_BY_CYCLE,
    private val cycleDuration: Duration = DEFAULT_ELIXIRS_CYCLE_DURATION
) : ElixirProducer {
    private var productionScope: CoroutineScope? = null

    override suspend fun startProducing(onProduced: (quantity: Int) -> Unit) {
        CoroutineScope(coroutineContext).launch {
            if (productionScope == null) {
                produce<Int> { produce() }.also { channel ->
                    productionScope = this
                    consumeProduction(channel, onProduced)
                }
            }
        }
    }

    override fun stopProducing(reason: String) {
        productionScope?.cancel(CancellationException(reason))
        productionScope = null
    }

    private suspend fun ProducerScope<Int>.produce() {
        while (isActive) {
            delay(cycleDuration.toMillis())
            send(elixirsProducedByCycle)
        }
        close()
    }

    private suspend fun consumeProduction(channel: ReceiveChannel<Int>, onReceived: (elixirs: Int) -> Unit) =
        coroutineScope {
            launch {
                for (quantity in channel)
                    onReceived(quantity)
            }
        }

    companion object {
        const val DEFAULT_ELIXIRS_PRODUCED_BY_CYCLE = 1
        val DEFAULT_ELIXIRS_CYCLE_DURATION = Duration.ofSeconds(1)
    }
}