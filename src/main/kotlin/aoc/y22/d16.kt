package aoc.y22

import java.io.File
import kotlin.math.max

data class ValveInfo(val name: String, val flowRate: Int, val nearby: Set<String>)

val VALVE_REGEX = "Valve (.+) has flow rate=([-0-9]+); tunnels? leads? to valves? (.+)".toRegex()
fun parseValves(lines: List<String>) = lines.map(::parseValve)
fun parseValve(line: String) = VALVE_REGEX.matchEntire(line)!!.groupValues.let { (_, n, fr, vs) ->
    ValveInfo(n, fr.toInt(), vs.split(", ").toSet())
}

fun main() {
    val lines = File("y22/y22d16.txt").readLines()
    val valves = parseValves(lines).sortedBy { -it.flowRate }
    val indexLookup = valves.indices.associateBy { valves[it].name }
    val aaIndex = indexLookup["AA"] ?: throw IllegalStateException("Can't find valve 'AA'")
    val adjacency = valves.map { it.nearby.mapNotNull(indexLookup::get) }
    val bits = valves.count { it.flowRate > 0 }
    require(bits <= 31)
    val maxBitMask = 1 shl bits
    val opt = List(30) { List(valves.size) { MutableList(maxBitMask) { 0 } } }

    (1 until 30).forEach { time ->
        val plane = opt[time]
        val prevPlane = opt[time - 1]
        plane.indices.forEach { vIdx ->
            val row = plane[vIdx]
            val vBit = 1 shl vIdx
            row.indices.forEach { openValves ->
                val flow = if ((openValves and vBit) == 0) 0
                else prevPlane[vIdx][openValves - vBit] + valves[vIdx].flowRate * time
                row[openValves] = max(flow, adjacency[vIdx].maxOf { prevPlane[it][openValves] })
            }
        }
    }

    val result1 = opt[29][aaIndex].max()
    println("Result 1: $result1")

    val r = opt[25][aaIndex]
    val result2 = (1 until maxBitMask).maxOf { me ->
        (0 until me).maxOf { elephant ->
            if ((me and elephant) == 0) r[me] + r[elephant] else 0
        }
    }
    println("Result 2: $result2")
}