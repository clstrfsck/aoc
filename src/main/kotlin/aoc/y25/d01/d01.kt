package aoc.y25.d01

import java.io.File
import kotlin.math.abs

fun positions(start: Int, moves: List<String>) = moves.runningFold(start to start) { (_, c), move ->
    c.mod(100).let { it to it + (if (move[0] == 'L') -1 else 1) * move.substring(1).toInt() }
}

fun main() {
    val lines = File("y25/y25d01.txt").readLines()

    val result1 = positions(50, lines).count { (_, c) -> c.mod(100) == 0 }
    println("Result1: $result1")

    val result2 = positions(50, lines).sumOf { (p, c) -> abs(c) / 100 + if (p != 0 && c <= 0) 1 else 0 }
    println("Result2: $result2")
}
