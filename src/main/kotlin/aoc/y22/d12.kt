package aoc.y22

import java.io.File
import java.util.LinkedList

data class Grid(val heights: List<List<Int>>) {
    private fun get(p: Position) = heights[p.y][p.x]
    private fun isValid(p: Position, h: Int) = p.y in heights.indices && p.x in heights[0].indices && get(p) <= h + 1
    private fun frontier(p: Position) = listOf(-1, 1).flatMap {
        listOf(Position(p.x + it, p.y), Position(p.x, p.y + it))
    }.filter { isValid(it, get(p)) }

    fun bfs(starts: List<Position>, goal: Position): Int {
        val visited = mutableSetOf<Position>().also { it.addAll(starts) }
        val queue = LinkedList<Pair<Position, Int>>().also { visited.forEach { elt -> it += Pair(elt, 0) } }
        while (queue.isNotEmpty()) {
            val (curr, count) = queue.pop()
            if (curr == goal) return count
            frontier(curr).filter { it !in visited }.forEach {
                queue += Pair(it, count + 1)
                visited += it
            }
        }
        return -1
    }
}

fun List<String>.parseGrid(): Grid =
    mapOf('S' to 'a', 'E' to 'z').let { Grid(map { r -> r.map { ch -> it.getOrDefault(ch, ch).code - 'a'.code } }) }

fun List<String>.parsePositions(): Triple<Position, Position, List<Position>> {
    val positions = flatMapIndexed { y, row -> row.mapIndexed  { x, ch -> Pair(ch, Position(x, y)) } }
    val start = positions.first { it.first == 'S' }.second
    val end = positions.first { it.first == 'E' }.second
    val lowGround = positions.filter { it.first == 'S' || it.first == 'a' }.map { it.second }
    return Triple(start, end, lowGround)
}

fun main() {
    val lines = File("y22/y22d12.txt").readLines()
    val grid = lines.parseGrid()
    val (start, end, lowGround) = lines.parsePositions()

    val result1 = grid.bfs(listOf(start), end)
    println("Result 1: $result1")

    val result2 = grid.bfs(lowGround, end)
    println("Result 2: $result2")
}