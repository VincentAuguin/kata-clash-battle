package fr.vauguin.clashbattle.card

import java.util.*

interface Deck {
    fun count(): Int
    fun discard(card: Card)
    fun getLastDiscarded(): Card?
}

class DefaultDeck(content: Collection<Card> = emptyList()) : Deck {

    private val queue: Queue<Card> = LinkedList(content)

    private var hasDiscardedOnce = false

    override fun discard(card: Card) {
        queue.offer(card)
        hasDiscardedOnce = true
    }

    override fun getLastDiscarded(): Card? = queue.takeIf { hasDiscardedOnce }?.lastOrNull()

    override fun count(): Int = queue.count()
}