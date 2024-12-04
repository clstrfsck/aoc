package aoc.y23

import java.io.File

val notNodeName = "[^A-Z0-9]+".toRegex()
private fun nodeFrom(s: String) = s.split(notNodeName).let { Pair(it[0], Pair(it[1], it[2])) }

private fun Pair<String, String>.next(ch: Char) = if (ch == 'L') first else second

private fun cycle(s: String): () -> Char {
    var i = 0
    return { s[i++ % s.length] }
}

private fun nodeSeq(start: String, ends: Set<String>, nodes: Map<String, Pair<String, String>>, path: () -> Char) =
    generateSequence(start) { node ->
        nodes[node]?.next(path()).let { next -> if (next in ends) null else next }
    }

private fun gcd(a: Long, b: Long): Long = if (b == 0L) a else gcd(b, a % b)
private fun lcm(a: Long, b: Long) = gcd(a, b).let { (a / it) * b }

fun main() {
    val lines = File("y23/y23d08.txt").readLines()
    val path = lines.first()
    val nodes = lines.drop(2).associate { nodeFrom(it) }

    val result1 = nodeSeq("AAA", setOf("ZZZ"), nodes, cycle(path)).count()
    println("Result 1: $result1")

    val startNodes = nodes.keys.filter { it.endsWith("A") }
    val endNodes = nodes.keys.filter { it.endsWith("Z") }.toSet()
    val result2 = startNodes.map { nodeSeq(it, endNodes, nodes, cycle(path)).count().toLong() }.reduce(::lcm)
    println("Result 2: $result2")
}
