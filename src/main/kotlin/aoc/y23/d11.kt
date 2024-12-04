package aoc.y23

import java.io.File
import kotlin.math.abs

private data class GalaxyPoint(val y: Long, val x: Long) {
    fun manhattanDistanceTo(other: GalaxyPoint) = abs(y - other.y) + abs(x - other.x)
    fun manhattanDistancesTo(others: List<GalaxyPoint>) = others.map { manhattanDistanceTo(it) }
}

private class Galaxy(map: List<String>, expand: Long) {
    val locations: List<GalaxyPoint>

    init {
        val ye = expansions(map)
        val xe = expansions(map.transpose())
        locations = map.flatMapIndexed { y, s ->
            s.mapIndexedNotNull { x, c ->  if (c == '#') GalaxyPoint(y + ye[y] * expand, x + xe[x] * expand) else null }
        }
    }

    fun computeDistances() = locations.windowed(locations.size, 1, true).takeWhile { it.size > 1 }
        .flatMap { it[0].manhattanDistancesTo(it.drop(1)) }

    companion object {
        private fun expansions(map: List<String>) = map.runningFold(0L) { n, r -> if (r.empty()) n + 1 else n }
        private fun String.empty() = all { it == '.' }
        private fun List<String>.transpose() = (0 until get(0).length).map { col ->
            indices.map { row -> get(row)[col] }.joinToString("")
        }
    }
}

fun main() {
    val lines = File("y23/y23d11.txt").readLines()

    val result1 = Galaxy(lines, 2 - 1).computeDistances().sum()
    println("Result 1: $result1")

    val result2 = Galaxy(lines, 1_000_000 - 1).computeDistances().sum()
    println("Result 2: $result2")
}
