package aoc.y24.d23

import java.io.File

typealias Vertices = Set<String>
fun Map<String, Vertices>.maximumCliques() = sequence { bk(emptySet(), keys, emptySet(), this@maximumCliques) }

suspend fun SequenceScope<Vertices>.bk(r: Vertices, p: Vertices, x: Vertices, g: Map<String, Vertices>) {
    if (p.isEmpty() && x.isEmpty()) yield(r) else {
        val ps = p.toList()
        ps.indices.forEach { i ->
            val (seen, v) = ps.slice(0..i).let { it.toSet() to it.last() }
            val gv = g[v].orEmpty()
            bk(r + v, (p - seen).intersect(gv), (x - seen).intersect(gv), g)
        }
    }
}

fun main() {
    val g = File("y24/y24d23.txt").readText().trim().lines().flatMap {
        it.split("-").let { (a, b) ->  listOf(a to b, b to a)  }
    }.groupBy({ it.first }, { it.second }).mapValues { it.value.toSet() }

    val result1 = g.keys.sumOf { a ->
        val aa = g[a].orEmpty()
        aa.sumOf { b -> (aa intersect g[b].orEmpty()).count { c -> listOf(a, b, c).any { it.startsWith("t") } } }
    } / 6
    println("Result 1: $result1")

    val result2 = g.maximumCliques().maxBy { it.size }.sorted().joinToString(",")
    println("Result 2: $result2")
}
