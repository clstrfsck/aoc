package aoc.y22

import java.io.File
import java.util.LinkedList

data class Monkey(
    val id: Int,
    val items: LinkedList<Long>,
    val op: (Long) -> Long,
    val divisor: Long,
    val ifTrue: Int,
    val ifFalse: Int,
    var inspections: Long = 0
) {
    fun process(monkeys: Map<Int, Monkey>, reduce: (Long) -> Long) {
        while (items.isNotEmpty()) {
            inspections += 1
            val item = items.removeFirst()
            val newItem = reduce.invoke(op.invoke(item))
            if ((newItem % divisor) == 0L) monkeys[ifTrue]!!.items.addLast(newItem)
            else monkeys[ifFalse]!!.items.addLast(newItem)
        }
    }
}

fun List<String>.toMonkey(): Monkey {
    val id = get(0).let { it.substring(7, it.length - 1).toInt() }
    val items = get(1).substring(18).split(',').map { it.trim().toLong() }
    val op = parseOp(get(2).substring(23))
    val divisor = get(3).substring(21).toLong()
    val ifTrue = get(4).substring(29).toInt()
    val ifFalse = get(5).substring(30).toInt()
    return Monkey(id, LinkedList(items), op, divisor, ifTrue, ifFalse)
}

fun parseOp(op: String): (Long) -> Long {
    if (op == "* old") return { n: Long -> n * n }
    val arg = op.substring(2).toLong()
    return when (op[0]) {
        '*' -> { n: Long -> n * arg }
        '+' -> { n: Long -> n + arg }
        else -> throw IllegalArgumentException("Unknown operation: $op")
    }
}

fun main() {
    val lines = File("y22/y22d11.txt").readLines()
    val monkeys1 = lines.chunked(7).map(List<String>::toMonkey).associateBy { it.id }
    repeat(20) {
        monkeys1.values.forEach { m -> m.process(monkeys1) { it / 3 } }
    }
    val result1 = monkeys1.values.map { it.inspections }.sortedDescending().take(2).reduce(Long::times)
    println("Result 1: $result1")

    val monkeys2 = lines.chunked(7).map(List<String>::toMonkey).associateBy { it.id }
    val modulus = monkeys2.values.map { it.divisor }.reduce(Long::times)
    repeat(10000) {
        monkeys2.values.forEach { m -> m.process(monkeys2) { it % modulus } }
    }
    val result2 = monkeys2.values.map { it.inspections }.sortedDescending().take(2).reduce(Long::times)
    println("Result 2: $result2")
}