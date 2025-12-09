package aoc.y25.d05

import kotlin.io.path.Path
import kotlin.io.path.readText
import kotlin.math.max

fun main() {
    val (rangesText, ingredientsText) = Path("y25/y25d05.txt").readText().split("\n\n").map { it.split("\n") }
    val ranges = rangesText.map { line -> line.split("-").let { it[0].toLong()..it[1].toLong() }  }
    val ingredients = ingredientsText.map { it.toLong() }

    val result1 = ingredients.count { ing -> ranges.any { ing in it } }
    println("Result1: $result1")

    val mergedRanges = ranges
        .sortedBy { it.first }
        .fold(emptyList<LongRange>()) { acc, range ->
            val last = acc.lastOrNull()
            if (last == null || last.last < range.first) {
                acc + listOf(range)
            } else {
                acc.dropLast(1) + listOf(last.first..max(last.last, range.last))
            }
        }
    val result2 = mergedRanges.sumOf { it.last - it.first + 1 }
    println("Result2: $result2")
}
