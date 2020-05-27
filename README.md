# Kata : Clash Battle
A kata aiming to recreate the mana (elixir) mechanism inspired from the game [Clash Royale](https://clashroyale.com/).

## Forewords

As a player of the famous mobile game Clash Royale, the idea stroke me just before getting to bed, Then I've decide to create a kata exercice of my own.

## Principle

A *kata* is a martial art ceremony that the judokas perform to acquire a new belt. This principle has been imported into the developers community. The goal is to create an algorithm step by step as the difficulty inscreases. A good way to resolve a kata is by coding it with [TDD](https://en.wikipedia.org/wiki/Test-driven_development).

## Language

You are completly free to code this kata with any language. For now, the solution will only be available in *kotlin*, but maybe one day other languages will follow.

## Subject

You have to recreate a mechanism that add some elixir over time. This elixir can be used by the player to pay for cards for a certain cost.
The specifications are listed below :

1. The player has a gauge of elixir from 0 to 10. The elixirs is represented as an integer and then a player can have for instance 6, 8 or 0 elixirs but not 0.4, 8.5 or any non-integer value. The player starts with 5 elixirs.
2. The player gains an elixir for each passed second.
3. The game can decide to change the speed of elixir points acquisition. For instance, the new speed can be twice the default speed (which is 1 second) and then would be 0.5 second.
4. The game should prompt the gauge (console prompt is fine).
5. The player has the possibility to lay a card. A card have a title and a cost (elixir point(s)). For now, the card is always the same and always available in his hand. The player can't lay a card if he have not enough elexir points. When the player lays a card, the card's cost is substracted to his total elixir points.
6. Create a card with variable cost : the cost is the cost of the last layed card plus one. For instance, if the player lay a card with a cost of 5, then the cost of this card will be 6.
7. The player has always an hand with exactly and always 4 cards available. Each time he lays a card, a new card is drawn and put in the player hand.
8. The player has a deck of 8 different cards. The drawn cards for the player are comming randomly from this deck. The player's hand can't have 2 same cards.
9. The drawn card is always the one on top of the deck. When a card is layed, this card is put at the end of deck. With this mechanism, the order is always the same and looping.
