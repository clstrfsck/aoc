package aoc.y24.d16

import java.io.File
import java.util.PriorityQueue

data class Pnt(val y: Int, val x: Int)
data class Vec(val dy: Int, val dx: Int)
operator fun Pnt.plus(d: Vec) = Pnt(y + d.dy, x + d.dx)
fun Vec.right() = Vec(dx, -dy)
fun Vec.left() = Vec(-dx, dy)

fun String.toGrid() = lines().flatMapIndexed { y, l -> l.mapIndexed { x, c -> Pnt(y, x) to c } }.toMap()

fun parts(grid: Map<Pnt, Char>): Pair<Int, Int> {
    val queue = PriorityQueue<Triple<List<Pnt>, Vec, Int>>(compareBy { it.third })
    val start = grid.entries.first { it.value == 'S' }.key
    val end = grid.entries.first { it.value == 'E' }.key
    queue.add(Triple(listOf(start), Vec(0, 1), 0))
    var min = Int.MAX_VALUE
    val best = HashSet<Pnt>()
    val seen = HashMap<Pair<Pnt, Vec>, Int>()
    while (queue.isNotEmpty()) {
        val (p, d, s) = queue.poll()
        if (p.last() == end) {
            if (s <= min) min = s else return min to best.size
            best.addAll(p)
        }
        if (p.last() to d !in seen || seen[p.last() to d]!! >= s) {
            seen[p.last() to d] = s
            if (grid[p.last() + d] != '#') queue.add(Triple(p + (p.last() + d), d, s + 1))
            queue.add(Triple(p, d.right(), s + 1000))
            queue.add(Triple(p, d.left(), s + 1000))
        }
    }
    return Int.MAX_VALUE to Int.MAX_VALUE
}

fun main() {
    val input = File("y24/y24d16.txt").readText()

    val (result1, result2) = parts(input.toGrid())
    println("Result 1: $result1")
    println("Result 2: $result2")
}