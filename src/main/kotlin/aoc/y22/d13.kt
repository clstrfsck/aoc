package aoc.y22

import java.io.File

fun inOrderList(left: List<*>, right: List<*>): Int {
    val leftIter = left.iterator()
    val rightIter = right.iterator()
    while (leftIter.hasNext() && rightIter.hasNext()) {
        inOrder(leftIter.next()!!, rightIter.next()!!).let { if (it != 0) return it }
    }
    if (leftIter.hasNext()) return 1
    if (rightIter.hasNext()) return -1
    return 0
}
fun inOrderInt(left: Int, right: Int): Int = left.compareTo(right)
fun inOrder(left: Any, right: Any): Int {
    return if (left is Int && right is Int) {
        inOrderInt(left, right)
    } else if (left is List<*> && right is List<*>) {
        inOrderList(left, right)
    } else if (left is Int && right is List<*>) {
        inOrderList(listOf(left), right)
    } else if (left is List<*> && right is Int) {
        inOrderList(left, listOf(right))
    } else throw IllegalStateException("Unknown things (left, right): $left, $right")
}

fun parseInt(ch: Char, i: ListIterator<Char>): Int {
    var accumulator = ch.digitToInt()
    while (i.hasNext()) {
        val next = i.next()
        if (!next.isDigit()) return accumulator
        accumulator = (accumulator * 10) + next.digitToInt()
    }
    throw IllegalStateException("No terminating char for int")
}
fun parseList(i: ListIterator<Char>): List<Any> {
    val items = mutableListOf<Any>()
    while (i.hasNext()) {
        when (val ch = i.next()) {
            ',' -> continue
            '[' -> items.add(parseList(i))
            ']' -> return items
            in '0'..'9' -> items.add(parseInt(ch, i)).also { i.previous() }
            else -> throw IllegalStateException("Unexpected character: $ch")
        }
    }
    throw IllegalStateException("Unmatched brackets")
}
fun parseList(s: String) = s.toList().listIterator().also { it.next() }.let { parseList(it) }

fun main() {
    val lines = File("y22/y22d13.txt").readLines()
    val lists = lines.filter(String::isNotBlank).map { parseList(it) }

    val result1 = lists.windowed(2, 2).mapIndexed { i, (l, r) -> if (inOrder(l, r) < 0) i + 1 else 0 }.sum()
    println("Result 1: $result1")

    val divs = Pair(parseList("[[2]]"), parseList("[[6]]"))
    val newList = (lists + divs.toList()).sortedWith(::inOrder)
    val result2 = (newList.indexOf(divs.first) + 1) * (newList.indexOf(divs.second) + 1)
    println("Result 2: $result2")
}