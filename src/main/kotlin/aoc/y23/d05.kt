package aoc.y23

import java.io.File
import kotlin.math.min

data class MapRange(val dest: Long, val source: Long, val length: Long)
data class SeedRange(val start: Long, val length: Long)

private fun String.toLongs() = split(" ").filter { it.isNotBlank() }.map { it.toLong() }
private fun seedsFrom(line: String) = line.split(": ")[1].toLongs()
private fun sourceTargetFrom(text: String) = text.split("\n").drop(1).map { rangesFrom(it.toLongs()) }
private fun rangesFrom(longs: List<Long>): MapRange = MapRange(longs[0], longs[1], longs[2])

private fun List<List<MapRange>>.processSeeds(seeds: List<SeedRange>): Long {
    return seeds.asSequence().map { s ->
        fold(sequenceOf(s)) { inputs, mapping ->
            sequence {
                for (p in inputs) {
                    var (start, length) = p
                    while (length > 0) {
                        var split = false
                        for (m in mapping) {
                            val delta = start - m.source
                            if (delta in 0 until m.length) {
                                val len = min(m.length - delta, length)
                                yield(SeedRange(m.dest + delta, len))
                                start += len
                                length -= len
                                split = true
                                break
                            }
                        }
                        if (!split) {
                            yield(SeedRange(start, length))
                            break
                        }
                    }
                }
            }
        }
    }.flatten().minOf { it.start }
}

fun main() {
    val text = File("y23/y23d05.txt").readText().trim()
    val sections = text.split("\n\n")
    val seeds = seedsFrom(sections[0])
    val mappings = sections.drop(1).map { sourceTargetFrom(it) }

    val result1 = mappings.processSeeds(seeds.map { SeedRange(it, 1L) })
    println("Result 1: $result1")

    val result2 = mappings.processSeeds(seeds.chunked(2).map { (start, len) -> SeedRange(start, len) })
    println("Result 2: $result2")
}
