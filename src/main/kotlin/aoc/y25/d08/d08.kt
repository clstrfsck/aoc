package aoc.y25.d08

import java.io.File

data class P3d(val x: Long, val y: Long, val z: Long)
fun P3d.distSq(p2: P3d) = (x - p2.x) * (x - p2.x) + (y - p2.y) * (y - p2.y) + (z - p2.z) * (z - p2.z)
data class Edge(val u: Int, val v: Int)

class UnionFind(n: Int) {
    private val parent = IntArray(n) { it }
    val sizes = IntArray(n) { 1 }
    var count = n

    fun find(i: Int): Int = if (parent[i] == i) i else find(parent[i]).also { parent[i] = it }

    fun union(i: Int, j: Int): Boolean {
        val rootI = find(i)
        val rootJ = find(j)
        if (rootI == rootJ) return false
        if (sizes[rootI] < sizes[rootJ]) {
            parent[rootI] = rootJ
            sizes[rootJ] += sizes[rootI]
        } else {
            parent[rootJ] = rootI
            sizes[rootI] += sizes[rootJ]
        }
        count -= 1
        return true
    }
}

fun main() {
    val lines = File("y25/y25d08.txt").readLines()
    val points = lines.map { l -> l.trim().split(",").map { it.toLong() }.let { P3d(it[0], it[1], it[2]) } }
    val sortedEdges = points.indices.flatMap { i ->
        (i + 1 until points.size).map { j ->
            Edge(i, j)
        }
    }.sortedBy { points[it.u].distSq(points[it.v]) }

    val result1 = UnionFind(points.size).run {
        sortedEdges.take(1000).forEach { union(it.u, it.v) }
        points.indices.groupBy { find(it) }.values.map { it.size }.sortedDescending().take(3).reduce(Int::times)
    }
    println("Result1: $result1")

    val result2 = UnionFind(points.size).run {
        sortedEdges.first { union(it.u, it.v) && count == 1 }.let { points[it.u].x * points[it.v].x }
    }
    println("Result2: $result2")
}
