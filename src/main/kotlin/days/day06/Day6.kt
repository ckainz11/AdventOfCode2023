package days.day06

import days.Day
import util.allInts

class Day6(override val input: String) : Day<Long>(input) {
    override fun solve1(): Long =
        input.lines().map { it.allInts() }.let { (times, distances) -> times.zip(distances) }.map { (time, distance) ->
            ((time - ((1..<time).first { (time - it) * it > distance } * 2)) + 1).toLong()
        }.reduce(Long::times)

    override fun solve2(): Long =
        input.lines().map { it.split(" ").drop(1).joinToString("").toLong() }.let { (time, distance) ->
            (time - ((1..<time).first { (time - it) * it > distance } * 2L)) + 1L
        }
}