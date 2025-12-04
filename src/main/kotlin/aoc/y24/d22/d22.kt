package aoc.y24.d22

import java.io.File
import kotlin.time.measureTimedValue

const val mask = (1 shl 24) - 1

fun next(prev: Int): Int {
    val prune1 = (prev xor (prev shl 6)) and mask
    val prune2 = prune1 xor (prune1 shr 5)
    return (prune2 xor (prune2 shl 11)) and mask
}

fun Int.secrets() = generateSequence(this) { next(it) }

fun part2(buyers: List<Int>): Int {
    val sales = buyers.map {
        it.secrets().take(2000).map { n -> n % 10 }.toList().asReversed().windowed(5)
            .associate { (a, b, c, d, e) -> listOf(d - e, c - d, b - c, a - b) to a }
    }.flatMap { it.entries }.groupBy({ it.key }, { it.value })
    return sales.values.maxOf { it.sum() }
}

fun main() {
    val input = File("y24/y24d22.txt").readText().trim().lines()
    val buyers = input.map { it.toInt() }

    val result1 = buyers.sumOf { it.secrets().drop(2000).first().toLong() }
    println("Result 1: $result1")

    val result2 = part2(buyers)
    println("Result 2: $result2")
}
