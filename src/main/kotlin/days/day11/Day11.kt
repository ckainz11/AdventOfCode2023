package days.day11

import days.Day
import util.*

class Day11(override val input: String) : Day<Long>(input) {

    private val grid = input.lines().map { it.toList() }
    private val galaxies = grid.mapMatrixIndexedNotNull { point, c -> if (c == '#') point else null }
    override fun solve1(): Long = solve(2L)

    override fun solve2(): Long = solve(1_000_000L)

    private fun solve(expand: Long): Long =
        listOf(grid, grid.transposed(1)).map { matrix -> matrix.map { list -> if (list.all { it == '.' }) expand else 1 } }
            .let { (rowHeights, colWidths) ->
                galaxies.map { p -> Point(colWidths.take(p.x + 1).sum().toInt(), rowHeights.take(p.y + 1).sum().toInt()) }
                    .pairwise { point, point2 -> point.mDist(point2).toLong() }.sum()
                }
}