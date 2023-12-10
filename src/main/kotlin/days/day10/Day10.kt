package days.day10

import days.Day
import util.Point
import util.get
import kotlin.math.abs

class Day10(override val input: String) : Day<Int>(input) {

    private val grid = input.lines().map { it.toList() }
    private val start = grid.indexOfFirst { it.contains('S') }.let { Point(grid[it].indexOf('S'), it) }
    private val loop = generateSequence(start + Point.DOWN to Point.DOWN) { (loc, dir) ->
        nextDirection(loc, dir).let { loc + it to it }
    }.map { it.first }.takeWhile { it != start }.toList() + start


    override fun solve1(): Int = loop.size / 2

    override fun solve2(): Int = (1..<grid.size - 1).sumOf { y ->
        val idy = grid[y].indices.filter { x ->
            (0..1).map { loop.indexOf(Point(x, y + it)) }.let { (i1, i2) ->
                i1 != -1 && i2 != -1 && (abs(i1 - i2) == 1 || i1 in listOf(0, loop.lastIndex) && i2 in listOf(0, loop.lastIndex))
            }
        }
        (idy.indices step 2).sumOf { i -> (idy[i]..idy[i + 1]).count { x -> Point(x, y) !in loop } }
    }


    private fun nextDirection(pos: Point, dir: Point): Point = when (grid[pos]) {
        '|' -> dir
        '-' -> dir
        'L' -> if (dir == Point.DOWN) Point.RIGHT else Point.UP
        'J' -> if (dir == Point.DOWN) Point.LEFT else Point.UP
        'F' -> if (dir == Point.UP) Point.RIGHT else Point.DOWN
        '7' -> if (dir == Point.UP) Point.LEFT else Point.DOWN
        else -> throw IllegalArgumentException("Character at position $pos is not a pipe!")

    }
}