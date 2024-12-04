package aoc.y23

import java.io.File
import kotlin.math.min

private fun List<String>.transpose() = (0 until get(0).length).map { col ->
    indices.map { row -> get(row)[col] }.joinToString("")
}

private fun List<String>.reflectionRow(diffs: Int) =
    (0 until (size - 1)).firstOrNull { reflectedDiffsAt(it) == diffs }?.plus(1) ?: 0

private fun List<String>.reflectedDiffsAt(index: Int): Int {
    val limit = min(index, size - index - 2)
    return slice(index - limit..index).reversed().zip(slice(index + 1..index + 1 + limit))
        .sumOf { (top, bot) -> top.zip(bot).count { (tc, bc) -> tc != bc } }
}

fun main() {
    val text = File("y23/y23d13.txt").readText().trim()
    val sections = text.split("\n\n").map { it.split("\n") }

    val result1 = sections.sumOf { it.reflectionRow(0) * 100 } + sections.sumOf { it.transpose().reflectionRow(0) }
    println("Result 1: $result1")

    val result2 = sections.sumOf { it.reflectionRow(1) * 100 } + sections.sumOf { it.transpose().reflectionRow(1) }
    println("Result 2: $result2")
}
