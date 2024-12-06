package aoc.y24

import java.io.File

private fun List<Int>.isDeltaInRange() = all { it in 1..3 } || all { -it in 1..3 }
private fun List<Int>.isSafe() = zipWithNext().map { (a, b) -> b - a }.isDeltaInRange()
private fun List<Int>.isMostlySafe() = isSafe() || indices.any { (take(it) + drop(it + 1)).isSafe() }

fun main() {
    val lines = File("y24/y24d02.txt").readLines()
    val items = lines.map { it.split(" ").map(String::toInt) }

    val result1 = items.count { it.isSafe() }
    println("Result 1: $result1")

    val result2 = items.count { it.isMostlySafe() }
    println("Result 2: $result2")
}
