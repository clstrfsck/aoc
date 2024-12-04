package aoc.y23

import java.io.File

private data class D18Vector(val dy: Long, val dx: Long) {
    operator fun times(scalar: Long) = D18Vector(dy * scalar, dx * scalar)
    companion object {
        val up = D18Vector(-1L, 0L)
        val down = D18Vector(1L, 0L)
        val left = D18Vector(0L, -1L)
        val right = D18Vector(0L, 1L)
    }
}

private data class D18Point(val y: Long, val x: Long) {
    operator fun plus(v: D18Vector) = D18Point(y + v.dy, x + v.dx)
    companion object {
        val origin = D18Point(0L, 0L)
    }
}

private fun Char.toDirection1() = when (this) {
    'U' -> D18Vector.up
    'D' -> D18Vector.down
    'L' -> D18Vector.left
    'R' -> D18Vector.right
    else -> throw IllegalStateException("Unknown direction1: $this")
}

private fun Char.toDirection2() = when (this) {
    '3' -> D18Vector.up
    '1' -> D18Vector.down
    '2' -> D18Vector.left
    '0' -> D18Vector.right
    else -> throw IllegalStateException("Unknown direction2: $this")
}

private fun List<D18Point>.shoelace() = (1..size).sumOf { i ->
    this[i % size].x * (this[(i + 1) % size].y - this[(i - 1) % size].y)
}

private fun polygonSize(directions: List<Pair<D18Vector, Long>>): Long {
    val count = directions.sumOf { it.second }
    val corners = directions.runningFold(D18Point.origin) { p, (v, n) ->
        p + (v * n)
    }
    return (corners.shoelace() + count) / 2 + 1
}

private fun String.parseInstruction(): Pair<D18Vector, Long> = get(7).toDirection2() to substring(2, 7).toLong(16)

fun main() {
    val lines = File("y23/y23d18.txt").readLines()

    val directions1 = lines.map { it.split(" ").let { (s, l) -> s[0].toDirection1() to l.toLong() } }
    val result1 = polygonSize(directions1)
    println("Result 1: $result1")

    val directions2 = lines.map { it.split(" ").let { (_, _, hex) -> hex.parseInstruction() } }
    val result2 = polygonSize(directions2)
    println("Result 2: $result2")
}
