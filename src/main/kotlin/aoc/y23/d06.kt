package aoc.y23

import java.io.File
import kotlin.math.ceil
import kotlin.math.sqrt

private val WHITESPACE = "\\s+".toRegex()

private fun Double.isInteger() = toLong().toDouble() == this // ewww

private fun solvedSize(time: Long, distance: Long): Long {
    val sb2m4ac = sqrt(time * time - 4.0 * distance)
    val minx = (time - sb2m4ac) / 2.0
    return ceil((time + sb2m4ac) / 2.0).toLong() - if (minx.isInteger()) minx.toLong() + 1 else ceil(minx).toLong()
}

fun main() {
    val lines = File("y23/y23d06.txt").readLines()

    val (times, distances) = lines.map { line -> line.split(WHITESPACE).drop(1).map { it.toLong() } }
    val result1 = times.indices.map { solvedSize(times[it], distances[it]) }.fold(1L) { acc, v -> acc * v }
    println("Result 1: $result1")

    val (time, distance) = lines.map { line -> line.filter { it.isDigit() }.toLong() }
    val result2 = solvedSize(time, distance)
    println("Result 1: $result2")
}
