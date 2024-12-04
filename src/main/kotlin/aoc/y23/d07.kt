package aoc.y23

import java.io.File

private val cardRanks1 = "AKQJT98765432".reversed().mapIndexed { i, ch -> Pair(ch, i) }.toMap()
private val cardRanks2 = "AKQT98765432J".reversed().mapIndexed { i, ch -> Pair(ch, i) }.toMap()

enum class TypeRank { HIGH_CARD, ONE_PAIR, TWO_PAIR, THREE_KIND, FULL_HOUSE, FOUR_KIND, FIVE_KIND }

data class Hand(val cards: String, val bid: Int) {
    private val part1Type = part1Type()
    private val part2Type = part2Type()

    fun part1Compare(right: Hand): Int {
        val result = part1Type.compareTo(right.part1Type)
        return if (result == 0) compareCards(0, right.cards, cardRanks1) else result
    }

    fun part2Compare(right: Hand): Int {
        val result = part2Type.compareTo(right.part2Type)
        return if (result == 0) compareCards(0, right.cards, cardRanks2) else result
    }

    private fun part1Type(): TypeRank {
        val ordered = cards.groupingBy { it }.eachCount().toList().sortedBy { -it.second }
        return when (ordered[0].second) {
            5 -> TypeRank.FIVE_KIND
            4 -> TypeRank.FOUR_KIND
            3 -> if (ordered[1].second == 2) TypeRank.FULL_HOUSE else TypeRank.THREE_KIND
            2 -> if (ordered[1].second == 2) TypeRank.TWO_PAIR else TypeRank.ONE_PAIR
            else -> TypeRank.HIGH_CARD
        }
    }

    private fun part2Type(): TypeRank {
        val orderedMap = cards.groupingBy { it }.eachCount()
        val ordered = orderedMap.toList().filter { it.first != 'J' }.sortedBy { -it.second }
        val jokerCount = orderedMap['J'] ?: 0
        if (jokerCount == 5) return TypeRank.FIVE_KIND
        return when (ordered[0].second) {
            5 -> TypeRank.FIVE_KIND
            4 -> if (jokerCount > 0) TypeRank.FIVE_KIND else TypeRank.FOUR_KIND
            3 -> when (jokerCount) {
                2 -> TypeRank.FIVE_KIND
                1 -> TypeRank.FOUR_KIND
                else -> if (ordered[1].second == 2) TypeRank.FULL_HOUSE else TypeRank.THREE_KIND
            }
            2 -> when (jokerCount) {
                3 -> TypeRank.FIVE_KIND
                2 -> TypeRank.FOUR_KIND
                1 -> if (ordered[1].second == 2) TypeRank.FULL_HOUSE else TypeRank.THREE_KIND
                else -> if (ordered[1].second == 2) TypeRank.TWO_PAIR else TypeRank.ONE_PAIR
            }
            1 -> when (jokerCount) {
                4 -> TypeRank.FIVE_KIND
                3 -> TypeRank.FOUR_KIND
                2 -> TypeRank.THREE_KIND
                1 -> TypeRank.ONE_PAIR
                else -> TypeRank.HIGH_CARD
            }
            else -> throw IllegalStateException()
        }
    }

    private fun compareCards(index: Int, h2: String, ranking: Map<Char, Int>): Int {
        val result = ranking[cards[index]]!!.compareTo(ranking[h2[index]]!!)
        return if (result == 0) compareCards(index + 1, h2, ranking) else result // eww
    }
}

private fun handFrom(s: String) = s.split(" ").let { Hand(it[0], it[1].toInt()) }

fun main() {
    val lines = File("y23/y23d07.txt").readLines()
    val hands = lines.map { handFrom(it) }

    println(hands.sortedWith(Hand::part1Compare))

    val result1 = hands.sortedWith(Hand::part1Compare).mapIndexed { i, hand -> (i + 1) * hand.bid }.sum()
    println("Result 1: $result1")

    val result2 = hands.sortedWith(Hand::part2Compare).mapIndexed { i, hand -> (i + 1) * hand.bid }.sum()
    println("Result 1: $result2")
}
