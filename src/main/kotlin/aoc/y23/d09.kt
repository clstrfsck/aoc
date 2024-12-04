package aoc.y23

import java.io.File

private val WHITESPACE = "\\s+".toRegex()
private fun String.toNumbers() = split(WHITESPACE).map { it.toLong() }

private fun extrapolate(elts: List<Long>, extractor: (List<Long>) -> Long, operator: (Long, Long) -> Long): Long {
    elts.distinct().let { if (it.size == 1) return it.single() }
    return operator(extractor(elts), extrapolate(elts.zipWithNext { a, b -> b - a }, extractor, operator))
}

fun main() {
    val lines = File("y23/y23d09.txt").readLines()
    val lists = lines.map { it.toNumbers() }

    val result1 = lists.sumOf { extrapolate(it, { l -> l.last() }, Long::plus) }
    println("Result 1: $result1")

    val result2 = lists.sumOf { extrapolate(it, { l -> l.first() }, Long::minus) }
    println("Result 2: $result2")
}
