package aoc.y24.d19

import java.io.File

fun countDesigns(cache: MutableMap<String, Long>, design: String, towels: List<String>): Long {
    return if (design.isEmpty()) 1L else cache.getOrPut(design) {
        towels.sumOf { if (design.startsWith(it)) countDesigns(cache, design.substring(it.length), towels) else 0L }
    }
}

fun parts(towels: List<String>, designs: List<String>): Pair<Int, Long> {
    val cache = mutableMapOf<String, Long>()
    return designs.map { countDesigns(cache, it, towels) }.filter { it != 0L }.let { it.size to it.sum() }
}

fun main() {
    val input = File("y24/y24d19.txt").readText()
    val (towels, designs) = input.split("\n\n").let { (f, s) -> f.trim().split(", ") to s.trim().split("\n") }

    val (result1, result2) = parts(towels, designs)
    println("Result 1: $result1")
    println("Result 2: $result2")
}
