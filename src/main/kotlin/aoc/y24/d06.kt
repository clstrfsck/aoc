package aoc.y24

import java.io.File

private data class Point(val y: Int, val x: Int)
private data class Direction(val dy: Int, val dx: Int)
private operator fun Point.plus(d: Direction) = Point(y + d.dy, x + d.dx)
private fun Direction.right() = Direction(dx, -dy)
private operator fun List<CharArray>.contains(p: Point): Boolean = p.y in indices && p.x in get(p.y).indices
private operator fun List<CharArray>.get(p: Point) = if (contains(p)) this[p.y][p.x] else null
private data class PathState(val p: Point, val d: Direction, val visited: MutableSet<Pair<Point, Direction>>)

private fun List<CharArray>.getPath(start: Point) =
    generateSequence(PathState(start, Direction(-1, 0), mutableSetOf())) { (p, d, visited) ->
        if (contains(p)) {
            visited.add(p to d)
            val blocked = this[p + d] == '#'
            PathState(if (blocked) p else p + d, if (blocked) d.right() else d, visited)
        } else null
    }.last().visited.map { it.first }.toSet()

private tailrec fun List<CharArray>.isValid(o: Point, ps: PathState): Boolean {
    if (!ps.visited.add(ps.p to ps.d)) return true
    if (!contains(ps.p)) return false
    val (newp, newd) = if (get(ps.p + ps.d) == '#' || ps.p + ps.d == o) ps.p to ps.d.right() else ps.p + ps.d to ps.d
    return isValid(o, PathState(newp, newd, ps.visited))
}

fun main() {
    val grid = File("y24/y24d06.txt").readLines().map { it.toCharArray() }
    val start = grid.indices.flatMap { r ->
        grid[r].indices.mapNotNull { c -> if (grid[r][c] == '^') Point(r, c) else null }
    }.firstOrNull() ?: throw IllegalStateException("No start found")
    val path = grid.getPath(start)

    val result1 = path.size
    println("Result 1: $result1")

    val result2 = path.filter { grid[it] == '.' }.count {
        grid.isValid(it, PathState(start, Direction(-1, 0), mutableSetOf()))
    }
    println("Result 2: $result2")
}
