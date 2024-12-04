package aoc.y23

import java.io.File

private fun String.parseGame() = split(": ")[1].trim().split("; ").map { handful ->
    handful.split(", ").associate { part -> part.split(" ").let { Pair(it[1], it[0].toInt()) }  }
}

private fun Map<String, Int>.within(limits: Map<String, Int>) = limits.keys.all {
   this.getOrDefault(it, 0) <= limits.getOrDefault(it, Int.MIN_VALUE)
}

private fun List<Map<String, Int>>.within(limits: Map<String, Int>) = all { it.within(limits) }

private fun List<Map<String, Int>>.maxValuesFor(keys: Set<String>): List<Int> {
    return keys.map { k -> maxOf { it.getOrDefault(k, 0) } }
}

private fun Iterable<Int>.product() = reduce { acc, v -> acc * v }

fun main() {
    val lines = File("y23/y23d02.txt").readLines()
    val games = lines.map(String::parseGame)

    val limits = mapOf("red" to 12, "green" to 13, "blue" to 14)
    val result1 = games.zip(games.indices).filter { it.first.within(limits) }.sumOf { it.second + 1 }
    println("Result 1: $result1")

    val result2 = games.sumOf { it.maxValuesFor(limits.keys).product() }
    println("Result 1: $result2")
}
