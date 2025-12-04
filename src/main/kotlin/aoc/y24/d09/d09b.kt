package aoc.y24.d09

import java.io.File

sealed class Content(open val size: Int)
data class Empty(override val size: Int) : Content(size)
data class Fyle(val id: Long, override val size: Int) : Content(size)

private fun String.parse() = sequence {
    (this@parse + "0").windowed(2, 2).fold(0L) { n, s ->
        yield(Fyle(n, s[0].digitToInt()))
        yield(Empty(s[1].digitToInt()))
        n + 1
    }
}.toList()

private fun Content.multiplierAt(pos: Int) = size * (pos * 2L + size - 1L) / 2L
private fun Fyle.valueAt(pos: Int) = id * multiplierAt(pos)

private tailrec fun Array<Content>.part1(tot: Long, left: Int, pos: Int, right: Int): Long = if (left >= right) {
    when (val elt = this[left]) {
        is Empty -> tot
        is Fyle -> tot + elt.valueAt(pos)
    }
} else when (val elt = this[left]) {
    is Fyle -> part1(tot + elt.valueAt(pos), left + 1, pos + elt.size, right)
    is Empty -> when (val elt2 = this[right]) {
        is Empty -> part1(tot, left, pos, right - 1)
        is Fyle -> {
            if (elt.size >= elt2.size) {
                this[left] = Empty(elt.size - elt2.size)
                part1(tot + elt2.valueAt(pos), left, pos + elt2.size, right - 1)
            } else {
                this[right] = Fyle(elt2.id, elt2.size - elt.size)
                part1(tot + elt2.id * elt.multiplierAt(pos), left + 1, pos + elt.size, right)
            }
        }
    }
}

private fun Array<Content>.part2(): Long {
    val positions = runningFold(0) { total, elt -> total + elt.size }.toIntArray()
    val emptys = MutableList<Int?>(10) { 1 }

    fun findEmpty(size: Int, maxPos: Int, pos: Int): Int? {
        return if (pos > maxPos) null
        else when (val elt = this[pos]) {
            is Fyle -> error("Only empty at odd positions")
            is Empty -> {
                if (elt.size >= size) pos.also { this[it] = Empty(elt.size - size) }
                else findEmpty(size, maxPos, pos + 2)
            }
        }
    }

    fun findEmpty(size: Int, maxPos: Int): Int? = when (val elt = emptys[size]) {
        null -> null
        else -> findEmpty(size, maxPos, elt).also { emptys[size] = it }
    }

    return foldRightIndexed(0L) { i, elt, total ->
        when (elt) {
            is Empty -> total
            is Fyle -> {
                val newPos = findEmpty(elt.size, i)?.let { j ->
                    positions[j].also { positions[j] += elt.size }
                } ?: positions[i]
                total + elt.valueAt(newPos)
            }
        }
    }
}

fun main() {
    val input = File("y24/y24d09.txt").readText()
    val content = input.parse()

    val result1 = content.toTypedArray().part1(0, 0, 0, content.size - 1)
    println("Result 1: $result1")

    val result2 = content.toTypedArray().part2()
    println("Result 2: $result2")
}
