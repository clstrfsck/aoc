package aoc.y25.d10

import com.google.ortools.Loader
import com.google.ortools.linearsolver.MPSolver
import java.io.File
import kotlin.math.roundToLong

val SPACES = Regex("\\s+")

data class Machine(val indicators: Set<Int>, val buttons: List<Set<Int>>, val joltage: List<Int>)

infix fun <T> Set<T>.symDiff(other: Set<T>): Set<T> = (this - other) + (other - this)

fun solveMachine1(indicators: Set<Int>, buttons: List<Set<Int>>): Int =
    generateSequence(listOf(emptySet<Int>() to 0) to setOf(emptySet<Int>())) { (queue, seen) ->
        queue.firstOrNull()?.let { (curr, steps) ->
            if (curr == indicators) null
            else buttons.map { curr symDiff it }
                .filter { it !in seen }
                .let { newStates -> (queue.drop(1) + newStates.map { it to (steps + 1) }) to (seen + newStates) }
        }
    }.lastOrNull()?.first?.firstOrNull { it.first == indicators }?.second ?: 0

fun solveMachine2(joltage: List<Int>, buttons: List<Set<Int>>): Double {
    val solver = MPSolver.createSolver("CBC") ?: throw IllegalStateException()
    val variables = Array(buttons.size) { i -> solver.makeIntVar(0.0, Double.POSITIVE_INFINITY, "x_$i") }

    joltage.indices.forEach { i ->
        val constraint = solver.makeConstraint(joltage[i].toDouble(), joltage[i].toDouble(), "c_$i")
        buttons.indices.filter { buttons[it].contains(i) }.forEach { constraint.setCoefficient(variables[it], 1.0) }
    }

    val objective = solver.objective()
    for (v in variables) objective.setCoefficient(v, 1.0)
    objective.setMinimization()

    val resultStatus = solver.solve()
    return if (resultStatus == MPSolver.ResultStatus.OPTIMAL) objective.value() else 0.0
}

fun String.rps(prefix: String, suffix: String) = removePrefix(prefix).removeSuffix(suffix)

fun main() {
    Loader.loadNativeLibraries()
    val lines = File("y25/y25d10.txt").readLines()

    val machines = lines.map { line ->
        val parts = line.trim().split(SPACES)
        Machine(
            indicators = parts[0].rps("[", "]").withIndex().filter { it.value == '#' }.map { it.index }.toSet(),
            buttons = parts.subList(1, parts.size - 1).map { it.rps("(", ")").split(",").map(String::toInt).toSet() },
            joltage = parts.last().rps("{", "}").split(",").map(String::toInt)
        )
    }

    val result1 = machines.sumOf { solveMachine1(it.indicators, it.buttons) }
    println("Result1: $result1")

    val result2 = machines.sumOf { solveMachine2(it.joltage, it.buttons).roundToLong() }
    println("Result2: $result2")
}
