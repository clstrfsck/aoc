package aoc.y23

import java.io.File

private data class D21Vector(val dy: Int, val dx: Int)

private val north = D21Vector(-1, 0)
private val east = D21Vector(0, 1)
private val south = D21Vector(1, 0)
private val west = D21Vector(0, -1)

private data class D21Point(val y: Int, val x: Int) {
    operator fun plus(d: D21Vector) = D21Point(y + d.dy, x + d.dx)
    fun neighbours() = listOf(this + north, this + east, this + south, this + west)
}

private data class D21Map(val data: List<String>) {
    val ySize = data.size
    val xSize = data[0].length // Assume all the same
    fun start() = (0 until ySize).flatMap { y ->
        (0 until xSize).mapNotNull { if (data[y][it] == 'S') D21Point(y, it) else null }
    }.single()
    fun fringes() = generateSequence(setOf(start())) { newFringe(it) }

    private fun wrap(p: D21Point) = D21Point(p.y.mod(ySize), p.x.mod(xSize))
    private fun isGarden(p: D21Point) = wrap(p).let { data[it.y][it.x] != '#' }
    private fun newFringe(fringe: Set<D21Point>) = fringe.flatMap { it.neighbours() }.filter { isGarden(it) }.toSet()
}

fun main() {
    val lines = File("y23/y23d21.txt").readLines()

    val map = D21Map(lines)
    val result1 = map.fringes().elementAt(64).size
    println("Result 1: $result1")

    assert(map.ySize == map.xSize)
    assert(map.start().let { it.x == it.y })
    assert(map.start().x * 2 + 1 == map.xSize)
    val startY = map.start().y
    val steps = map.fringes().map { it.size }.take(2 * map.ySize + startY + 1).toList()
    val p = (0..2).map { steps[startY + it * map.ySize].toDouble() to startY + it * map.xSize }
    val y01 = (p[1].first - p[0].first) / (p[1].second - p[0].second)
    val y12 = (p[2].first - p[1].first) / (p[2].second - p[1].second)
    val y012 = (y12 - y01) / (p[2].second - p[0].second)
    val n = 26501365
    val result2 = (p[0].first + y01 * (n - p[0].second) + y012 * (n - p[0].second) * (n - p[1].second)).toLong()
    println("Result 2: $result2")
}
