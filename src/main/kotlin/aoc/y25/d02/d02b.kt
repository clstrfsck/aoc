package aoc.y25.d02

import java.io.File

private fun String.equalHalves() = isNotEmpty() && length % 2 == 0 && chunked(length / 2).distinct().size == 1

private fun String.pi() = IntArray(length).also { pi ->
    var j = 0
    for (i in 1 until length) {
        while (j > 0 && get(i) != get(j)) j = pi[j - 1]
        if (get(i) == get(j)) j += 1
        pi[i] = j
    }
}

private fun String.isRepeatedUnit() = length >= 2
        && (length - pi()[length - 1]).let { p -> p < length && length % p == 0 }

fun main() {
    val line = File("y25/y25d02.txt").readLines().first()
    val nums = line.split(",").map { it.split("-").map(String::toLong) }.flatMap { (f, l) -> f..l }

    val result1 = nums.filter { it.toString().equalHalves() }.sum()
    println("Result1: $result1")

    val result2 = nums.filter { it.toString().isRepeatedUnit() }.sum()
    println("Result2: $result2")
}
