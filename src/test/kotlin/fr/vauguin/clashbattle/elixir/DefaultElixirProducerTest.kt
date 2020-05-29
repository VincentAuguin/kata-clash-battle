package fr.vauguin.clashbattle.elixir

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Duration

@ExperimentalCoroutinesApi
internal class DefaultElixirProducerTest {
    lateinit var producer: DefaultElixirProducer

    @BeforeEach
    fun setUp() {
        producer = DefaultElixirProducer()
    }

    @Test
    fun `Producer should produce one elixir each seconds by default`() = runBlockingTest {
        var producedQuantity = 0
        producer.startProducing { producedQuantity += it }
        advanceTimeBy(2_000)
        producer.stopProducing("End of test")
        assertEquals(2, producedQuantity)
    }

    @Test
    fun `Producer should not produce elixirs if a second is not completely passed`() = runBlockingTest {
        var producedQuantity = 0
        producer.startProducing { producedQuantity += it }
        advanceTimeBy(999)
        producer.stopProducing("End of test")
        assertEquals(0, producedQuantity)
    }

    @Test
    fun `Producer should produce the defined amount of elixirs per cycle`() = runBlockingTest {
        var producedQuantity = 0
        producer = DefaultElixirProducer(elixirsProducedByCycle = 3)
        producer.startProducing { producedQuantity += it }
        advanceTimeBy(2_000)
        producer.stopProducing("End of test")
        assertEquals(6, producedQuantity)
    }

    @Test
    fun `Producer should produce elixirs with the defined cycle duration`() = runBlockingTest {
        var producedQuantity = 0
        producer =
            DefaultElixirProducer(cycleDuration = Duration.ofMillis(250))
        producer.startProducing { producedQuantity += it }
        advanceTimeBy(1_000)
        producer.stopProducing("End of test")
        assertEquals(4, producedQuantity)
    }

    @Test
    fun `Producer should not have multiple elixirs production in parallel`() = runBlockingTest {
        var producedQuantity = 0
        producer.startProducing { producedQuantity += it }
        producer.startProducing { producedQuantity += it } // start again
        advanceTimeBy(1_000)
        producer.stopProducing("End of test")
        assertEquals(1, producedQuantity)
    }
}