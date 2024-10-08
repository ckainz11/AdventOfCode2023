package util

import java.lang.IllegalArgumentException
import java.lang.IndexOutOfBoundsException
import java.util.*
import kotlin.math.abs
import kotlin.math.sign

typealias Matrix<T> = List<List<T>>
typealias MutableMatrix<T> = MutableList<MutableList<T>>

fun <T> matrixOf(vararg rows: List<T>): Matrix<T> = List(rows.size) { i -> rows[i] }
fun <T> matrixOf(rows: List<List<T>>): Matrix<T> = List(rows.size) { i -> rows[i] }
fun <T> Matrix<T>.toMutableMatrix(): MutableMatrix<T> = this.map { it.toMutableList() }.toMutableList()
fun <T> Matrix<T>.getColumn(col: Int): List<T> = getCol(this, col)
fun <T> Matrix<T>.columns() = transposed(1)
operator fun <T> Matrix<T>.get(p: Point): T = this[p.y][p.x]
operator fun <T> MutableMatrix<T>.set(p: Point, value: T) {
    this[p.y][p.x] = value
}

fun <T, R> Matrix<T>.mapMatrix(transform: (T) -> R): Matrix<R> = this.map { it.map(transform) }
fun <T, R> Matrix<T>.mapMatrixIndexed(transform: (Point, T) -> R): Matrix<R> =
    this.mapIndexed { y, row -> row.mapIndexed { x, col -> transform(Point(y, x), col) } }

fun <T> Matrix<T>.matrixForEachIndexed(action: (Point, T) -> Unit) {
    this.forEachIndexed { y, row -> row.forEachIndexed { x, col -> action(Point(x, y), col) } }
}

fun <T> Matrix<T>.sum(sum: (T) -> Int): Int = this.sumOf { row -> row.sumOf { sum(it) } }

/**
 * Returns index of the first element matching the given [predicate], or -1 if the list does not contain such element.
 */
fun <T> Matrix<T>.matrixIndexOfFirst(predicate: (T) -> Boolean): Point {
    for ((y, row) in this.withIndex()) {
        for ((x, value) in row.withIndex()) {
            if (predicate(value))
                return Point(x, y)
        }
    }
    return Point(-1, -1)
}

fun <T> Matrix<T>.subMatrix(rows: Int, cols: Int, index: Point): Matrix<T> {
    val sub = mutableListOf<List<T>>()
    for (y in index.y until index.y + rows) {
        sub.add(this[y].subList(index.x, index.x + cols))
    }
    return sub
}

fun <T> Matrix<T>.matrixToString(): String = this.joinToString("\n") { it.joinToString(", ") }
fun <T : Comparable<T>> Matrix<T>.matrixMax(): T = this.mapNotNull { it.maxOrNull() }.maxOrNull()!!
fun <T : Comparable<T>> Matrix<T>.matrixMin(): T = this.mapNotNull { it.minOrNull() }.minOrNull()!!
fun <T> Matrix<T>.getColNum(): Int = this[0].size
fun <T> Matrix<T>.getRowNum(): Int = this.size
fun <T> Matrix<T>.transposed(times: Int = 1): Matrix<T> = transposeMatrix(this, times)
fun <T> emptyMatrixOf(rows: Int, columns: Int, default: T) = MutableList(rows) { MutableList(columns) { default } }
fun <T> Matrix<T>.count(predicate: (T) -> Boolean) = this.sumOf { it.count(predicate) }
fun <T> Matrix<T>.getAdjacent(row: Int, col: Int): List<T> =
    this.getAdjacentCoordinates(row, col).map { it -> this[it.y][it.x] }

fun <T> Matrix<T>.getAdjacentCoordinates(row: Int, col: Int): List<Point> {
    val adjacent = mutableListOf<Point>()
    if (col != 0) adjacent.add(Point(col - 1, row))
    if (col != this.getColNum() - 1) adjacent.add(Point(col + 1, row))
    if (row != 0) adjacent.add(Point(col, row - 1))
    if (row != this.getRowNum() - 1) adjacent.add(Point(col, row + 1))
    return adjacent
}

fun <T> Matrix<T>.getAdjacentCoordinates(point: Point): List<Point> = getAdjacentCoordinates(point.y, point.x)
fun <T> Matrix<T>.getRangesToEdge(point: Point) = getRangesToEdge(point.y, point.x)
fun <T> Matrix<T>.getRangesToEdge(row: Int, col: Int) = getColumnToEdge(row, col) + getRowToEdge(row, col)
fun <T> Matrix<T>.getColumnToEdge(row: Int, col: Int): List<List<T>> =
    this.getColumn(col).let { listOf(it.subList(0, row), it.subList(row + 1, it.size)) }

fun <T> Matrix<T>.getRowToEdge(row: Int, col: Int): List<List<T>> =
    this[row].let { listOf(it.subList(0, col), it.subList(col + 1, it.size)) }


fun <T> Matrix<T>.getSurroundingCoordinates(row: Int, col: Int): List<Point> {
    val adjacent = getAdjacentCoordinates(row, col).toMutableList()
    if (col != 0 && row != 0) adjacent.add(Point(col - 1, row - 1))
    if (col != 0 && row != this.getRowNum() - 1) adjacent.add(Point(col - 1, row + 1))
    if (col != this.getColNum() - 1 && row != 0) adjacent.add(Point(col + 1, row - 1))
    if (col != this.getColNum() - 1 && row != this.getRowNum() - 1) adjacent.add(Point(col + 1, row + 1))
    return adjacent
}

fun <T> Matrix<T>.getSurroundingCoordinates(point: Point): List<Point> =
    this.getSurroundingCoordinates(point.y, point.x)

/**
 * Returns a list containing only the non-null results of applying the given [transform] function to each element
 * and its index in the original collection.
 */
fun <T, R> Matrix<T>.mapMatrixIndexedNotNull(transform: (Point, T) -> R) = this.flatMapIndexed { y, row ->
    row.mapIndexedNotNull { x, it -> transform(Point(x, y), it) }
}

data class Point(var x: Int, var y: Int) {
    operator fun plus(other: Point) = Point(other.x + x, other.y + y)
    operator fun minus(other: Point) = Point(other.x - x, other.y - y)
    operator fun times(n: Int) = Point(x * n, y * n)

    fun mDist(other: Point): Int = abs(this.x - other.x) + abs(this.y - other.y)

    infix fun lineTo(other: Point): List<Point> {
        val line = mutableListOf<Point>()
        val xR = (other.x - x).let { Pair(it.sign, 0..kotlin.math.abs(it)) }
        val yR = (other.y - y).let { Pair(it.sign, 0..kotlin.math.abs(it)) }
        for (x in xR.second) {
            for (y in yR.second) {
                line.add(this + Point(x * xR.first, y * yR.first))
            }
        }
        return line
    }

    companion object {
        val LEFT = Point(-1, 0)
        val RIGHT = Point(1, 0)
        val UP = Point(0, -1)
        val DOWN = Point(0, 1)
        val DOWN_RIGHT = DOWN + RIGHT
        val DOWN_LEFT = DOWN + LEFT
        val UP_LEFT = UP + LEFT
        val UP_RIGHT = UP + RIGHT

        val straightDirections = listOf(RIGHT, DOWN, LEFT, UP)
        val diagonalDirections = listOf(DOWN_RIGHT, DOWN_LEFT, UP_LEFT, UP_RIGHT)
        val directions = listOf(RIGHT, DOWN_RIGHT, DOWN, DOWN_LEFT, LEFT, UP_LEFT, UP, UP_RIGHT)
    }
}

fun Point.moveInDirection(direction: Char, step: Int = 1): Point = when (direction) {
    'N', 'U' -> this + (Point.UP * step)
    'S', 'D' -> this + (Point.DOWN * step)
    'W', 'L' -> this + (Point.LEFT * step)
    'E', 'R' -> this + (Point.RIGHT * step)
    else -> throw IllegalArgumentException("$direction is not a valid direction")
}

data class Point3(val x: Int, val y: Int, val z: Int) {
    operator fun plus(other: Point3) = Point3(other.x + x, other.y + y, other.z + z)
    operator fun minus(other: Point3) = Point3(other.x - x, other.y - y, other.z - z)
    operator fun times(n: Int) = Point3(x * n, y * n, z * n)

    fun neighbors(): List<Point3> = listOf(
        Point3(x + 1, y, z), Point3(x - 1, y, z),
        Point3(x, y + 1, z), Point3(x, y - 1, z),
        Point3(x, y, z + 1), Point3(x, y, z - 1),
    )
}

/*----- List Functions -----*/

fun <T> List<T>.pairwise() = mapIndexed { index, first -> drop(index + 1).map { second -> first to second } }.flatten()
fun <T, R> List<T>.pairwise(transform: (T, T) -> R) =
    mapIndexed { index, first -> drop(index + 1).map { second -> transform(first, second) } }.flatten()

fun <E> permutations(list: List<E>, length: Int? = null): Sequence<List<E>> = sequence {
    val n = list.size
    val r = length ?: list.size

    val indices = list.indices.toMutableList()
    val cycles = (n downTo (n - r)).toMutableList()
    yield(indices.take(r).map { list[it] })

    while (true) {
        var broke = false
        for (i in (r - 1) downTo 0) {
            cycles[i]--
            if (cycles[i] == 0) {
                val end = indices[i]
                for (j in i until indices.size - 1) {
                    indices[j] = indices[j + 1]
                }
                indices[indices.size - 1] = end
                cycles[i] = n - i
            } else {
                val j = cycles[i]
                val tmp = indices[i]
                indices[i] = indices[-j + indices.size]
                indices[-j + indices.size] = tmp
                yield(indices.take(r).map { list[it] })
                broke = true
                break
            }
        }
        if (!broke) {
            break
        }
    }
}
/*-----Helper Functions-----*/

private fun <T> transposeMatrix(matrix: Matrix<T>): Matrix<T> = List(matrix.getColNum()) { i -> matrix.getColumn(i) }
private fun <T> transposeMatrix(matrix: Matrix<T>, times: Int): Matrix<T> {
    var newMatrix = matrix
    repeat(times) {
        newMatrix = transposeMatrix(newMatrix)
    }
    return newMatrix
}


fun <T> getCol(array: List<List<T>>, col: Int): List<T> {
    val rows = array.size
    val column = mutableListOf<T>()
    (0 until rows).forEach {
        try {
            column.add(array[it][col])
        } catch (_: IndexOutOfBoundsException) {
        }
    }
    return column
}

/*-----Range Functions-----*/

infix fun IntRange.overlaps(other: IntRange): Boolean =
    first in other || last in other || other.first in this || other.last in this

infix fun IntRange.containsRange(other: IntRange): Boolean = other.first in this && other.last in this
infix fun IntRange.adjoint(other: IntRange): Boolean = this.last + 1 == other.first || other.last + 1 == this.first

/*-----String & Char Functions-----*/

fun String.allInts() = allIntsInString(this)
fun String.allLongs(): List<Long> = """\d+""".toRegex().findAll(this)
    .map { it.value.toLong() }
    .toList()

fun allIntsInString(line: String): List<Int> {
    return """-?\d+""".toRegex().findAll(line)
        .map { it.value.toInt() }
        .toList()
}

fun String.firstInt(): Int = """-?\d+""".toRegex().find(this)!!.value.toInt()

fun String.hexToBinaryString(): String {
    val num = this.uppercase(Locale.getDefault())
    var binary = ""
    for (hex in num) {
        when (hex) {
            '0' -> binary += "0000"
            '1' -> binary += "0001"
            '2' -> binary += "0010"
            '3' -> binary += "0011"
            '4' -> binary += "0100"
            '5' -> binary += "0101"
            '6' -> binary += "0110"
            '7' -> binary += "0111"
            '8' -> binary += "1000"
            '9' -> binary += "1001"
            'A' -> binary += "1010"
            'B' -> binary += "1011"
            'C' -> binary += "1100"
            'D' -> binary += "1101"
            'E' -> binary += "1110"
            'F' -> binary += "1111"
        }
    }
    return binary
}


/*-----Math Functions-----*/
fun leastCommonMultiple(a: Long, b: Long): Long {
    val larger = if (a > b) a else b
    val maxLcm = a * b
    var lcm = larger
    while (lcm <= maxLcm) {
        if (lcm % a == 0L && lcm % b == 0L) {
            return lcm
        }
        lcm += larger
    }
    return maxLcm
}