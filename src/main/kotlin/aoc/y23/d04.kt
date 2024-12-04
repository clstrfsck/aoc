package aoc.y23

import java.io.File

data class ScratchCard(val card: Int, val winningNumbers: Set<Int>, val scratchedNumbers: Set<Int>) {
    val winCount = (winningNumbers intersect scratchedNumbers).size
    private val winRange = (card + 1)..(card + winCount)
    fun cardCount(cards: List<ScratchCard>): Int = 1 + cards.slice(winRange).sumOf { it.cardCount(cards) }
}

private fun String.toNumbers() = split(" ").filter { it.isNotBlank() }.map { it.toInt() }.toSet()
private fun String.parseCard(n: Int) =
    split(": ")[1].trim().split(" | ").let { ScratchCard(n, it[0].toNumbers(), it[1].toNumbers()) }

fun main() {
    val lines = File("y23/y23d04.txt").readLines()
    val scratchCards = lines.mapIndexed { i, line -> line.parseCard(i) }

    val result1 = scratchCards.map { it.winCount }.filter { it > 0 }.sumOf { 1 shl (it - 1) }
    println("Result 1: $result1")

    val result2 = scratchCards.sumOf { it.cardCount(scratchCards) }
    println("Result 1: $result2")
}
