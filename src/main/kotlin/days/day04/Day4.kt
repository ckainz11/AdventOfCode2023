package days.day04

import days.Day
import util.allInts
import kotlin.math.pow

class Day4(override val input: String) : Day<Int>(input) {
    override fun solve1(): Int = input.lines().sumOf { line ->
        line.split(": ").takeLast(1).first().split("|").take(2)
            .map { it.allInts() }
            .let { (winning, mine) ->
                (2).toFloat().pow(mine.intersect(winning).size - 1).toInt()
            }
    }

    override fun solve2(): Int =
        input.lines().indices.associateWith { 1 }.toMutableMap().let { freq ->
            input.lines().forEachIndexed { i, line ->
                line.split(": ").takeLast(1).first().split("|").take(2)
                    .map { it.allInts() }
                    .let { (winning, mine) ->
                        (1..(mine.intersect(winning).size)).forEach { win ->
                            freq[i + win] = freq[i + win]!! + freq[i]!!
                        }
                    }
            }
            return@let freq
        }.values.sum()


}