package aoc.y24.d25

import java.io.File

fun main() {
    val (ls, ks) = File("y24/y24d25.txt").readText().trim().split("\n\n").map { it.lines() }
        .map { ('#' == it[0][0]) to it[0].indices.map { c -> it.indices.count { r -> it[r][c] == '#' } - 1 } }
        .partition { it.first }
    val result1 = ls.sumOf { (_, l) -> ks.count { (_, k) -> l.indices.all { l[it] + k[it] <= 5 } } }
    println("Result 1: $result1")
}
