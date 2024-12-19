package day11

import org.example.util.ResourceLoader
import java.util.concurrent.ConcurrentHashMap

private val map = ResourceLoader.useResourceFile("/day11/day11.txt") { lines ->
    val map = ConcurrentHashMap<Long, Long>()
    lines.first().split(" ").map(String::toLong).forEach { map[it] = 1L }
    return@useResourceFile map
}

fun solver(rounds: Int) {
    var currentMap = map
    for (i in 0 until rounds) {
        val roundResultMap = ConcurrentHashMap<Long, Long>()
        for (entry in currentMap.entries) {
            val (key, count) = entry
            when {
                key == 0L -> {
                    roundResultMap.merge(1L, count) { old, new -> old + new }
                }
                key.toString().length % 2 == 0 -> {
                    val toString = key.toString()
                    val firstHalf = toString.substring(0, toString.length / 2).toLong()
                    var secondHalf = toString.substring(toString.length / 2).dropWhile { it == '0' }
                    if (secondHalf.isEmpty()) secondHalf = "0"
                    val secondHalfKey = secondHalf.toLong()

                    roundResultMap.merge(firstHalf, count) { old, new -> old + new }
                    roundResultMap.merge(secondHalfKey, count) { old, new -> old + new }
                }
                else -> {
                    roundResultMap.merge(key * 2024, count) { old, new -> old + new }
                }
            }
        }
        currentMap = roundResultMap
    }

    println("Total stones: ${currentMap.values.sum()}")
}

fun part1() {
    solver(25)
}

fun part2() {
    solver(75)
}