package aoc.y24.d25

import java.io.File

fun main() {
    val input = File("y24/y24d25.txt").readText().trim().split("\n\n").map { it.lines() }
    val result1 = input.sumOf { a ->
        input.count { b -> a.zip(b).none { (c, d) -> c.zip(d).any { (e, f) -> e == '#' && f == '#' } } }
    } / 2
    println("Result 1: $result1")
}
