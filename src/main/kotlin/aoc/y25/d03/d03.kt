package aoc.y25.d03

import java.io.File

fun List<List<Int>>.getMaxJolts(digitCount: Int) = sumOf { bank ->
    (digitCount - 1 downTo 0).fold(0L to bank) { (acc, digits), digit ->
        val (index, digit) = digits.subList(0, digits.size - digit).withIndex().maxByOrNull { it.value }!!
        10 * acc + digit to digits.subList(index + 1, digits.size)
    }.first
}

fun main() {
    val data = File("y25/y25d03.txt").readLines().map { line -> line.map { it.digitToInt() } }

    println(data.getMaxJolts(2))
    println(data.getMaxJolts(12))
}
