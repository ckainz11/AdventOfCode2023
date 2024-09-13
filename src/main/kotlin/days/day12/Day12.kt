package days.day12

import days.Day
import util.allInts

class Day12(override val input: String) : Day<Long>(input) {
    override fun solve1(): Long = input.lines().map { line -> line.split(" ")
        .let { it[0] to it[1].allInts() } }
        .sumOf { (springs, groups) -> countArrangements(springs, groups) }

    override fun solve2(): Long = input.lines().map { line -> line.split(" ")
        .let {"${it[0]}?".repeat(5).dropLast(1) to "${it[1]},".repeat(5).allInts()} }
        .sumOf { (springs, groups) -> countArrangements(springs, groups) }

    private val cache = hashMapOf<Pair<String, List<Int>>, Long>()
    private fun countArrangements(springs: String, groups: List<Int>): Long {
        if (groups.isEmpty()) return if ('#' in springs) 0 else 1;
        if (springs.isEmpty()) return 0;
        return cache.getOrPut(springs to groups) {
            var result = 0L
            if (springs.first() in ".?") // treat ? as .
                result += countArrangements(springs.drop(1), groups)
            if (springs.first() in "#?" // treat ? as #
                && groups.first() <= springs.length // group must be smaller/equal than remaining springs
                && "." !in springs.take(groups.first()) // group should not contain '.'
                && (groups.first() == springs.length || springs[groups.first()] != '#')) // group should be at the end or not have a # after
                result += countArrangements(springs.drop(groups.first() + 1), groups.drop(1))
            result
        }
    }
}