package aoc.y24

import java.io.File

private data class TopoState(val input: List<Int>, val inDegree: Map<Int, Int?>, val sorted: List<Int> = listOf())

fun main() {
    val (rawRules, rawUpdates) = File("y24/y24d05.txt").readText().trim().split("\n\n")
    val rules = rawRules.lines().map { it.split("|").map(String::toInt) }
    val updates = rawUpdates.lines().map { it.split(",").map(String::toInt) }

    val (validUpdates, invalidUpdates) = updates.partition { update ->
        rules.all { rule -> rule.map { update.indexOf(it) }.let { (a, b) -> b < 0 || a < b } }
    }
    val result1 = validUpdates.sumOf { it[it.size / 2] }
    println("Result 1: $result1")

    val outMap = rules.groupBy({ it.first() }, { it.last() })
    val inMap = rules.groupBy({ it.last() }, { it.first() })
    val result2 = invalidUpdates.map { update ->
        val outLinks = update.fold(emptyMap<Int, List<Int>>()) { acc, page ->
            acc + (page to outMap[page].orEmpty().filter { it in update })
        }
        val inDegree = update.fold(emptyMap<Int, Int?>()) { acc, page ->
            acc + (page to inMap[page].orEmpty().count { it in update })
        }
        generateSequence(TopoState(update, inDegree)) { (current, inDegree, sorted) ->
            current.firstOrNull { inDegree[it] == 0 }?.let {
                val newInDegree = outLinks[it]?.map { n -> n to inDegree[n]?.minus(1) }.orEmpty()
                TopoState(current - it, inDegree + newInDegree, sorted + it)
            }
        }.last().sorted
    }.sumOf { it[it.size / 2] }
    println("Result 2: $result2")
}
