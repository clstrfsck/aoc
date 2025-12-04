package aoc.y24.d08

import java.io.File

private data class P2(val y: Int, val x: Int)
private data class V2(val dy: Int, val dx: Int)
private operator fun P2.minus(v: V2) = P2(y - v.dy, x - v.dx)
private operator fun P2.minus(p: P2) = V2(y - p.y, x - p.x)

private fun List<P2>.pairs() = flatMapIndexed { i, a -> mapIndexedNotNull { j, b -> if (i != j) a to b else null } }
private fun List<String>.has(p: P2): Boolean = p.y in indices && p.x in (this[p.y].indices)
private fun List<String>.antinodes(p: P2, v: V2) = generateSequence(p) { it - v }.takeWhile { has(it) }

fun main() {
    val input = File("y24/y24d08.txt").readLines()

    val elements = input.flatMapIndexed { y, r -> r.mapIndexed { x, c -> c to P2(y, x) } }
    val map = elements.filter { it.first != '.' }.groupBy({ it.first }, { it.second })

    val result1 = map.values.flatMap { it.pairs().map { (p1, p2) -> p1 - (p2 - p1) } }.filter { input.has(it) }
    println("Result 1: ${result1.toSet().size}")

    val result2 = map.values.flatMap { it.pairs().flatMap { (p1, p2) -> input.antinodes(p1, p2 - p1) } }
    println("Result 2: ${result2.toSet().size}")
}
