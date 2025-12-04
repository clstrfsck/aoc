package aoc.y24.d15

import java.io.File

data class Pnt(val y: Int, val x: Int)
data class Vec(val dy: Int, val dx: Int)
private operator fun Pnt.plus(dir: Vec): Pnt = Pnt(y + dir.dy, x + dir.dx)
private operator fun Pnt.minus(dir: Vec): Pnt = Pnt(y - dir.dy, x - dir.dx)

val dirs = mapOf('^' to Vec(-1, 0), 'v' to Vec(1, 0), '<' to Vec(0, -1), '>' to Vec(0, 1))
val Char?.isBox get() = this == 'O' || this == '[' || this == ']'
val Char?.dir get() = dirs[this] ?: throw IllegalArgumentException("Invalid direction: $this")

fun MutableMap<Pnt, Char>.push(robot: Pnt, dir: Vec): Pnt {
    var p = robot + dir
    while (this[p].isBox) p += dir
    if (this[p] == '.') {
        while (p != robot) {
            this[p] = this[p - dir]!!
            p -= dir
        }
        this[p] = '.'
        return robot + dir
    }
    return robot
}

fun part1(grid: Map<Pnt, Char>, commands: String): Int {
    val g = grid.toMutableMap()
    var robot = g.entries.first { it.value == '@' }.key
    for (c in commands) robot = g.push(robot, c.dir)
    return g.entries.filter { (_, c) -> c == 'O' }.sumOf { (p, _) -> p.y * 100 + p.x }
}

fun Map<Pnt, Char>.other(box: Pnt): Pnt = if (this[box] == '[') Pnt(box.y, box.x + 1) else Pnt(box.y, box.x - 1)
fun Map<Pnt, Char>.pushable(b1: Pnt, dir: Vec): Boolean {
    val b2 = other(b1)
    return (get(b1 + dir) == '.' || (get(b1 + dir).isBox && pushable(b1 + dir, dir))) &&
            (get(b2 + dir) == '.' || (get(b2 + dir).isBox && pushable(b2 + dir, dir)))
}

fun MutableMap<Pnt, Char>.pushBig(b1: Pnt, dir: Vec) {
    val b2 = other(b1)
    if (get(b1 + dir).isBox) pushBig(b1 + dir, dir)
    if (get(b2 + dir).isBox) pushBig(b2 + dir, dir)
    this[b1 + dir] = this[b1]!!
    this[b2 + dir] = this[b2]!!
    this[b1] = '.'
    this[b2] = '.'
}

fun part2(grid: Map<Pnt, Char>, commands: String): Int {
    val g = grid.entries.flatMap { (p, c) ->
        when (c) {
            '@' -> listOf(Pnt(p.y, p.x * 2) to '@', Pnt(p.y, p.x * 2 + 1) to '.')
            'O' -> listOf(Pnt(p.y, p.x * 2) to '[', Pnt(p.y, p.x * 2 + 1) to ']')
            else -> listOf(Pnt(p.y, p.x * 2) to c, Pnt(p.y, p.x * 2 + 1) to c)
        }
    }.toMap().toMutableMap()

    var robot = g.entries.first { it.value == '@' }.key
    for (c in commands) {
        val d = c.dir
        if (d.dy == 0) robot = g.push(robot, d)
        else {
            val p = robot + d
            if (g[p].isBox && g.pushable(p, d)) {
                g.pushBig(p, d)
                g[robot] = '.'
                robot += d
                g[robot] = '@'
            } else if (g[p] == '.') {
                g[robot] = '.'
                robot += d
                g[robot] = '@'
            }
        }
    }
    return g.entries.filter { (_, c) -> c == '[' }.sumOf { (p, _) -> p.y * 100 + p.x }
}

fun main() {
    val (grid, lines) = File("y24/y24d15.txt").readText().split("\n\n")
    val commands = lines.replace("\n", "")
    val points = grid.lines().flatMapIndexed { y, line -> line.mapIndexed { x, c -> Pnt(y, x) to c } }.toMap()

    val result1 = part1(points.toMutableMap(), commands)
    println("Result 1: $result1")

    val result2 = part2(points.toMutableMap(), commands)
    println("Result 2: $result2")
}
