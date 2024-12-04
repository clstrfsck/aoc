package aoc.y23

import java.io.File
import java.util.PriorityQueue

private data class D17Vector(val dy: Int, val dx: Int) {
    val left by lazy { D17Vector(-dx, dy) }
    val right by lazy { D17Vector(dx, -dy) }
}

private data class D17Point(val y: Int, val x: Int) {
    operator fun plus(d: D17Vector) = D17Point(y + d.dy, x + d.dx)
}

private data class D17Ray(val p: D17Point, val v: D17Vector, val n: Int) {
    fun neighbours1() = buildList {
        if (n < 3) add(D17Ray(p + v, v, n + 1))
        add(D17Ray(p + v.left, v.left, 1))
        add(D17Ray(p + v.right, v.right, 1))
    }
    fun neighbours2() = buildList {
        if (n < 10) add(D17Ray(p + v, v, n + 1))
        if (n >= 4 || n == 0) {
            add(D17Ray(p + v.left, v.left, 1))
            add(D17Ray(p + v.right, v.right, 1))
        }
    }
}

private data class Vertex(val r: D17Ray, val score: Int): Comparable<Vertex> {
    override fun compareTo(other: Vertex) = score.compareTo(other.score)
}

private fun shortestPath(
    start: D17Ray,
    isEnd: (D17Ray) -> Boolean,
    neighbours: (D17Ray) -> Iterable<D17Ray>,
    cost: (D17Ray) -> Int
): Int? {
    val vertices = PriorityQueue(listOf(Vertex(start, 0)))
    var end: D17Ray? = null
    val seen = mutableMapOf(start to 0)

    while (end == null) {
        if (vertices.isEmpty()) return null
        val (curr, score) = vertices.remove()
        if (isEnd(curr)) end = curr
        val next = neighbours(curr).filter { it !in seen }.map { Vertex(it, score + cost(it)) }
        vertices.addAll(next)
        seen.putAll(next.associate { it.r to it.score })
    }
    return seen[end]
}

fun main() {
    val lines = File("y23/y23d17.txt").readLines()
    val map = lines.map { line -> line.map { it.digitToInt() } }

    val start = D17Ray(D17Point(0, 0), D17Vector(0, 1), 0)
    val end = D17Point(map.lastIndex, map[0].lastIndex)
    val result1 = shortestPath(
        start,
        { it.p == end },
        { r -> r.neighbours1().filter { it.p.y in map.indices && it.p.x in map[0].indices } },
        { map[it.p.y][it.p.x] }
    )
    println("Result 1: $result1")

    val result2 = shortestPath(
        start,
        { it.p == end && it.n >= 4 },
        { r -> r.neighbours2().filter { it.p.y in map.indices && it.p.x in map[0].indices } },
        { map[it.p.y][it.p.x] }
    )
    println("Result 2: $result2")
}
