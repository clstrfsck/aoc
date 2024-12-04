package aoc.y22

import java.io.File

fun List<List<Int>>.treeLines(y: Int, x: Int) = sequenceOf(
    sequenceOf((y - 1 downTo 0), (y + 1 until size)).map { it.map { ny -> ny to x } },
    sequenceOf((x - 1 downTo 0), (x + 1 until get(0).size)).map { it.map { nx -> y to nx } }
).flatten().map { it.map { (y, x) -> get(y)[x] } }
fun List<Int>.countVisible(h: Int) = indexOfFirst { it >= h }.let { if (it < 0) size else it + 1 }

fun main() {
    val lines = File("y22/y22d08.txt").readLines()
    val map = lines.map { it.map(Char::digitToInt) }
    val mapHeights = map.flatMapIndexed { y, row -> row.mapIndexed { x, h -> Triple(y, x, h) } }

    val result1 = mapHeights.count { (y, x, h) -> map.treeLines(y, x).any { tl -> tl.all { it < h } } }
    println("Result 1: $result1")

    val result2 = mapHeights.maxOf { (y, x, h) -> map.treeLines(y, x).map { it.countVisible(h) }.reduce(Int::times) }
    println("Result 2: $result2")
}