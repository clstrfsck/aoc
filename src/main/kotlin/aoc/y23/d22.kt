package aoc.y23

import java.io.File

data class Brick(val xs: IntRange, val ys: IntRange, val zs: IntRange)
data class Links(val supportedBy: List<Brick>, val supporting: List<Brick>)

private fun Brick.adjustHeight(to: Int): Brick = Brick(xs, ys, (zs.last - zs.first).let { to..(to + it) })
private fun IntRange.intersects(other: IntRange) = intersect(other).isNotEmpty()

class BrickStack(lines: List<String>) {
    private val stack: Map<Brick, Links>

    init {
        val snapshot = lines.map {
            it.split("~").map { coords -> coords.split(",").map(String::toInt) }.let { ns ->
                Brick(ns[0][0]..ns[1][0], ns[0][1]..ns[1][1], ns[0][2]..ns[1][2])
            }
        }.sortedBy { brick -> brick.zs.first }
        val settled = snapshot.fold(setOf<Brick>()) { fallen, b -> fallen + project(fallen, b) }
        val supportedBy = settled.fold(listOf<Pair<Brick, List<Brick>>>()) { bricks, b ->
            val supports = bricks.map { it.first }
                .filter { it.xs.intersects(b.xs) && it.ys.intersects(b.ys) && it.zs.last + 1 == b.zs.first }
            bricks + (b to supports)
        }
        val map = supportedBy.flatMap { it.second.map { b -> b to it.first } }.groupBy({ it.first }, { it.second })
        stack = supportedBy.associate { it.first to Links(it.second, map[it.first] ?: emptyList()) }
    }

    private fun project(fallen: Set<Brick>, b: Brick) =
        b.adjustHeight(maxHeight(makeFloor(fallen, b.xs, b.ys), b.xs, b.ys) + 1)

    private fun makeFloor(fallen: Set<Brick>, xs: IntRange, ys: IntRange) = xs.flatMap { x ->
        ys.mapNotNull { y ->
            fallen.filter { x in it.xs && y in it.ys }.maxOfOrNull { it.zs.last }?.let { Pair(x, y) to it }
        }
    }.toMap()

    private fun maxHeight(floor: Map<Pair<Int, Int>, Int>, xs: IntRange, ys: IntRange) =
        xs.maxOf { x -> ys.maxOf { y -> floor[x to y] ?: -1  } }

    private fun supportedBy(b: Brick) = stack[b]?.supportedBy ?: throw IllegalArgumentException("No such brick: $b")
    private fun supporting(b: Brick) = stack[b]?.supporting ?: throw IllegalArgumentException("No such brick: $b")

    fun safeForDisintegration() =
        stack.values.filter { sb -> !sb.supporting.any { stack[it]!!.supportedBy.size == 1 } }.size

    fun sumFalling() = stack.keys.filter { sb -> supporting(sb).any { supportedBy(it).size == 1 } }
        .sumOf { sb ->
            generateSequence(stack.keys to setOf(sb)) { (candidates, fallen) ->
                val unsupported = candidates.filter { candidate ->
                    supportedBy(candidate).let { it.isNotEmpty() && it.all(fallen::contains) }
                }
                if (unsupported.isEmpty()) null else (candidates - unsupported.toSet()) to (fallen + unsupported)
            }.last().second.size - 1
        }
}

fun main() {
    val lines = File("y23/y23d22.txt").readLines()

    val stack = BrickStack(lines)

    val result1 = stack.safeForDisintegration()
    println("Result 1: $result1")

    val result2 = stack.sumFalling()
    println("Result 2: $result2")
}
