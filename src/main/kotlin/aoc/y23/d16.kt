package aoc.y23

import java.io.File

data class BeamDir(val dy: Int, val dx: Int) {
    fun bltrMirror() = BeamDir(-dx, -dy)
    fun tlbrMirror() = BeamDir(dx, dy)
}

private data class BeamPoint(val y: Int, val x: Int) {
    fun travel(direction: BeamDir): BeamRay = BeamRay(BeamPoint(y + direction.dy, x + direction.dx), direction)
}

private data class BeamRay(val origin: BeamPoint, val dir: BeamDir)

private fun List<String>.exits(ray: BeamRay): List<BeamDir> {
    return when (get(ray.origin.y)[ray.origin.x]) {
        '/' -> listOf(ray.dir.bltrMirror())
        '\\' -> listOf(ray.dir.tlbrMirror())
        '-' -> if (ray.dir.dx == 0) listOf(BeamDir(0, -1), BeamDir(0, 1)) else listOf(ray.dir)
        '|' -> if (ray.dir.dy == 0) listOf(BeamDir(-1, 0), BeamDir(1, 0)) else listOf(ray.dir)
        else -> listOf(ray.dir)
    }
}

private fun List<String>.isValid(p: BeamPoint) = p.y in indices && p.x in get(0).indices

private fun List<String>.energisedTiles(start: BeamRay): Map<BeamPoint, Set<BeamDir>> {
    val queue = ArrayDeque<BeamRay>()
    val rays = mutableMapOf<BeamPoint, MutableSet<BeamDir>>()
    queue.add(start)
    while (queue.isNotEmpty()) {
        val current = queue.removeFirst()
        if (rays[current.origin]?.contains(current.dir) != true) {
            rays.computeIfAbsent(current.origin) { mutableSetOf() }.add(current.dir)
            queue.addAll(exits(current).map { current.origin.travel(it) }.filter { isValid(it.origin) })
        }
    }
    return rays
}

fun main() {
    val lines = File("y23/y23d16.txt").readLines()

    val result1 = lines.energisedTiles(BeamRay(BeamPoint(0, 0), BeamDir(0, 1))).size
    println("Result 1: $result1")

    val yMax = lines.size - 1
    val xMax = lines[0].length - 1
    val result2 = listOf(
        (0..xMax).map { x ->
            listOf(
                lines.energisedTiles(BeamRay(BeamPoint(0, x), BeamDir(1, 0))).size,
                lines.energisedTiles(BeamRay(BeamPoint(yMax, x), BeamDir(-1, 0))).size
            )
        },
        (0..yMax).map { y ->
            listOf(
                lines.energisedTiles(BeamRay(BeamPoint(y, 0), BeamDir(0, 1))).size,
                lines.energisedTiles(BeamRay(BeamPoint(y, xMax), BeamDir(0, -1))).size
            )
        }
    ).maxOf { o -> o.maxOf { it.max() } }
    println("Result 2: $result2")
}
