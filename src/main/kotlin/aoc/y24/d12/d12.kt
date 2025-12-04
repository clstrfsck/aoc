package aoc.y24.d12

import java.io.File

data class P2D(val y: Int, val x: Int)
data class V2D(val dy: Int, val dx: Int)
operator fun P2D.plus(v: V2D) = P2D(y + v.dy, x + v.dx)
operator fun V2D.times(s: Int) = V2D(dy * s, dx * s)
fun V2D.right() = V2D(dx, -dy)

fun main() {
    val lines = File("y24/y24d12.txt").readLines()

    val m = lines.indices.flatMap { y ->
        lines[y].indices.map { x ->
            P2D(y, x) to lines[y][x]
        }
    }.toMap().withDefault { null }

    val notSeen = m.keys.toMutableSet()

    var result1 = 0
    var result2 = 0
    while (notSeen.isNotEmpty()) {
        val start = notSeen.first().also { notSeen.remove(it) }
        val ch = m[start]
        val stack = ArrayDeque<P2D>().also { it.add(start) }
        var area = 0
        var perimeter = 0
        val sides = mutableSetOf<Pair<P2D, V2D>>()
        while (stack.isNotEmpty()) {
            val node = stack.removeFirst()
            area += 1
            perimeter += 4
            for (d in listOf(V2D(1, 0), V2D(-1, 0), V2D(0, 1), V2D(0, -1))) {
                val newNode = node + d
                if (m[newNode] != ch) sides.add(node to d)
                else {
                    perimeter -= 1
                    if (notSeen.remove(newNode)) stack.addFirst(newNode)
                }
            }
        }

        val sideCount = sides.count { (p, d) -> (p + d.right()) to d !in sides }
        result1 += area * perimeter
        result2 += area * sideCount
    }

    println("Result 1: $result1")
    println("Result 2: $result2")
}
