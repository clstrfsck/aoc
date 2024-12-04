package aoc.y22

import java.io.File
import java.util.LinkedList
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.system.measureNanoTime

val SENSOR_REGEX = "Sensor at x=([-0-9]+), y=([-0-9]+): closest beacon is at x=([-0-9]+), y=([-0-9]+)".toRegex()
fun parseSensorAndBeacons(lines: List<String>) = lines.map(::parseSensorAndBeacon)
fun parseSensorAndBeacon(line: String) = SENSOR_REGEX.matchEntire(line)!!.groupValues.let { (_, sx, sy, bx, by) ->
    Pair(Position(sx.toInt(), sy.toInt()), Position(bx.toInt(), by.toInt()))
}

fun IntRange.overlap(r: IntRange): Boolean = first <= r.last && r.first <= last
fun IntRange.adjacent(r: IntRange): Boolean = first == r.last + 1 || r.first == last + 1

class MergedRanges(initRanges: List<IntRange> = emptyList()) {
    private val sortedRanges = LinkedList<IntRange>()
    val ranges: List<IntRange> get() = sortedRanges

    init {
        initRanges.forEach(this::merge)
    }

    fun merge(range: IntRange) {
        if (!range.isEmpty()) {
            var i = 0
            while (i < sortedRanges.size) {
                val curr = sortedRanges[i]
                if (range.overlap(curr) || range.adjacent(curr)) {
                    joinFrom(i, range)
                    return
                }
                if (curr.first > range.first) break
                i += 1
            }
            sortedRanges.add(i, range)
        }
    }

    private fun joinFrom(i: Int, r: IntRange) {
        var newRange = outer(r, sortedRanges[i])
        val nextIndex = i + 1
        while (nextIndex < sortedRanges.size) {
            val next = sortedRanges[nextIndex]
            if (newRange.overlap(next) || newRange.adjacent(next)) {
                newRange = outer(newRange, next)
                sortedRanges.removeAt(nextIndex)
            } else break
        }
        sortedRanges[i] = newRange
    }

    private fun outer(l: IntRange, r: IntRange) = min(l.first, r.first)..max(l.last, r.last)
}

fun Pair<Position, Int>.rangeForLine(y: Int): IntRange =
    abs(first.y - y).let { IntRange(first.x - (second - it), first.x + (second - it)) }

fun mdistance(p1: Position, p2: Position): Int = abs(p1.x - p2.x) + abs(p1.y - p2.y)

fun main() {
    val lines = File("y22/y22d15.txt").readLines()

    val sensorAndBeacons = parseSensorAndBeacons(lines)
    val sensorAndDistances = sensorAndBeacons.map { Pair(it.first, mdistance(it.first, it.second)) }
    val beacons = sensorAndBeacons.map { it.second }

    val mr = MergedRanges(sensorAndDistances.map { it.rangeForLine(2000000) })
    val result1 = mr.ranges.sumOf { it.last - it.first + 1 } - beacons.distinct().count { it.y == 2000000 }
    println("Result 1: $result1")

    val elapsed = measureNanoTime {
        val row = (0..4000000).asSequence().map { line ->
            Pair(MergedRanges(sensorAndDistances.map { it.rangeForLine(line) }).ranges, line)
        }.first { it.first.size > 1 }
        val col = row.first[0].last + 1
        val result2 = 4000000L * col.toLong() + row.second.toLong()
        println("Result 2: $result2")
    }
    println(elapsed / 1e9)
}