package aoc.y24.d24

import java.io.File

data class Gate(val inputs: List<String>, val op: String, val output: String)

fun Map<String, Int>.extractNumber(prefix: String) = filterKeys { it.startsWith(prefix) }
    .mapKeys { it.key.drop(1).toInt() }.toList().fold(0L) { acc, (i, v) -> acc or (v.toLong() shl i) }

fun compute(values: MutableMap<String, Int>, gates: Map<String, Gate>, input: String): Int {
    return values.getOrPut(input) {
        val gate = gates[input] ?: error("No gate has output $input")
        val inputs = gate.inputs.map { compute(values, gates, it) }
        when (gate.op) {
            "AND" -> inputs.reduce(Int::and)
            "OR" -> inputs.reduce(Int::or)
            "XOR" -> inputs.reduce(Int::xor)
            else -> error("Unknown op: ${gate.op}")
        }
    }
}

fun compute(gates: List<Gate>, init: Map<String, Int>): Map<String, Int> {
    val gatesByOutput = gates.associateBy { it.output }
    return init.toMutableMap().also { values -> gates.forEach { compute(values, gatesByOutput, it.output) } }
}

fun main() {
    val (rawInit, rawGates) = File("y24/y24d24.txt").readText().trim().split("\n\n")
    val init = rawInit.lines().map { it.split(": ") }.associate { it[0] to it[1].toInt() }
    val gates = rawGates.lines().map { it.split(" ") } .map { (i1, op, i2, _, o) -> Gate(listOf(i1, i2), op, o)  }

    val result1 = compute(gates, init).extractNumber("z")
    println("Result 1: $result1")

    val swaps = listOf("kfp" to "hbs", "dhq" to "z18", "pdg" to "z22", "jcp" to "z27")
    val swap = swaps.map { (a, b) -> listOf(a to b, b to a) }.flatten().toMap()
    val newGates = gates.map { Gate(it.inputs, it.op, swap[it.output] ?: it.output) }
    val newSystem = compute(newGates, init)
    val x = newSystem.extractNumber("x")
    val y = newSystem.extractNumber("y")
    val z = newSystem.extractNumber("z")
    val lnz = ((x + y) xor z).countTrailingZeroBits()
    if (lnz == Long.SIZE_BITS) {
        val result2 = swaps.map { it.toList() }.flatten().sorted().joinToString(",")
        println("Result 2: $result2")
    } else {
        val eNodes = lnz.toString().padStart(2, '0').let { n -> "xyz".map { "$it$n" }.toSet() }
        val eGates = newGates.filter { g -> g.inputs.any { it in eNodes } || g.output in eNodes }.toSet()
        val iNodes = eGates.map { it.inputs + it.output }.flatten().toSet()
        val allGates = eGates + newGates.filter { g -> g.inputs.any { it in iNodes } }.toSet()
        val allInputs = allGates.map { it.inputs }.flatten().toSet()
        val allOutputs = allGates.map { it.output }.toSet()

        println("digraph BrokenAdder {")
        println("  node [shape=box width=.5 height=.5];")
        println("  rankdir=\"LR\";")

        (allOutputs - allInputs).forEach {
            println("  $it [color=\"green\" fontcolor=\"green\"];")
        }
        (allInputs - allOutputs).forEach {
            println("  $it [color=\"blue\" fontcolor=\"blue\"];")
        }
        var gateId = 1
        val labels = mapOf("XOR" to "⊻", "AND" to "∧", "OR" to "∨")
        for ((inputs, op, output) in allGates) {
            println("  g_$gateId [label=\"${labels[op]}\" color=\"red\" fontcolor=\"red\" width=.25 height=.25];")
            inputs.forEach { println("  $it -> g_$gateId;") }
            println("  g_$gateId -> $output;")
            gateId += 1
        }
        println("}")
    }
}