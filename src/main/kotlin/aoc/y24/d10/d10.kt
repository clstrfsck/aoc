package aoc.y24.d10

import java.io.File

private data class YxVal(val y: Int, val x: Int)
private val directions = listOf(YxVal(-1, 0), YxVal(1, 0), YxVal(0, -1), YxVal(0, 1))

private operator fun List<List<Int>>.get(p: YxVal) = this[p.y][p.x]
private operator fun List<List<Int>>.contains(p: YxVal) = p.y in indices && p.x in get(p.y).indices

private fun List<List<Int>>.dfs(p: YxVal, visited: Set<YxVal> = emptySet()): List<YxVal> {
    if (get(p) == 9) return listOf(p)
    return directions.flatMap { d ->
        val n = YxVal(p.y + d.y, p.x + d.x)
        if (contains(n) && !visited.contains(n) && get(n) == get(p) + 1) dfs(n, visited + p) else emptyList()
    }
}

fun main() {
    val input = File("y24/y24d10.txt").readLines()
    val map = input.map { it.map(Char::digitToInt) }
    val trailheads = map.flatMapIndexed() { y, r -> r.mapIndexedNotNull { x, v -> if (v == 0) YxVal(y, x) else null  } }

    val result1 = trailheads.sumOf { map.dfs(it).toSet().size }
    println("Result 1: $result1")

    val result2 = trailheads.sumOf { map.dfs(it).size }
    println("Result 2: $result2")
}
