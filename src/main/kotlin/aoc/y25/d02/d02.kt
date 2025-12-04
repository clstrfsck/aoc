package aoc.y25.d02

import java.io.File

fun main() {
    val line = File("y25/y25d02.txt").readLines().first()
    val nums = line.split(",").map { it.split("-").map(String::toLong) }.flatMap { (f, l) -> f..l }

    val result1 = Regex("^(.+)(\\1)$").let { r -> nums.filter { it.toString().matches(r) } }.sum()
    println("Result1: $result1")

    val result2 = Regex("^(.+)(\\1)+$").let { r -> nums.filter { it.toString().matches(r) } }.sum()
    println("Result2: $result2")
}
