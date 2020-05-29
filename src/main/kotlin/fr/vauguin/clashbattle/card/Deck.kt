package fr.vauguin.clashbattle.card

import java.util.*

interface Deck {
    fun count(): Int
    fun draw(): Card?
    fun discard(card: Card)
    fun getLastDiscarded(): Card?
}

class DefaultDeck(content: Set<Card>) : Deck {

    private val queue: Queue<Card> = LinkedList(content)

    private var hasDiscardedOnce = false

    override fun draw(): Card? = queue.poll()

    override fun discard(card: Card) {
        queue.offer(card)
        hasDiscardedOnce = true
    }

    override fun getLastDiscarded(): Card? = queue.takeIf { hasDiscardedOnce }?.lastOrNull()

    override fun count(): Int = queue.count()
}

class DeckIsEmpty : Exception("Unable to draw a card because the deck is empty")