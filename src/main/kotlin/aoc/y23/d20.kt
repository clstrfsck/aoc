package aoc.y23

import java.io.File

private sealed class D20Node(val name: String, val outputNames: List<String>) {
    var watch: ((String, Boolean?) -> Unit)? = null
    fun pulse(from: String, pulse: Boolean) = doPulse(from, pulse).also { watch?.invoke(name, it) }
    protected abstract fun doPulse(from: String, pulse: Boolean): Boolean?
    class Broadcast(name: String, outputNames: List<String>): D20Node(name, outputNames) {
        override fun doPulse(from: String, pulse: Boolean) = pulse
    }
    class FlipFlop(name: String, outputNames: List<String>): D20Node(name, outputNames) {
        var state = false
        override fun doPulse(from: String, pulse: Boolean): Boolean? {
            if (!pulse) {
                state = !state
                return state
            }
            return null
        }
    }
    class Conjunction(name: String, outputNames: List<String>): D20Node(name, outputNames) {
        val states = mutableMapOf<String, Boolean>()
        fun setInputs(inputs: List<String>) {
            states.clear()
            states.putAll(inputs.map { it to false })
        }
        override fun doPulse(from: String, pulse: Boolean): Boolean {
            states[from] = pulse
            return !states.values.all { it }
        }
    }
}

private fun String.parseModule() = split(" -> ").let {
    val name = it[0].substring(1)
    val outputs = it[1].split(", ")
    when (it[0][0]) {
        '%' -> D20Node.FlipFlop(name, outputs)
        '&' -> D20Node.Conjunction(name, outputs)
        else -> D20Node.Broadcast(it[0], outputs)
    }
}

private fun Map<String, D20Node>.updateConjunctions(): Map<String, D20Node> {
    val conjns = entries.filter { it.value is D20Node.Conjunction }.associate { it.key to mutableListOf<String>() }
    values.forEach { node -> node.outputNames.forEach { conjns[it]?.add(node.name) } }
    conjns.entries.forEach { (get(it.key) as D20Node.Conjunction).setInputs(it.value) }
    return this
}

private fun Map<String, D20Node>.pushButton(): Pair<Int, Int> {
    var pulses = 0 to 0
    val queue = ArrayDeque<Triple<String, String, Boolean>>()
    queue.addLast(Triple("button", "broadcaster", false))
    while (!queue.isEmpty()) {
        val (from, name, input) = queue.removeFirst()
        pulses += if (input) 0 to 1 else 1 to 0
        val node = get(name)
        if (node != null) {
            node.pulse(from, input)?.let { next -> node.outputNames.forEach { queue.addLast(Triple(name, it, next)) } }
        }
    }
    return pulses
}

private operator fun Pair<Int, Int>.plus(p: Pair<Int, Int>) = (first + p.first) to (second + p.second)
private fun gcd(a: Long, b: Long): Long = if (b == 0L) a else gcd(b, a % b)
private fun lcm(a: Long, b: Long) = gcd(a, b).let { (a / it) * b }

fun main() {
    val lines = File("y23/y23d20.txt").readLines()

    val graph1 = lines.map { it.parseModule() }.associateBy { it.name }.updateConjunctions()
    val totals = (1..1000).map { graph1.pushButton() }.fold(0 to 0) { acc, p -> acc + p }
    val result1 = totals.first * totals.second
    println("Result 1: $result1")

    val graph2 = lines.map { it.parseModule() }.associateBy { it.name }.updateConjunctions()
    val outputNode = graph2.values.single { "rx" in it.outputNames }
    val nodeNames = graph2.values.filter { outputNode.name in it.outputNames }.map { it.name }.toSet()
    var pushCount = 0
    val triggerCounts = mutableMapOf<String, Int>()
    nodeNames.forEach {
        graph2[it]!!.watch = { t, o ->
            if (o == true) triggerCounts.putIfAbsent(t, pushCount)
        }
    }
    do {
        pushCount += 1
        graph2.pushButton()
    } while (triggerCounts.size != nodeNames.size)
    val result2 = triggerCounts.values.map(Int::toLong).reduce(::lcm)
    println("Result 2: $result2")
}
