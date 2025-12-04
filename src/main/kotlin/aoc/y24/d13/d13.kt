package aoc.y24.d13

import java.io.File

const val offset = 10000000000000L

fun parse(s: String) = s.filter { it.isDigit() || it == ',' || it == '\n' }.split(Regex("[\n,]")).map { it.toLong() }
operator fun List<Long>.component6() = this[5]

fun solve(machine: List<Long>): Long {
    val (ax, ay, bx, by, px, py) = machine
    val b = (px * ay - py * ax) / (ay * bx - by * ax)
    val a = (px * by - py * bx) / (by * ax - ay * bx)
    return if (ax * a + bx * b == px && ay * a + by * b == py) 3 * a + b else 0
}

fun main() {
    val ms = File("y24/y24d13.txt").readText().split("\n\n").map { parse(it) }

    val result1 = ms.sumOf { solve(it) }
    println("Result 1: $result1")

    val result2 = ms.map { listOf(it[0], it[1], it[2], it[3], it[4] + offset, it[5] + offset) }.sumOf { solve(it) }
    println("Result 2: $result2")
}
