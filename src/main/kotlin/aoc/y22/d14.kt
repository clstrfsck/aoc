package aoc.y22

import java.io.File
import kotlin.math.sign

data class SandCave(
    val occupied: MutableSet<Position>,
    val maxY: Int = occupied.maxOfOrNull(Position::y)!!
) {
    fun fillFrom(at: Position, floor: Int = -1) {
        while (at !in occupied) {
            var curr = at
            while (true) {
                val next = move(curr, floor) ?: return
                if (next == curr) break
                curr = next
            }
            occupied.add(curr)
        }
    }

    private fun move(at: Position, floor: Int): Position? {
        if (floor < 0) {
            if (at.y > maxY) return null
        } else if (at.y == floor) return at
        Position(at.x, at.y + 1).let { if (it !in occupied) return it }
        Position(at.x - 1, at.y + 1).let { if (it !in occupied) return it }
        Position(at.x + 1, at.y + 1).let { if (it !in occupied) return it }
        return at
    }

    companion object {
        fun fromWallDefinitions(wd: List<String>) = fromWalls(wd.map(::parseWall))

        private fun fromWalls(ws: List<List<Position>>) =
            SandCave(ws.flatMap { toLines(it) }.toMutableSet())
        private fun parseWall(s: String) = s.split(" -> ").map { parsePoint(it) }
        private fun parsePoint(s: String) = s.split(',').let { (x, y) -> Position(x.toInt(), y.toInt()) }
        private fun toLines(lines: List<Position>): List<Position> {
            var curr = lines.first()
            val positions = mutableListOf(curr)
            for (next in lines.subList(1, lines.size)) {
                val dx = (next.x - curr.x).sign
                val dy = (next.y - curr.y).sign
                while (curr != next) {
                    curr = Position(curr.x + dx, curr.y + dy)
                    positions.add(curr)
                }
            }
            return positions
        }
    }
}

fun main() {
    val lines = File("y22/y22d14.txt").readLines()

    val cave = SandCave.fromWallDefinitions(lines)
    val initialOccupations = cave.occupied.size

    cave.fillFrom(Position(500, 0))
    val result1 = cave.occupied.size - initialOccupations
    println("Result 1: $result1")

    cave.fillFrom(Position(500, 0), cave.maxY + 1)
    val result2 = cave.occupied.size - initialOccupations
    println("Result 2: $result2")
}