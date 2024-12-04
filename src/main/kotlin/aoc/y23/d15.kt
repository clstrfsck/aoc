package aoc.y23

import java.io.File
import kotlin.collections.LinkedHashMap

private fun String.aocHash() = fold(0) { h, c -> ((h + c.code) * 17) % 256 }

fun process(boxes: Array<LinkedHashMap<String, Int>>, label: String, len: String) {
    boxes[label.aocHash()].let { box -> box.compute(label) { _, _ -> if (len.isEmpty()) null else len.toInt() } }
}

fun main() {
    val text = File("y23/y23d15.txt").readText().trim()

    val result1 = text.split(",").sumOf { it.aocHash() }
    println("Result 1: $result1")

    val regex = "[-=]".toRegex()
    val boxes = Array<LinkedHashMap<String, Int>>(256) { LinkedHashMap() }
    text.split(",").map { regex.split(it) }.map { (label, len) -> process(boxes, label, len) }
    val result2 = boxes.flatMapIndexed { boxNumber, lenses ->
        lenses.values.mapIndexed { slot, len -> (boxNumber + 1) * (slot + 1) * len }
    }.sum()
    println("Result 2: $result2")
}
