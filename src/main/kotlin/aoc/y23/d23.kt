package aoc.y23

import java.io.File

private class Day23(val lines: List<String>) {
    data class Point(val y: Int, val x: Int) {
        operator fun plus(v: Vector) = Point(y + v.dy, x + v.dx)
        fun neighbours() = directions.map { this + it }
    }
    data class Vector(val dy: Int, val dx: Int)

    private val yIndices = lines.indices
    private val xIndices = lines.first().indices
    private val start = Point(0, lines.first().indexOf('.'))
    private val end = Point(lines.lastIndex, lines.last().indexOf('.'))

    fun solvePart1() = dfs(start, end, setOf(), 0)

    private fun dfs(p: Point, end: Point, visited: Set<Point>, steps: Int): Int = if (p == end) steps
        else validNeighbours(p).filter { it !in visited }.maxOfOrNull { dfs(it, end, visited + p, steps + 1) } ?: 0

    private fun validNeighbours(p: Point) = when (lines[p.y][p.x]) {
        '^' -> listOf(p + Vector(-1, 0))
        '>' -> listOf(p + Vector(0, 1))
        'v' -> listOf(p + Vector(1, 0))
        '<' -> listOf(p + Vector(0, -1))
        else -> p.neighbours().filter { isValid(it) }
    }

    private fun isValid(p: Point) = p.y in yIndices && p.x in xIndices && lines[p.y][p.x] != '#'

    fun solvePart2() = dfso(adjacencies(), start, end, mutableMapOf())

    private fun adjacencies(): Map<Point, Map<Point, Int>> {
        val adjacencies = yIndices.flatMap { y ->
            xIndices.mapNotNull { x ->
                val p = Point(y, x)
                if (isValid(p)) {
                    p to directions.mapNotNull { v ->
                        val n = p + v
                        if (isValid(n)) n to 1 else null
                    }.toMap(mutableMapOf())
                } else null
            }
        }.toMap(mutableMapOf())

        adjacencies.keys.toList().forEach { key ->
            adjacencies[key]?.takeIf { it.size == 2 }?.let { neighbors ->
                val left = neighbors.keys.first()
                val right = neighbors.keys.last()
                val totalSteps = neighbors[left]!! + neighbors[right]!!
                adjacencies.getOrPut(left) { mutableMapOf() }.merge(right, totalSteps, ::maxOf)
                adjacencies.getOrPut(right) { mutableMapOf() }.merge(left, totalSteps, ::maxOf)
                listOf(left, right).forEach { adjacencies[it]?.remove(key) }
                adjacencies.remove(key)
            }
        }
        return adjacencies
    }

    private fun dfso(graph: Map<Point, Map<Point, Int>>, p: Point, end: Point, seen: Map<Point, Int>): Int {
        if (p == end) return seen.values.sum()
        return graph[p]?.filter { it.key !in seen }?.map { dfso(graph, it.key, end, seen + it.toPair()) }
            ?.maxOrNull() ?: 0
    }

    companion object {
        val directions = listOf(Vector(-1, 0), Vector(1, 0), Vector(0, -1), Vector(0, 1))
    }
}

fun main() {
    val lines = File("y23/y23d23.txt").readLines()

    val setup = Day23(lines)

    val result1 = setup.solvePart1()
    println("Result 1: $result1")

    val result2 = setup.solvePart2()
    println("Result 2: $result2")
}
