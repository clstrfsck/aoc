package aoc.y23

import java.io.File

const val START = 0
const val LAST = 24

val initial = listOf(
    'e' to 1,
    'f' to 2,
    'n' to 3,
    'o' to 4,
    's' to 5,
    't' to 6
)

val deltaTable = buildMap {
    for (i in START..LAST) {
        for (e in initial) put(Pair(i, e.first), e.second)
    }
    put(Pair(1, 'i'), 7)   // ei
    put(Pair(2, 'i'), 8)   // fi
    put(Pair(2, 'o'), 9)   // fo
    put(Pair(3, 'i'), 10)  // ni
    put(Pair(4, 'n'), 11)  // on
    put(Pair(5, 'e'), 12)  // se
    put(Pair(5, 'i'), 13)  // si
    put(Pair(6, 'h'), 14)  // th
    put(Pair(6, 'w'), 15)  // tw
    put(Pair(7, 'g'), 16)  // eig
    put(Pair(8, 'v'), 17)  // fiv
    put(Pair(9, 'n'), 11)  // fon
    put(Pair(9, 'u'), 18)  // fou
    put(Pair(10, 'n'), 19) // nin
    put(Pair(11, 'i'), 10) // oni
    put(Pair(12, 'i'), 7)  // sei
    put(Pair(12, 'v'), 20) // sev
    put(Pair(14, 'r'), 21) // thr
    put(Pair(16, 'h'), 22) // eigh
    put(Pair(19, 'i'), 10) // nini
    put(Pair(20, 'e'), 23) // seve
    put(Pair(21, 'e'), 24) // thre
    put(Pair(23, 'i'), 7)  // sevei
    put(Pair(24, 'i'), 7)  // threi
}

val omegaTable = buildMap {
    for (i in START..LAST) {
        for (ch in '0'..'9') put(Pair(i, ch), ch)
    }
    put(Pair(11, 'e'), '1')
    put(Pair(13, 'x'), '6')
    put(Pair(15, 'o'), '2')
    put(Pair(17, 'e'), '5')
    put(Pair(18, 'r'), '4')
    put(Pair(19, 'e'), '9')
    put(Pair(22, 't'), '8')
    put(Pair(23, 'n'), '7')
    put(Pair(24, 'e'), '3')
}

private fun String.transduce() = StringBuilder().let { sb ->
    fold(START) { state, ch ->
        val stateWithInput = Pair(state, ch)
        omegaTable[stateWithInput]?.let(sb::append)
        deltaTable[stateWithInput] ?: START
    }
    sb.toString()
}

private fun String.firstAndLast(): String = "${first()}${last()}"

fun main() {
    val lines = File("y23/y23d01.txt").readLines()

    val result1 = lines.sumOf { it.filter(Char::isDigit).firstAndLast().toInt() }
    println("Result 1: $result1")

    val result2 = lines.map(String::transduce).sumOf { it.firstAndLast().toInt() }
    println("Result 1: $result2")
}
