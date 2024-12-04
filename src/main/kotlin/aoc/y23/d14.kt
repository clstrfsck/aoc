package aoc.y23

import java.io.File

private fun List<String>.transpose() = (0 until get(0).length).map { col ->
    indices.map { row -> get(row)[col] }.joinToString("")
}

private fun List<String>.subst(from: String, to: String) = generateSequence(this) { current ->
    current.map { it.replace(from, to) }.let { if (it == current) null else it }
}.last()

private fun List<String>.rollWest() = subst(".O", "O.")
private fun List<String>.rollEast() = subst("O.", ".O")
private fun List<String>.rollNorth() = transpose().rollWest().transpose()
private fun List<String>.rollSouth() = transpose().rollEast().transpose()

fun main() {
    val lines = File("y23/y23d14.txt").readLines()

    val result1 = lines.rollNorth().mapIndexed { i, l -> (lines.size - i) * l.count { it == 'O'} }.sum()
    println("Result 1: $result1")

    val map = mutableMapOf<List<String>, Int>()
    var next = lines
    var count = 0
    while (next !in map.keys) {
        map[next] = count
        next = next.rollNorth().rollWest().rollSouth().rollEast()
        count += 1
    }
    val cycleStart = map[next]!!
    val index = cycleStart + (1_000_000_000 - cycleStart) % (count - cycleStart)
    val resultLines = map.entries.first { it.value == index }.key
    val result2 = resultLines.mapIndexed { i, l -> (lines.size - i) * l.count { it == 'O'} }.sum()
    println("Result 2: $result2")
}
