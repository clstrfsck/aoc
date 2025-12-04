package aoc.y24.d09

import java.io.File

fun List<Long>.checksum() = withIndex().filter { it.value != -1L }.sumOf { it.index * it.value }

private fun String.parse() = sequence {
    (this@parse + "0").windowed(2, 2).fold(0L) { n, s ->
        yield(generateSequence { n }.take(s[0].digitToInt()))
        yield(generateSequence { -1L }.take(if (s.length == 1) 0 else s[1].digitToInt()))
        n + 1
    }
}.flatten().toList()

fun part1(blocks: List<Long>) = sequence {
    var frontIdx = 0
    var backIdx = blocks.size - 1
    while (frontIdx <= backIdx) {
        if (blocks[frontIdx] != -1L) yield(blocks[frontIdx++])
        else if (blocks[backIdx] == -1L) backIdx -= 1
        else {
            frontIdx += 1
            yield(blocks[backIdx--])
        }
    }
}.toList()

fun part2(blocks: List<Long>): List<Long> {
    val compactedBlocks = blocks.toMutableList()
    val fileInfo = mutableMapOf<Long, Pair<Int, Int>>()

    for (elt in compactedBlocks.withIndex()) {
        if (elt.value != -1L) {
            fileInfo[elt.value] = fileInfo[elt.value]?.let { (s, l) -> s to l + 1 } ?: (elt.index to 1)
        }
    }

    // Move files starting from the highest file ID
    for (fileId in fileInfo.keys.sortedDescending()) {
        val (fileStart, fileSize) = fileInfo[fileId]!!
        var freeSpaceStart = -1
        var freeSpaceLength = 0

        for (i in compactedBlocks.indices) {
            if (compactedBlocks[i] == -1L) {
                if (freeSpaceStart == -1) {
                    freeSpaceStart = i
                }
                freeSpaceLength++
                if (freeSpaceLength == fileSize) {
                    break
                }
            } else {
                freeSpaceStart = -1
                freeSpaceLength = 0
            }
        }

        if (freeSpaceStart in 0..<fileStart) {
            for (i in 0 until fileSize) {
                compactedBlocks[freeSpaceStart + i] = fileId
                compactedBlocks[fileStart + i] = -1L
            }
        }
    }
    return compactedBlocks
}

fun main() {
    val input = File("y24/y24d09.txt").readText()
    val blocks = input.parse()

    val result1 = part1(blocks).checksum()
    println("Result 1: $result1")

    val result2 = part2(blocks).checksum()
    println("Result 2: $result2")
}
