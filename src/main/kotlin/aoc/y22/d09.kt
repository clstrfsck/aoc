package aoc.y22

import java.io.File
import kotlin.math.abs
import kotlin.math.sign

data class Position(val x: Int = 0, val y: Int = 0)
data class RopeState(
    val knots: List<Position>,
    val tailPositions: Set<Position> = setOf(knots.last())
)

fun RopeState.applyCmd(cmd: Char): RopeState {
    val head = knots.first()
    val newHead = when (cmd) {
        'U' -> head.copy(y = head.y + 1)
        'D' -> head.copy(y = head.y - 1)
        'R' -> head.copy(x = head.x + 1)
        'L' -> head.copy(x = head.x - 1)
        else -> throw IllegalStateException("Unknown cmd: $cmd")
    }
    val newKnots = knots.subList(1, knots.size).runningFold(newHead) { h, t ->
        val dx = h.x - t.x
        val dy = h.y - t.y
        if (abs(dy) > 1 || abs(dx) > 1) Position(x = t.x + dx.sign, y = t.y + dy.sign) else t
    }
    return RopeState(newKnots, tailPositions + newKnots.last())
}

fun RopeState.applyCmds(cmdStr: String) =
    (1..cmdStr.substring(2).toInt()).fold(this) { acc, _ -> acc.applyCmd(cmdStr[0]) }

fun main() {
    val lines = File("y22/y22d09.txt").readLines()

    val result1 = lines.fold(RopeState(List(2) { Position() }), RopeState::applyCmds).tailPositions.size
    println("Result 1: $result1")

    val result2 = lines.fold(RopeState(List(10) { Position() }), RopeState::applyCmds).tailPositions.size
    println("Result 2: $result2")
}