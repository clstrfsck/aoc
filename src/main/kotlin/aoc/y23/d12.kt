package aoc.y23

import java.io.File

private val cache = hashMapOf<Pair<String, List<Int>>, Long>()

private fun String.toNumbers() = split(",").map { it.toInt() }
private fun String.dup(n: Int, s: String) = List(n) { this }.joinToString(s)
private fun Boolean.toNumber() = if (this) 1L else 0L
private val Char.unknown get() = this == '?'
private val Char.operational get() = this == '.'
private val Char.damaged get() = this == '#'
private val Char.possiblyOperational get() = operational || unknown
private val Char.possiblyDamaged get() = damaged || unknown
private fun Boolean.maybe(compute: () -> Long) = if (this) compute() else 0L
private fun String.matchesGroup(groupSize: Int) = when {
    groupSize < length -> take(groupSize).none { it.operational } && !this[groupSize].damaged
    groupSize == length -> take(groupSize).none { it.operational }
    else -> false
}

private fun count(springs: String, groups: List<Int>): Long {
    if (groups.isEmpty()) return springs.none { it.damaged }.toNumber()
    if (springs.isEmpty()) return 0
    return cache.getOrPut(springs to groups) {
        springs.first().let { sf ->
            val notGrouped = (sf.possiblyOperational).maybe { count(springs.drop(1), groups) }
            val grouped = (sf.possiblyDamaged).maybe {
                groups.first().let { gf ->
                    springs.matchesGroup(gf).maybe { count(springs.drop(gf + 1), groups.drop(1)) }
                }
            }
            grouped + notGrouped
        }
    }
}

fun main() {
    val lines = File("y23/y23d12.txt").readLines()

    val result1 = lines.sumOf { it.split(" ").let { (springs, groups) -> count(springs, groups.toNumbers()) } }
    println("Result 1: $result1")

    val result2 = lines.sumOf {
        it.split(" ").let { (springs, groups) -> count(springs.dup(5, "?"), groups.dup(5, ",").toNumbers()) }
    }
    println("Result 2: $result2")
}
