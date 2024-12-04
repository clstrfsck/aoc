package aoc.y23

import java.io.File
import kotlin.math.max
import kotlin.math.min

private sealed class Node {
    abstract fun accept(item: Map<Char, Int>): Boolean
    fun findPaths() = mutableListOf<List<Pair<Boolean?, Node>>>().apply { addPath(listOf(), this) }

    private fun addPath(currentPath: List<Pair<Boolean?, Node>>, paths: MutableList<List<Pair<Boolean?, Node>>>) {
        when (this) {
            is Internal -> {
                trueNode.addPath(currentPath + Pair(true, this), paths)
                falseNode.addPath(currentPath + Pair(false, this), paths)
            }
            is Leaf -> paths.add(currentPath + Pair(null, this))
        }
    }

    sealed class Internal(val trueNode: Node, val falseNode: Node) : Node() {
        abstract fun test(item: Map<Char, Int>): Boolean
        override fun accept(item: Map<Char, Int>) = if (test(item)) trueNode.accept(item) else falseNode.accept(item)

        class Less(val category: Char, val value: Int, tn: Node, fn: Node) : Internal(tn, fn) {
            override fun test(item: Map<Char, Int>): Boolean = item[category]!! < value
        }
        class Greater(val category: Char, val value: Int, tn: Node, fn: Node) : Internal(tn, fn) {
            override fun test(item: Map<Char, Int>): Boolean = item[category]!! > value
        }
    }

    abstract class Leaf : Node() {
        companion object {
            val accept = object : Leaf() { override fun accept(item: Map<Char, Int>) = true }
            val reject = object : Leaf() { override fun accept(item: Map<Char, Int>) = false }
        }
    }
}

private fun String.parseToken() =
    if (':' in this) split(':').let { (catOpValue, target) -> target to catOpValue } else this to null
private fun String.parseRule() = split('{', '}').let { (name, ops) -> name to ops.split(',').map { it.parseToken() } }
private fun String.parseItem() =
    removePrefix("{").removeSuffix("}").split(',').associate { e -> e.split('=').let { it[0][0] to it[1].toInt() } }

private fun Map<String, List<Pair<String, String?>>>.buildExpressionTree(target: String) = when (target) {
    "A" -> Node.Leaf.accept
    "R" -> Node.Leaf.reject
    else -> buildNode(get(target)!!)
}

private fun Map<String, List<Pair<String, String?>>>.buildNode(tokens: List<Pair<String, String?>>): Node {
    val (target, categoryOpValue) = tokens[0]
    return if (categoryOpValue == null) buildExpressionTree(target)
    else {
        val category = categoryOpValue[0]
        val value = categoryOpValue.substring(2).toInt()
        when (categoryOpValue[1]) {
            '<' -> Node.Internal.Less(category, value, buildExpressionTree(target), buildNode(tokens.drop(1)))
            '>' -> Node.Internal.Greater(category, value, buildExpressionTree(target), buildNode(tokens.drop(1)))
            else -> throw IllegalStateException("$target -> $categoryOpValue")
        }
    }
}

private fun List<Pair<Boolean?, Node>>.combinations(): Long {
    val limits = "xmas".associate { it to 1..4000 }.toMutableMap()
    for ((branch, n) in this) {
        if (branch == true) when (n) {
            is Node.Internal.Less -> limits.computeIfPresent(n.category) { _, l -> l.first..min(n.value - 1, l.last) }
            is Node.Internal.Greater -> limits.computeIfPresent(n.category) { _, l -> max(n.value+1, l.first)..l.last }
            else -> Unit
        } else if (branch == false) when (n) {
            is Node.Internal.Less -> limits.computeIfPresent(n.category) { _, l -> max(n.value, l.first)..l.last }
            is Node.Internal.Greater -> limits.computeIfPresent(n.category) { _, l -> l.first..min(n.value, l.last) }
            else -> Unit
        }
    }
    return limits.values.fold(1L) { acc, limit -> acc * limit.count() }
}

fun main() {
    val text = File("y23/y23d19.txt").readText().trim()

    val (ruleLines, objectLines) = text.trim().split("\n\n").map { it.split("\n") }
    val expression = ruleLines.associate { it.parseRule() }.buildExpressionTree("in")
    val items = objectLines.map { it.parseItem() }

    val result1 = items.filter { expression.accept(it) }.sumOf { it.values.sum() }
    println("Result 1: $result1")

    val result2 = expression.findPaths().filter { it.last().second == Node.Leaf.accept }.sumOf { it.combinations() }
    println("Result 2: $result2")
}
