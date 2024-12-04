package aoc.y22

import java.io.File
import java.util.LinkedList

data class Pos3D(val x: Int, val y: Int, val z: Int) {
    fun neighbours() = listOf(-1, 1).flatMap { listOf(copy(x = x + it), copy(y = y + it), copy(z = z + it)) }.toSet()

    companion object {
        fun parse(s: String) = s.split(',').map(String::toInt).let { (x, y, z) -> Pos3D(x, y, z) }
    }
}

data class Box3D(val xr: IntRange, val yr: IntRange, val zr: IntRange) {
    fun contains(p: Pos3D) = p.x in xr && p.y in yr && p.z in zr
}

fun <T> Iterable<T>.intRangeOf(f: (T) -> Int): IntRange = minOf(f)..maxOf(f)
fun IntRange.expandedBy(amount: Int) = (first - amount)..(last + amount)

fun main() {
    val lines = File("y22/y22d18.txt").readLines()
    val points = lines.map(Pos3D::parse).toSet()

    val result1 = points.sumOf { p -> 6 - p.neighbours().count { it in points } }
    println("Result 1: $result1")

    val box = Box3D(
        points.intRangeOf { it.x }.expandedBy(1),
        points.intRangeOf { it.y }.expandedBy(1),
        points.intRangeOf { it.z }.expandedBy(1)
    )
    val minPoint = Pos3D(box.xr.first, box.yr.first, box.zr.first)
    val queue = LinkedList<Pos3D>().apply { add(minPoint) }
    val seen = mutableSetOf<Pos3D>()
    var found = 0
    while (queue.isNotEmpty()) {
        val next = queue.removeFirst()
        if (next !in seen) {
            seen += next
            next.neighbours().filter(box::contains).forEach { if (it in points) found += 1 else queue.addLast(it) }
        }
    }
    val result2 = found
    println("Result 2: $result2")
}