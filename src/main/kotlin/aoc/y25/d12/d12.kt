package aoc.y25.d12

import java.io.File

val NON_DIGITS = Regex("\\D+")

fun main() {
    val result1 = File("y25/y25d12.txt").readText().split("\n\n").last().split("\n")
        .map { s -> s.split(NON_DIGITS).map { it.toInt() } }
        .count { r -> r.take(2).reduce(Int::times) >= r.drop(2).sum() * 9 }
    println("Part1: $result1")
}
