package aoc.y24

import java.io.File
import kotlin.math.abs

fun main() {
    val lines = File("y24/y24d01.txt").readLines()

    val lists = lines.map { it.split("   ").let { ss -> ss[0] to ss[1] } }
    val (list1, list2) = lists.unzip().toList().map { it.map(String::toInt).sorted() }

    val result1 = list1.zip(list2).sumOf { (v1, v2) -> abs(v1 - v2) }
    println("Result 1: $result1")

    val result2 = list2.groupingBy { it }.eachCount().let { f -> list1.sumOf { it * (f[it] ?: 0) } }
    println("Result 1: $result2")
}
