package days.day07

import days.Day

class Day7(override val input: String) : Day<Int>(input) {
    override fun solve1(): Int = input.lines().asSequence().map {
        it.split(" ").let { (s, bid) ->
            Hand.from(s) to bid.toInt()
        }
    }.sortedBy { it.first }.mapIndexed { index, pair -> (index + 1) * pair.second }.sum()

    override fun solve2(): Int = input.lines().asSequence().map {
        it.split(" ").let { (s, bid) ->
            Hand.from(s, true) to bid.toInt()
        }
    }.sortedBy { it.first }
        .mapIndexed { index, pair -> (index + 1) * pair.second }.sum()

    data class Hand(val cards: List<Char>, val type: HandType, val part2: Boolean = false) : Comparable<Hand> {
        companion object {
            fun from(line: String, part2: Boolean = false): Hand =
                line.groupingBy { it }.eachCount().map { it.value }.sortedDescending().let { freq ->
                    if (part2) Hand(line.toList(), evaluateHandTypePart2(freq, line.count { it == 'J' }), true)
                    else Hand(line.toList(), evaluateHandType(freq))
                }


            private fun evaluateHandType(freq: List<Int>): HandType {
                return when (freq) {
                    listOf(5) -> HandType.FIVE_OF_A_KIND
                    listOf(3, 2) -> HandType.FULL_HOUSE
                    listOf(4, 1) -> HandType.FOUR_OF_A_KIND
                    listOf(3, 1, 1) -> HandType.THREE_OF_A_KIND
                    listOf(2, 2, 1) -> HandType.TWO_PAIRS
                    listOf(2, 1, 1, 1) -> HandType.ONE_PAIR
                    else -> HandType.HIGH_CARD
                }
            }

            private fun evaluateHandTypePart2(freq: List<Int>, jokerCount: Int): HandType {
                return if (jokerCount == 0) evaluateHandType(freq)
                else when (freq) {
                    listOf(5 - jokerCount, jokerCount).filter { it != 0 }.sortedDescending() -> HandType.FIVE_OF_A_KIND
                    listOf(4 - jokerCount, 1, jokerCount).filter { it != 0 }
                        .sortedDescending() -> HandType.FOUR_OF_A_KIND

                    listOf(3 - jokerCount, 2, jokerCount).filter { it != 0 }.sortedDescending() -> HandType.FULL_HOUSE
                    listOf(3 - jokerCount, 1, 1, jokerCount).filter { it != 0 }
                        .sortedDescending() -> HandType.THREE_OF_A_KIND

                    listOf(2 - jokerCount, 2, 1, jokerCount).filter { it != 0 }.sortedDescending() -> HandType.TWO_PAIRS
                    listOf(2 - jokerCount, 1, 1, 1, jokerCount).filter { it != 0 }
                        .sortedDescending() -> HandType.ONE_PAIR

                    else -> HandType.HIGH_CARD
                }
            }


        }

        private fun charToCardValue(c: Char): Int = when {
            c == 'A' -> 14
            c == 'K' -> 13
            c == 'Q' -> 12
            part2 && c == 'J' -> 1
            !part2 && c == 'J' -> 11
            c == 'T' -> 10
            else -> c.digitToInt()
        }

        override fun compareTo(other: Hand): Int {
            val typeDelta = this.type.ordinal - other.type.ordinal
            if (typeDelta != 0)
                return typeDelta

            this.cards.zip(other.cards).forEach { (myCard, otherCard) ->
                val cardDelta = charToCardValue(myCard) - charToCardValue(otherCard)
                if (cardDelta != 0)
                    return cardDelta
            }
            return 0
        }


    }

    enum class HandType {
        HIGH_CARD,
        ONE_PAIR,
        TWO_PAIRS,
        THREE_OF_A_KIND,
        FULL_HOUSE,
        FOUR_OF_A_KIND,
        FIVE_OF_A_KIND
    }
}