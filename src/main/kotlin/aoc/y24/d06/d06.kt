package aoc.y24.d06

import java.io.File

private data class Point(val y: Int, val x: Int)
private data class Direction(val dy: Int, val dx: Int)
private operator fun Point.plus(d: Direction) = Point(y + d.dy, x + d.dx)
private fun Direction.right() = Direction(dx, -dy)
private operator fun List<String>.contains(p: Point): Boolean = p.y in indices && p.x in get(p.y).indices
private operator fun List<String>.get(p: Point) = if (contains(p)) this[p.y][p.x] else null
private data class PathState(val p: Point, val d: Direction, val visited: MutableSet<Pair<Point, Direction>>)
private data class PathState2(val p: Point, val d: Direction, val visited: MutableList<Point>)

private fun List<String>.getPath(start: Point) =
    generateSequence(PathState2(start, Direction(-1, 0), mutableListOf())) { (p, d, visited) ->
        if (contains(p)) {
            visited.add(p)
            val blocked = this[p + d] == '#'
            PathState2(if (blocked) p else p + d, if (blocked) d.right() else d, visited)
        } else null
    }.last().visited.toSet()

private fun List<String>.isValid(o: Point, p: Point, d: Direction): Boolean {
    var pp = p
    var dd = d
    val visited = mutableSetOf<Pair<Point, Direction>>()
    while (true) {
        if (!visited.add(pp to dd)) return true
        if (!contains(pp)) return false
        val next = pp + dd
        val blocked = next == o || get(next) == '#'
        pp = if (blocked) pp else next
        dd = if (blocked) dd.right() else dd
    }
}

fun main() {
    val grid = File("y24/y24d06.txt").readLines()
    val start = grid.indices.flatMap { r ->
        grid[r].indices.mapNotNull { c -> if (grid[r][c] == '^') Point(r, c) else null }
    }.firstOrNull() ?: throw IllegalStateException("No start found")

    val path = grid.getPath(start)
    val result1 = path.size
    println("Result 1: $result1")

    val result2 = path.count {
        grid[it] == '.' && grid.isValid(it, start, Direction(-1, 0))
    }
    println("Result 2: $result2")
}
