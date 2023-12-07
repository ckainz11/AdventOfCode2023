package util

import days.Day
import days.day01.Day1
import days.day02.Day2
import days.day03.Day3
import days.day04.Day4
import days.day05.Day5
import days.day06.Day6
import days.day07.Day7


class DayFactory {
    companion object {
        val days = mapOf(
            1 to ::Day1,
            2 to ::Day2,
            3 to ::Day3,
            4 to ::Day4,
            5 to ::Day5,
            6 to ::Day6,
            7 to ::Day7
        )

        fun getDayObject(day: Int, input: String): Day<out Any> {
            return days[day]?.invoke(input) ?: error("this day doesnt have a solution yet")
        }
    }


}