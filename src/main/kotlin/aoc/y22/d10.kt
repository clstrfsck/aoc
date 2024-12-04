package aoc.y22

import java.io.File

fun List<Long>.applyNoop() = this + last()
fun List<Long>.applyAdd(value: Long) = this + (last() + value)
fun List<Long>.applyInstruction(ins: String) = when (ins.substring(0, 4)) {
    "noop" -> applyNoop()
    "addx" -> applyNoop().applyAdd(ins.substring(5).toLong())
    else -> throw IllegalArgumentException("Unknown instruction: $ins")
}
fun List<Long>.isNear(cycle: Int) = (cycle % 40).let { get(cycle) in (it - 1)..(it + 1) }

fun main() {
    val lines = File("y22/y22d10.txt").readLines()
    val cpu = lines.fold(listOf(1), List<Long>::applyInstruction)

    val result1 = listOf(20, 60, 100, 140, 180, 220).sumOf { cpu[it - 1] * it }
    println("Result 1: $result1")

    println("Result 2:")
    (1..240).forEach { cycle ->
        print(if (cpu.isNear(cycle - 1)) '#' else ' ')
        if (cycle % 40 == 0) println()
    }
}