package aoc.y24.d07

import java.io.File

fun canInvertConcat(acc: Long, n: Long) = acc != n && acc.toString().endsWith(n.toString())
fun invertConcat(acc: Long, n: Long) = acc.toString().dropLast(n.toString().length).toLong()

private fun validEqn(acc: Long, op: Char, args: List<Long>, ops: List<Char>): Boolean {
    val n = args.last()
    if (args.size == 1) return acc == n
    val newAcc = when (op) {
        '+' -> if (acc >= n) acc - n else return false
        '*' -> if (acc % n == 0L) acc / n else return false
        '|' -> if (canInvertConcat(acc, n)) invertConcat(acc, n) else return false
        else -> throw IllegalArgumentException("Unknown operator: $op")
    }
    return ops.any { validEqn(newAcc, it, args.dropLast(1), ops) }
}

fun main() {
    val input = File("y24/y24d07.txt").readLines()
    val equations = input.map { it.split(": ") }.map { (v, ns) -> v.toLong() to ns.split(" ").map { it.toLong() } }

    val ops1 = listOf('+', '*')
    val result1 = equations.filter { (v, ns) -> ops1.any { validEqn(v, it, ns, ops1) } }.sumOf { it.first }
    println("Result 1: $result1")

    val ops2 = listOf('+', '*', '|')
    val result2 = equations.filter { (v, ns) -> ops2.any { validEqn(v, it, ns, ops2) } }.sumOf { it.first }
    println("Result 2: $result2")
}
