package aoc.y24

import java.io.File

private fun validEqn(target: Long, acc: Long, op: Char, args: List<Long>, ops: List<Char>): Boolean {
    if (args.isEmpty()) return acc == target
    val newAcc = when (op) {
        '+' -> acc + args.first()
        '*' -> acc * args.first()
        '|' -> "$acc${args.first()}".toLong()
        else -> throw IllegalArgumentException("Unknown operator: $op")
    }
    return ops.any { validEqn(target, newAcc, it, args.drop(1), ops) }
}

fun main() {
    val input = File("y24/y24d07.txt").readLines()
    val equations = input.map { it.split(": ") }.map { (v, ns) -> v.toLong() to ns.split(" ").map { it.toLong() } }

    val result1 = equations.filter { (v, ns) -> validEqn(v, 0, '+', ns, listOf('+', '*')) }.sumOf { it.first }
    println("Result 1: $result1")

    val result2 = equations.filter { (v, ns) -> validEqn(v, 0, '+', ns, listOf('+', '*', '|')) }.sumOf { it.first }
    println("Result 2: $result2")
}
