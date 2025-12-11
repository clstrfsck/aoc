package aoc.y25.d11

import java.io.File

typealias Memo = HashMap<Triple<String, Boolean, Boolean>, Long>

fun Map<String, List<String>>.countPaths(node: String, dac: Boolean, fft: Boolean, memo: Memo = Memo()): Long {
    if (node == "out") return if (dac && fft) 1 else 0
    val key = Triple(node, dac || node == "dac", fft || node == "fft")
    return memo.getOrPut(key) { get(node)?.sumOf { countPaths(it, key.second, key.third, memo) } ?: 0 }
}

fun main() {
    val lines = File("y25/y25d11.txt").readLines()
    val graph = lines.associate { line -> line.split(":").let { it[0] to it[1].trim().split(" ") } }

    println("Part1: ${graph.countPaths("you", dac = true, fft = true)}")
    println("Part2: ${graph.countPaths("svr", dac = false, fft = false)}")
}
