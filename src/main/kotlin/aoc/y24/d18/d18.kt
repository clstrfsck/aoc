package aoc.y24.d18

import java.io.File

data class Pnt(val x: Int, val y: Int)
fun Pnt.neighbors() = listOf(Pnt(x + 1, y), Pnt(x - 1, y), Pnt(x, y + 1), Pnt(x, y - 1))

fun part1(points: List<Pnt>) = solve(points.toSet())

fun part2(points: List<Pnt>): String {
    var count = points.size
    while (count > 0 && solve(points.take(count).toSet()) == null) count -= 1
    return "${points[count].x},${points[count].y}"
}

private fun solve(points: Set<Pnt>, maxx: Int = 70, maxy: Int = 70): Int? {
    val queue = ArrayDeque<Pair<Pnt, Int>>().also { it.add(Pnt(0, 0) to 0) }
    val seen = mutableSetOf<Pnt>()
    while (queue.isNotEmpty()) {
        val (p, c) = queue.removeFirst()
        if (p.x == maxx && p.y == maxy) return c
        if (seen.add(p)) {
            queue.addAll(
                p.neighbors().filter { it.x in 0..maxx && it.y in 0..maxy && it !in points }.map { it to c + 1 }
            )
        }
    }
    return null
}

fun main() {
    val points = File("y24/y24d18.txt").readLines().map { it.split(",").let { (x, y) -> Pnt(x.toInt(), y.toInt()) } }

    val result1 = part1(points.take(1024))
    println("Result 1: $result1")

    val result2 = part2(points)
    println("Result 2: $result2")
}
