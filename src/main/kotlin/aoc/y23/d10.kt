package aoc.y23

import java.io.File

enum class Direction(val dy: Int, val dx: Int) {
    NORTH(-1, 0), EAST(0, 1), SOUTH(1, 0), WEST(0, -1);

    val reversed get() = when (this) {
        NORTH -> SOUTH
        EAST -> WEST
        SOUTH -> NORTH
        WEST -> EAST
    }
}

private data class MazePoint(val y: Int, val x: Int) {
    operator fun plus(direction: Direction) = MazePoint(y + direction.dy, x + direction.dx)
}

private data class InputOutput(val input: Direction, val output: Direction) {
    fun hasDirection(direction: Direction) = input == direction || output == direction
    fun exitFor(entry: Direction) = when (entry) {
        input -> output
        output -> input
        else -> throw IllegalStateException("$this $entry")
    }
}

private fun findInputOutput(nodes: Map<MazePoint, InputOutput>, start: MazePoint): InputOutput {
    val directions = Direction.values().filter { nodes[start + it]?.hasDirection(it.reversed) == true }
    return InputOutput(directions[0], directions[1])
}

private val pipeMap = mapOf(
    '|' to InputOutput(Direction.NORTH, Direction.SOUTH),
    '-' to InputOutput(Direction.WEST, Direction.EAST),
    'L' to InputOutput(Direction.NORTH, Direction.EAST),
    'J' to InputOutput(Direction.NORTH, Direction.WEST),
    '7' to InputOutput(Direction.SOUTH, Direction.WEST),
    'F' to InputOutput(Direction.SOUTH, Direction.EAST)
)

private data class PipeMaze(val start: MazePoint, val nodes: Map<MazePoint, InputOutput>, val yLimit: Int, val xLimit: Int) {
    fun next(p: MazePoint, d: Direction) = (p + d).let { it to nodes[it]!!.exitFor(d.reversed) }

    fun loop(): List<MazePoint> = generateSequence(next(start, nodes[start]!!.input)) { (p, d) ->
        if (p == start) null else next(p, d)
    }.map { it.first }.toList()

    fun loopArea() = loop().toSet().let { loop ->
        (0 until yLimit).sumOf { y ->
            (0 until xLimit).fold(Pair(false, 0)) { s, x ->
                val point = MazePoint(y, x)
                val inLoop = point in loop
                val inside = (inLoop && nodes[point]!!.input == Direction.NORTH) xor s.first
                Pair(inside, if (!inLoop && inside) s.second + 1 else s.second)
            }.second
        }
    }
}

fun main() {
    val lines = File("y23/y23d10.txt").readLines()

    val nodes = lines.flatMapIndexed { y, row ->
        row.mapIndexedNotNull { x, ch -> pipeMap[ch]?.let { MazePoint(y, x) to it } }
    }.toMap()
    val start = lines.flatMapIndexed { y, row ->
        row.mapIndexedNotNull { x, ch -> if (ch == 'S') MazePoint(y, x) else null }
    }.single()
    val startNode = start to findInputOutput(nodes, start)
    val maze = PipeMaze(start, nodes + startNode, lines.size, lines[0].length)

    val result1 = maze.loop().size / 2
    println("Result 1: $result1")

    val result2 = maze.loopArea()
    println("Result 2: $result2")
}
