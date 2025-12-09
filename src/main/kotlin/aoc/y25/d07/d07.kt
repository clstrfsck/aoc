package aoc.y25.d07

import java.io.File

fun main() {
    val lines = File("y25/y25d07.txt").readLines()

    val counts = lines.first().let { l -> MutableList(l.length) { if (l[it] == 'S') 1L else 0L } }
    val result1 = lines.drop(1).sumOf { line ->
        val snapshot = counts.toList()
        snapshot.indices.count { x ->
            (line[x] == '^' && snapshot[x] != 0L).takeIf { it }?.also {
                counts[x] = 0
                if (x > 0) counts[x - 1] += snapshot[x]
                if (x < counts.lastIndex) counts[x + 1] += snapshot[x]
            } ?: false
        }
    }

    println("Result1: $result1")
    println("Result2: ${counts.sum()}")
}
