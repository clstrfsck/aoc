package aoc.y23

import java.io.File

private data class Point(
    val x: Int,
    val y: Int
) {
    val neighbours: Set<Point> by lazy {
        setOf(
            Point(x - 1, y - 1),
            Point(x - 1, y),
            Point(x - 1, y + 1),
            Point(x, y - 1),
            Point(x, y + 1),
            Point(x + 1, y - 1),
            Point(x + 1, y),
            Point(x + 1, y + 1)
        )
    }
}

private data class NumberInfo(
    val digits: List<Char>,
    val locations: Set<Point>
) {
    fun value() = digits.joinToString("").toInt()
    fun adjacentTo(point: Point) = (locations intersect point.neighbours).isNotEmpty()
    fun adjacentToAny(points: Iterable<Point>) = points.any { adjacentTo(it) }
}

private fun List<String>.parseNumbers(): List<NumberInfo> {
    return buildList {
        this@parseNumbers.forEachIndexed { y, row ->
            val digits = mutableListOf<Char>()
            val locations = mutableListOf<Point>()
            row.forEachIndexed { x, ch ->
                if (ch.isDigit()) {
                    digits.add(ch)
                    locations.add(Point(x, y))
                } else if (digits.isNotEmpty()) {
                    add(NumberInfo(digits.toList(), locations.toSet()))
                    digits.clear()
                    locations.clear()
                }
            }
            if (digits.isNotEmpty())
                add(NumberInfo(digits.toList(), locations.toSet()))
        }
    }
}

private fun List<String>.parseSymbols(test: (Char) -> Boolean): List<Point> {
    return buildList {
        this@parseSymbols.forEachIndexed { y, row ->
            row.forEachIndexed { x, ch ->
                if (!ch.isDigit() && test(ch)) add(Point(x, y))
            }
        }
    }
}

private fun <T> Iterable<T>.productOf(selector: (T) -> Int) = fold(1) { acc, e -> acc * selector(e) }

fun main() {
    val lines = File("y23/y23d03.txt").readLines()
    val numbers = lines.parseNumbers()

    val symbols1 = lines.parseSymbols { it != '.' }
    val result1 = numbers.filter { it.adjacentToAny(symbols1) }.sumOf { it.value() }
    println("Result 1: $result1")

    val symbols2 = lines.parseSymbols { it == '*' }
    val result2 = symbols2.map { sym -> numbers.filter { it.adjacentTo(sym) } }
        .filter { it.size == 2 }.sumOf { e -> e.productOf { it.value() } }
    println("Result 1: $result2")
}
