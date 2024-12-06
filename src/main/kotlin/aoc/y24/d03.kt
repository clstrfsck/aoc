package aoc.y24

import java.io.File

private val MUL = Regex("mul\\(([0-9]{1,3}),([0-9]{1,3})\\)")
private const val DO = "do()"
private const val DO_NOT = "don't()"

private tailrec suspend fun SequenceScope<List<Long>>.yieldMul(s: String, i: Int, skip: Boolean) {
    if (i < 0 || i >= s.length) return
    MUL.matchAt(s, i)?.let { yield(it.groupValues.drop(1).map(String::toLong)) }
    yieldMul(s, if (skip && s.regionMatches(i, DO_NOT, 0, DO_NOT.length)) s.indexOf(DO, i) else i + 1, skip)
}

fun main() {
    val contents = File("y24/y24d03.txt").readText()

    val result1 = sequence { yieldMul(contents, 0, false) }.sumOf { (a, b) -> a * b }
    println("Result 1: $result1")

    val result2 = sequence { yieldMul(contents, 0, true) }.sumOf { (a, b) -> a * b }
    println("Result 1: $result2")
}
