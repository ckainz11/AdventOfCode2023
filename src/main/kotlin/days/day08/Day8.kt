package days.day08

import days.Day
import util.leastCommonMultiple

class Day8(override val input: String) : Day<Long>(input) {

    private val parsed = input.split("\n\n").let { (instructions, nodes) ->
        instructions to nodes.lines().map { it.split(" = ") }
            .associate { it[0] to it[1].drop(1).dropLast(1).split(", ").let { (left, right) -> left to right } }
    }

    override fun solve1(): Long = parsed.let { (instructions, nodes) ->
        var node = "AAA"
        var depth = 0

        while (node != "ZZZ") {
            node = if (instructions[depth % instructions.length] == 'L') nodes[node]!!.first else nodes[node]!!.second
            depth++
        }
        depth.toLong()
    }

    override fun solve2(): Long = parsed.let { (instructions, nodes) ->
        nodes.keys.filter { it.endsWith("A") }.map { start ->
            var depth = 0L
            var current = start

            while (!current.endsWith("Z")) {
                current = if (instructions[(depth % instructions.length).toInt()] == 'L') nodes[current]!!.first else nodes[current]!!.second
                depth++
            }
            depth
        }.reduce { acc, i -> leastCommonMultiple(acc, i) }
    }


}