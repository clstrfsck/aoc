package aoc.y25.d04

import java.io.File

data class P2d(val x: Int, val y: Int)

fun P2d.neighbourhood() = (-1..1).flatMap { dy -> (-1..1).map { dx -> P2d(x + dx, y + dy) } }

fun main() {
    val points = File("y25/y25d04.txt").readLines().flatMapIndexed { y, line ->
        line.mapIndexedNotNull { x, c -> if (c == '@') P2d(x, y) else null }
    }.toSet()

    val result1 = points.count { p -> p.neighbourhood().count { it in points } < 5 }
    println("Result1: $result1")

    val result2 = generateSequence(0 to points) { (total, points) ->
        val toRemove = points.filter { p -> p.neighbourhood().count { it in points } < 5 }.toSet()
        if (toRemove.isEmpty()) null else (total + toRemove.size) to (points - toRemove)
    }.lastOrNull()?.first
    println("Result2: $result2")
}
