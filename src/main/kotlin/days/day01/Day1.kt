package days.day01

import days.Day

class Day1(override val input: String) : Day<Int>(input) {
    override fun solve1(): Int = input.lines().sumOf { line -> (line.first { c -> c.isDigit()}.toString() + line.last { c -> c.isDigit() }).toInt() }


    private val digits = mapOf(
        "one" to 1,
        "two" to 2,
        "three" to 3,
        "four" to 4,
        "five" to 5,
        "six" to 6,
        "seven" to 7,
        "eight" to 8,
        "nine" to 9,
        "1" to 1,
        "2" to 2,
        "3" to 3,
        "4" to 4,
        "5" to 5,
        "6" to 6,
        "7" to 7,
        "8" to 8,
        "9" to 9
    )

    override fun solve2(): Int = input.lines().sumOf {
        line -> digits[line.findAnyOf(digits.keys)?.second]!! * 10 + digits[line.findLastAnyOf(digits.keys)?.second]!!
    }
}