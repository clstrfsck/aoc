package aoc.y25.d09

import java.io.File
import kotlin.math.max
import kotlin.math.min

data class P2d(val x: Int, val y: Int): Comparable<P2d> {
    override fun compareTo(other: P2d) = x.compareTo(other.x).let { if (it == 0) y.compareTo(other.y) else it }
    fun area(p2: P2d) = (x - p2.x + 1).toLong() * (y - p2.y + 1).toLong()
}

fun main() {
    val lines = File("y25/y25d09.txt").readLines()
    val points = lines.map { l -> l.split(",").let { (x, y) -> P2d(x.toInt(), y.toInt()) } }
    val pairs = points.flatMap { p1 -> points.map { p2 -> p1 to p2 } }

    val result1 = pairs.maxOf { (p1, p2) -> p1.area(p2) }
    println("Result1: $result1")

    fun isValid(tl: P2d, br: P2d) = points.indices.none { i ->
        val (l1, l2) = points[i] to points[(i + 1) % points.size]
        tl.x < max(l1.x, l2.x) && min(l1.x, l2.x) < br.x && tl.y < max(l1.y, l2.y) && min(l1.y, l2.y) < br.y
    }

    val result2 = pairs.filter { (p1, p2) -> p1 <= p2 }
        .mapNotNull { (tl, br) -> if (isValid(tl, br)) br.area(tl) else null }.maxOrNull() ?: 0
    println("Result2: $result2")
}
