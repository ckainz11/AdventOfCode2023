package util

import days.Day
import days.day01.Day1
import days.day02.Day2
import days.day03.Day3
import days.day04.Day4
import days.day05.Day5
import days.day06.Day6
import days.day07.Day7
import days.day08.Day8
import days.day09.Day9
import days.day10.Day10
import days.day11.Day11
import days.day12.Day12
import days.day13.Day13


class DayFactory {
    companion object {
        val days = mapOf(
            1 to ::Day1,
            2 to ::Day2,
            3 to ::Day3,
            4 to ::Day4,
            5 to ::Day5,
            6 to ::Day6,
            7 to ::Day7,
            8 to ::Day8,
            9 to ::Day9,
            10 to ::Day10,
            11 to ::Day11,
            12 to ::Day12,
            13 to ::Day13
        )

        fun getDayObject(day: Int, input: String): Day<out Any> {
            return days[day]?.invoke(input) ?: error("this day doesnt have a solution yet")
        }
    }


}