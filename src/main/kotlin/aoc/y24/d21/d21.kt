package aoc.y24.d21

import java.io.File

data class Pos(val r: Int, val c: Int)
data class Dir(val dr: Int, val dc: Int)
operator fun Pos.minus(p: Pos) = Dir(r - p.r, c - p.c)
operator fun Pos.plus(v: Dir) = Pos(r + v.dr, c + v.dc)

val positions = mapOf(
    '7' to Pos(0, 0), '8' to Pos(0, 1), '9' to Pos(0, 2),
    '4' to Pos(1, 0), '5' to Pos(1, 1), '6' to Pos(1, 2),
    '1' to Pos(2, 0), '2' to Pos(2, 1), '3' to Pos(2, 2),
    'P' to Pos(3, 0), '0' to Pos(3, 1), 'A' to Pos(3, 2),

    'p' to Pos(0, 0), '^' to Pos(0, 1), 'a' to Pos(0, 2),
    '<' to Pos(1, 0), 'v' to Pos(1, 1), '>' to Pos(1, 2)
)
val directions = mapOf('^' to Dir(-1, 0), 'v' to Dir(1, 0), '<' to Dir(0, -1), '>' to Dir(0, 1))

fun Char.position() = positions[this] ?: error("Invalid position $this")
fun Char.direction() = directions[this] ?: error("Invalid direction $this")

fun String.permutations(): List<String> {
    if (isEmpty()) return emptyList()
    if (length == 1) return listOf(this)
    val result = mutableListOf<String>()
    for (i in indices) {
        val char = this[i]
        val remaining = substring(0, i) + substring(i + 1)
        for (perm in remaining.permutations()) {
            result.add(char + perm)
        }
    }
    return result
}

fun moves(start: Pos, end: Pos, panic: Pos): List<String> {
    val (dr, dc) = end - start
    val kdr = if (dr < 0) "^".repeat(-dr) else "v".repeat(dr)
    val kdc = if (dc < 0) "<".repeat(-dc) else ">".repeat(dc)
    return (kdr + kdc).permutations()
        .filter { s -> s.runningFold(start) { acc, dir -> acc + dir.direction() }.none { it == panic } }
        .map { it + "a" }.ifEmpty { listOf("a") }
}

typealias Cache = HashMap<Pair<String, Int>, Long>

fun minLength(cache: Cache, code: String, limit: Int, level: Int = 0): Long = cache.getOrPut(code to level) {
    val panic = if (level == 0) 'P'.position() else 'p'.position()
    val start = if (level == 0) 'A'.position() else 'a'.position()
    code.fold(start to 0L) { (pos, length), char ->
        val nextPos = char.position()
        val moves = moves(pos, nextPos, panic)
        nextPos to length + if (level == limit) moves.first().length.toLong() else moves.minOf {
            minLength(cache, it, limit, level + 1)
        }
    }.second
}

fun main() {
    val codes = File("y24/y24d21.txt").readText().trim().lines()

    val result1 = Cache().let { cache -> codes.sumOf { minLength(cache, it, 2) * it.substring(0..2).toLong() } }
    println("Result 1: $result1")

    val result2 = Cache().let { cache -> codes.sumOf { minLength(cache, it, 25) * it.substring(0..2).toLong() } }
    println("Result 2: $result2")
}
