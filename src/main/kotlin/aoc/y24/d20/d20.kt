package aoc.y24.d20

import java.io.File
import kotlin.math.abs

data class Pnt(val x: Int, val y: Int)
fun Pnt.neighbours() = listOf(Pnt(x + 1, y), Pnt(x - 1, y), Pnt(x, y + 1), Pnt(x, y - 1))

fun Map<Pnt, Char>.path(): List<Pnt> {
    val end = entries.first { it.value == 'E' }.key
    var next = entries.first { it.value == 'S' }.key
    val visited = LinkedHashSet(listOf(next))
    while (next != end) {
        next = next.neighbours().first { contains(it) && get(it) != '#' && it !in visited }
        visited.add(next)
    }
    return visited.toList()
}

fun List<Pnt>.solve(duration: Int, minSaving: Int) = (0..<lastIndex).sumOf { i ->
    (i + 1..<size).count { j ->
        val cost = abs(get(i).x - get(j).x) + abs(get(i).y - get(j).y)
        cost <= duration && (j - i) - cost >= minSaving
    }
}

fun main() {
    val input = File("y24/y24d20.txt").readText().trim().lines()
    val grid = input.mapIndexed { y, line -> line.mapIndexed { x, c -> Pnt(x, y) to c } }.flatten().toMap()

    val path = grid.path()

    val result1 = path.solve(2, 100)
    println("Result 1: $result1")

    val result2 = path.solve(20, 100)
    println("Result 2: $result2")
}
