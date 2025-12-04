package aoc.y24.d11

import java.io.File

fun blink(stones: Map<Long, Long>) = stones.flatMap { (stone, count) ->
    if (stone == 0L) listOf(1L to count)
    else {
        val s = stone.toString()
        if (s.length % 2 == 0) s.chunked(s.length / 2).map { it.toLong() to count }
        else listOf(stone * 2024 to count)
    }
}.groupBy({ it.first }, { it.second }).mapValues { it.value.sum() }

fun main() {
    val numbers = File("y24/y24d11.txt").readText().split(" ").map { it.toLong() }
    val stones = numbers.groupingBy { it }.eachCount().mapValues { it.value.toLong() }

    val result1 = generateSequence(stones) { blink(it) }.drop(25).first().values.sum()
    println("Result 1: $result1")

    val result2 = generateSequence(stones) { blink(it) }.drop(75).first().values.sum()
    println("Result 2: $result2")
}
