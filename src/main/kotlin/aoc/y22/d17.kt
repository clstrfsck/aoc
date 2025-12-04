package aoc.y22

import java.io.File

data class TowerState(val blockIndex: Int, val dirIndex: Int, val heights: List<Int>)

val standardBlocks = listOf(
    listOf(Position(0, 0), Position(1, 0), Position(2, 0), Position(3, 0)),
    listOf(Position(1, 0), Position(0, 1), Position(1, 1), Position(2, 1), Position(1, 2)),
    listOf(Position(0, 0), Position(1, 0), Position(2, 0), Position(2, 1), Position(2, 2)),
    listOf(Position(0, 0), Position(0, 1), Position(0, 2), Position(0, 3)),
    listOf(Position(0, 0), Position(1, 0), Position(0, 1), Position(1, 1))
)

data class Tower(
    val across: List<Int>,
    val width: Int = 7,
    val blocks: List<List<Position>> = standardBlocks,
    val map: MutableList<MutableList<Boolean>> = mutableListOf(),
    var blockCount: Int = 0,
    var acrossCount: Int = 0,
    var pos: Position = Position(2, 3)
) {
    private val blockIndex: Int get() = blockCount % blocks.size
    private val acrossIndex: Int get() = (acrossCount - 1) % across.size
    val height: Int get() = map.size

    fun addBlock() {
        do {
            across()
        } while (down())
    }

    private fun across() {
        acrossCount += 1
        across[acrossIndex].let { dx ->
            Position(pos.x + dx, pos.y).let { if (it.good()) pos = it }
        }
    }

    private fun down(): Boolean {
        val newPos = Position(pos.x, pos.y - 1)
        if (newPos.good()) pos = newPos
        else { // grounded
            blocks[blockIndex].forEach {
                val bp = Position(pos.x + it.x, pos.y + it.y)
                while (bp.y >= map.size) map.add(MutableList(width) { false })
                map[bp.y][bp.x] = true
            }
            pos = Position(2, height + 3)
            blockCount += 1
            return false
        }
        return true
    }

    private fun floor(): List<Int> = (0 until width).map { x ->
        val maxY = height - 1
        (maxY downTo 0).forEach { if (map[it][x]) return@map maxY - it }
        return@map height
    }

    fun state() = TowerState(blockIndex, acrossIndex, floor())

    private fun Position.good() = (x >= 0) && (y >= 0) && blocks[blockIndex].all { bp ->
        val ny = y + bp.y
        val nx = x + bp.x
        (nx < width && ((ny >= height) || !map[ny][nx]))
    }
}

fun parseLine(line: String) = line.toList().map { ch ->
    when (ch) {
        '<' -> -1
        '>' -> +1
        else -> throw IllegalArgumentException("Unknown direction: $ch")
    }
}

fun main() {
    val line = File("y22/y22d17.txt").readLines().first()

    val result1 = Tower(parseLine(line)).let { tower ->
        repeat(2022) { tower.addBlock() }
        tower.height
    }
    println("Result 1: $result1")

    val result2 = Tower(parseLine(line)).let { tower ->
        val end = 1000000000000L
        val seen = mutableMapOf(tower.state() to Pair(0, 0))
        while (true) {
            tower.addBlock()
            val state = tower.state()
            val prior = seen[state]
            if (prior != null) {
                val remainingBlocks = end - tower.blockCount
                val blocksPerCycle = tower.blockCount - prior.first
                val remainingCycles = remainingBlocks / blocksPerCycle
                val cycleHeight = tower.height - prior.second
                repeat((remainingBlocks % blocksPerCycle).toInt()) { tower.addBlock() }
                return@let tower.height + cycleHeight * remainingCycles
            }
            seen[state] = Pair(tower.blockCount, tower.height)
        }
    }
    println("Result 2: $result2")
}