package days.day09

import days.Day
import util.allInts

class Day9(override val input: String) : Day<Int>(input) {

    private val parsed =
        input.lines().map { line ->
            generateSequence(line.allInts()) {
                it.zipWithNext { a, b -> b - a }
            }.takeWhile { !it.all { num -> num == 0 } }
        }

    override fun solve1(): Int = parsed.sumOf { s -> s.sumOf { it.last() } }

    override fun solve2(): Int = parsed.sumOf { s -> s.toList().foldRight(0) { ints, acc -> ints.first() - acc }.toInt() }
}