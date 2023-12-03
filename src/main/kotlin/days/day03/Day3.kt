package days.day03

import days.Day
import util.*

class Day3(override val input: String) : Day<Int>(input) {

    private val numReg = """\d+""".toRegex()
    private val inputMatrix = matrixOf(input.lines().map { it.toList() })

    override fun solve1(): Int {

        return input.lines().mapIndexed { y, line ->
            numReg.findAll(line).sumOf { num ->
                if (getSurroundingCoordinatesFor(y, num.range).all {
                        try {
                            inputMatrix[it] == '.'
                        } catch (ex: IndexOutOfBoundsException) {
                            true
                        }
                    }) 0
                else num.value.toInt()
            }
        }.sum()
    }

    private fun getSurroundingCoordinatesFor(row: Int, rng: IntRange): List<Point> = buildList {
        (row - 1..row + 1).forEach { y ->
            (rng.first - 1..rng.last + 1).forEach { x ->
                if (y != row || x !in rng)
                    add(Point(x, y))
            }
        }
    }


    override fun solve2(): Int {
        val nums: MutableMap<Point, Pair<Point, Int>> = mutableMapOf()
        input.lines().forEachIndexed { y, line ->
            numReg.findAll(line).forEach { matchResult ->
                matchResult.range.forEach { x -> nums[Point(x,y)] = Pair(Point(y, matchResult.range.first), matchResult.value.toInt()) }
            }
        }

        var sum = 0

        inputMatrix.matrixForEachIndexed {p, c ->
            if(c == '*') {
                val adjNums: MutableSet<Pair<Point, Int>> = emptySet<Pair<Point, Int>>().toMutableSet()
                Point.directions.forEach {
                    val next = p + it
                    val num = nums[next]
                    if(num != null)
                        adjNums.add(num)
                }
                if (adjNums.size == 2)
                    sum += adjNums.map { it.second }.reduce(Int::times)
            }

        }
        return sum
    }

}