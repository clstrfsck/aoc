package aoc.y24.d14

import java.io.File
import kotlin.math.max
import kotlin.math.pow

data class Pnt(val x: Int, val y: Int)
data class Vec(val dx: Int, val dy: Int)
operator fun Vec.times(scalar: Int) = Vec(dx * scalar, dy * scalar)
operator fun Vec.div(scalar: Int) = Vec(dx / scalar, dy / scalar)
operator fun Pnt.plus(v: Vec) = Pnt(x + v.dx, y + v.dy)
fun Pnt.mod(m: Vec) = Pnt(x.mod(m.dx), y.mod(m.dy))
data class Robot(val p: Pnt, val v: Vec)

val extent = Vec(101, 103)

fun parse(input: List<String>) = input.map { line ->
    line.filter { it.isDigit() || it == '-' || it == ',' || it == ' ' }
        .split(Regex("[ ,]")).map { it.toInt() }
        .let { Robot(Pnt(it[0], it[1]), Vec(it[2], it[3])) }
}

private fun part1(robots: List<Robot>): Int {
    val mid = extent / 2
    return robots.mapNotNull {
        val p = (it.p + it.v * 100).mod(extent)
        if (p.x == mid.dx || p.y == mid.dy) null
        else p.x / (mid.dx + 1) * 2 + p.y / (mid.dy + 1)
    }.groupingBy { it }.eachCount().values.reduce(Int::times)
}

fun List<Int>.variance() = average().let { mean -> map { (it - mean).pow(2.0) }.average() }

fun part2a(robots: List<Robot>): Int {
    return generateSequence(1 to robots) { (i, rs) -> i + 1 to rs.map { (p, v) -> Robot((p + v).mod(extent), v) }  }
        .takeWhile { (i, rs) ->
            rs.groupBy { it.p.x }.values.none { it.size >= 25 } ||
                rs.groupBy { it.p.y }.values.none { it.size >= 25 }
        }.last().first
}

fun part2(robots: List<Robot>): Int {
    val vars = generateSequence(robots) { it.map { (p, v) -> Robot((p + v).mod(extent), v) } }
        .take(max(extent.dx, extent.dy))
        .map { it.map { r -> r.p.x }.variance() to it.map { r -> r.p.y }.variance() }
        .toList()
    val bx = vars.withIndex().minBy { it.value.first }.index
    val by = vars.withIndex().minBy { it.value.second }.index
    val inv = extent.dx.toBigInteger().modInverse(extent.dy.toBigInteger()).toInt()
    return bx + (inv * (by - bx)).mod(extent.dy) * extent.dx
}

fun main() {
    val lines = File("y24/y24d14.txt").readLines()
    val rs = parse(lines)

    val result1 = part1(rs)
    println("Result 1: $result1")

    val result2 = part2(rs)
    println("Result 2: $result2")
}
