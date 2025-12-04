package aoc.y24.d17

import java.io.File

/*
 * l0:  bst ra          // rb = ra mod 8
 *      bxl 1           // rb = rb xor 1
 *      cdv rb          // rc = ra / 2^rb
 *      adv 3           // ra = ra / (2^3)
 *      bxc 7(ignored)  // rb = rb xor rc
 *      bxl 6           // rb = rb xor 6
 *      out rb          // output rb mod 8
 *      jnz l0          // if (ra != 0) goto l0
 */

fun cycle(ra: Long): Pair<Int, Long> {
    val rb = (ra.mod(8) xor 1).toLong()
    return ((rb xor (ra / (1 shl rb.toInt()))) xor 6).mod(8) to ra / 8
}

fun output(ra: Long) = generateSequence(cycle(ra)) { (_, ra) -> if (ra == 0L) null else cycle(ra) }.map { it.first }

fun part1(ra: Long) = output(ra).joinToString(",")

fun search(result: MutableList<Long>, program: List<Int>, length: Int = 1, value: Long = 0L) {
    val upper = value shl 3
    for (lower in 0..7) {
        if (output(upper + lower).toList() == program.takeLast(length)) {
            if (length == program.size) result.add(upper + lower)
            else search(result, program, length + 1, upper + lower)
        }
    }
}

fun part2(program: List<Int>) = mutableListOf<Long>().also { search(it, program) }.minOrNull()

fun main() {
    val (registers, program) = File("y24/y24d17.txt").readText().split("\n\n").map { s ->
        Regex("\\d+").findAll(s).map { it.value.toInt() }.toList()
    }

    val result1 = part1(registers[0].toLong())
    println("Result 1: $result1")

    val result2 = part2(program)
    println("Result 2: $result2")
}
