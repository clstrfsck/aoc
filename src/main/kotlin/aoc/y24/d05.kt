package aoc.y24

import java.io.File

fun main() {
    val (rawRules, rawUpdates) = File("y24/y24d05.txt").readText().trim().split("\n\n")
    val rules = rawRules.lines().map { it.split("|").map(String::toInt) }.map { (k, v) -> k to v }
        .groupBy({it.first}, {it.second}).mapValues { it.value.toSet() }
    val updates = rawUpdates.lines().map { it.split(",").map(String::toInt) }

    val (validUpdates, invalidUpdates) = updates.partition { update ->
        update.windowed(2).all { (l, r) -> rules[l]?.contains(r) == true }
    }
    val result1 = validUpdates.sumOf { it[it.size / 2] }
    println("Result 1: $result1")

    val result2 = invalidUpdates.map {
        it.sortedWith { l, r -> if (l == r) 0 else if (rules[l]?.contains(r) == true) -1 else 1 }
    }.sumOf { it[it.size / 2] }
    println("Result 2: $result2")
}
