package day6

import org.example.util.ResourceLoader

typealias Coordinate = Pair<Int, Int> // Pair(Y,X)

private class CycleDetectedException : RuntimeException()

private val lines = ResourceLoader.getResourceFile("/day6/day6.txt").readLines()

private val arrowRegex = Regex("<|\\^|>|v")
private var currentMovementDirection = "^"
private val initialCoordinate: Coordinate = lines.find { it.contains(arrowRegex) }!!.let {
    lines.indexOf(it) to it.indexOf("^")
}
private lateinit var currentCoordinate: Coordinate
private var coordinatesMovedOver = mutableSetOf<Coordinate>()
private var willMoveOut: Boolean = false

// Only for part 2
// MutableList<Pair<String,Coordiante>>, where String is the `movementDirection` forcing the obstacle hit
private var foundObstaclePath: MutableSet<Pair<String, Coordinate>> =
    mutableSetOf()// reset after trying putting in an obstacle

private fun List<String>.isObsticaleInWay(): Boolean {
    var hit: Coordinate? = null
    when (currentMovementDirection) {
        "^" -> {
            if (currentCoordinate.first - 1 == -1) {
                willMoveOut = true
                return false
            };
            if (this[currentCoordinate.first - 1][currentCoordinate.second] == '#') {
                hit = Coordinate(currentCoordinate.first - 1, currentCoordinate.second)
            }
        }

        ">" -> {
            if (currentCoordinate.second + 1 == this[0].length) {
                willMoveOut = true
                return false
            }
            if (this[currentCoordinate.first][currentCoordinate.second + 1] == '#') {
                hit = Coordinate(currentCoordinate.first, currentCoordinate.second + 1)
            }
        }

        "v" -> {
            if (currentCoordinate.first + 1 == this.size) {
                willMoveOut = true
                return false
            }
            if (this[currentCoordinate.first + 1][currentCoordinate.second] == '#') {
                hit = Coordinate(currentCoordinate.first + 1, currentCoordinate.second)
            }
        }
        "<" -> {
            if (currentCoordinate.second - 1 == -1) {
                willMoveOut = true
                return false
            }
            if (this[currentCoordinate.first][currentCoordinate.second - 1] == '#') {
                hit = Coordinate(currentCoordinate.first, currentCoordinate.second - 1)
            }
        }

        else -> throw IllegalStateException("??????")
    }

    if (hit != null) {
        if (!foundObstaclePath.add(currentMovementDirection to hit)) { // found a cycle
            throw CycleDetectedException()
        }
    }

    return hit != null
}

private fun rotateGuard() {
    currentMovementDirection = when (currentMovementDirection) {
        "^" -> ">"
        ">" -> "v"
        "v" -> "<"
        "<" -> "^"
        else -> throw IllegalStateException("??????")
    }
}

private fun moveGuard() {
    coordinatesMovedOver.add(currentCoordinate)
    when (currentMovementDirection) {
        "^" -> {
            currentCoordinate = Coordinate(currentCoordinate.first - 1, currentCoordinate.second)
        }

        ">" -> {
            currentCoordinate = Coordinate(currentCoordinate.first, currentCoordinate.second + 1)
        }

        "v" -> {
            currentCoordinate = Coordinate(currentCoordinate.first + 1, currentCoordinate.second)
        }

        "<" -> {
            currentCoordinate = Coordinate(currentCoordinate.first, currentCoordinate.second - 1)
        }
    }
}

fun part1() {
    currentCoordinate = initialCoordinate
    while (!willMoveOut) {
        if (lines.isObsticaleInWay()) {
            rotateGuard()
        }
        moveGuard()
    }

    println(coordinatesMovedOver.size)
}

fun part2() {
    var count = 0

    for (i in lines.indices) {
        for (j in lines[0].indices) {
            foundObstaclePath = mutableSetOf()
            currentCoordinate = initialCoordinate
            currentMovementDirection = "^"
            coordinatesMovedOver = mutableSetOf()
            willMoveOut = false

            val linesCopy = buildList {
                lines.forEachIndexed { index, s ->
                    val string = if (index == i) {
                        s.replaceRange(startIndex = j, endIndex = j + 1, "#")
                    } else {
                        s
                    }
                    this@buildList.add(string)
                }
            }

            try {
                while (!willMoveOut) {
                    if (linesCopy.isObsticaleInWay()) {
                        rotateGuard()
                    }
                    moveGuard()
                }
            } catch (_: CycleDetectedException) {
                linesCopy.forEach {
                    println(it)
                }
                println("-----\n\n")
                count++
            }
        }
    }

    println(count)
}