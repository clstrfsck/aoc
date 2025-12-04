package aoc.y24.d04

import java.io.File

private data class V2D(val dy: Int, val dx: Int)
private operator fun V2D.times(scalar: Int) = V2D(dy * scalar, dx * scalar)
private data class P2D(val y: Int, val x: Int)
private operator fun P2D.plus(vector: V2D) = P2D(y + vector.dy, x + vector.dx)

private fun Map<P2D, Char>.extractor(origin: P2D, directions: Iterable<V2D>, range: IntRange) =
    directions.map { d -> range.mapNotNull { get(origin + d * it) }.joinToString("") }

private val dirs = listOf(V2D(1, 0), V2D(-1, 0), V2D(0, 1), V2D(0, -1), V2D(1, 1), V2D(-1, -1), V2D(1, -1), V2D(-1, 1))

private fun Map<P2D, Char>.wordsPart1(p: P2D) = extractor(p, dirs, 0..3)
private fun Map<P2D, Char>.wordsPart2(p: P2D) = extractor(p, listOf(V2D(1, 1), V2D(1, -1)), -1..1)
private fun List<String>.isCrossMas() = all { it == "MAS" || it == "SAM" }

fun main() {
    val lines = File("y24/y24d04.txt").readLines()
    val grid = lines.indices.flatMap { y -> lines[y].indices.map { x -> P2D(y, x) to lines[y][x] } }.toMap()

    val result1 = grid.filterValues { it == 'X' }.keys.sumOf { grid.wordsPart1(it).count { it == "XMAS" } }
    println("Result 1: $result1")

    val result2 = grid.filterValues { it == 'A' }.keys.map { grid.wordsPart2(it) }.count { it.isCrossMas() }
    println("Result 2: $result2")
}
