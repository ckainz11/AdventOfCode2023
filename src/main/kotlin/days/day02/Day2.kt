package days.day02

import days.Day
import util.firstInt

class Day2(override val input: String) : Day<Int>(input) {

    val rex = "(((1[3-9]|20)\\sred)|((1[4-9]|20)\\sgreen)|((1[5-9]|20)\\sblue))".toRegex()

    override fun solve1(): Int = input.lines().sumOf { line -> if (rex.containsMatchIn(line)) 0 else line.firstInt() }

    val redRex = "(\\d+\\sred)".toRegex()
    val greenRex = "(\\d+\\sgreen)".toRegex()
    val blueRex = "(\\d+\\sblue)".toRegex()

    val rexList = listOf(redRex, greenRex, blueRex)

    override fun solve2(): Int = input.lines().sumOf { line ->
        rexList.fold(1) { acc, r -> acc * r.findAll(line).maxOf { i -> i.value.firstInt() } }.toInt()
    }
}