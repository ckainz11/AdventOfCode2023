package days.day05

import days.Day
import util.allLongs
import kotlin.math.max
import kotlin.math.min

class Day5(override val input: String) : Day<Long>(input) {

    val seeds = input.lines().first().allLongs()
    val seeds2 = input.lines().first().allLongs().chunked(2).map { (a, b) -> a..<(a + b) }
    val maps = input.split("\n\n").drop(1)
        .map { map ->
            map.lines().drop(1).associate { line ->
                line.allLongs().let { (dest, source, length) ->
                    source..(source + length) to dest..(dest + length)
                }
            }
        }

    override fun solve1(): Long = seeds.minOfOrNull { seed -> maps.fold(seed) { next, map -> convert1(next, map) } }!!

    private fun convert1(toConvert: Long, map: Map<LongRange, LongRange>): Long {
        for ((sourceRange, destRange) in map) {
            val offset = destRange.first - sourceRange.first
            if (toConvert in sourceRange)
                return toConvert + offset
        }
        return toConvert
    }

    fun convert2(toConvert: LongRange, map: Map<LongRange, LongRange>): List<LongRange> {
        val mappedInputRanges = mutableListOf<LongRange>()
        val outputRanges = map.entries.mapNotNull { (source, dest) ->
            val start = max(source.first, toConvert.first)
            val end = min(source.last, toConvert.last)
            if (start <= end) {
                mappedInputRanges += start..end
                (dest.first - source.first).let { (start + it)..(end + it) }
            } else null
        }
        val cuts =
            listOf(toConvert.first) + mappedInputRanges.flatMap { listOf(it.first, it.last) } + listOf(toConvert.last)
        val unmappedInputRanges = cuts.chunked(2).mapNotNull { (first, second) ->
            if (second > first) if (second == cuts.last()) first..second else first..<second else null
        }
        return outputRanges + unmappedInputRanges
    }

    override fun solve2(): Long =
        seeds2.flatMap { maps.fold(listOf(it)) { acc, map -> acc.flatMap { convert2(it, map) } } }
            .minOf { it.first }
}