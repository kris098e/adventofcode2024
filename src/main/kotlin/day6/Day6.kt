package day6

import org.example.util.ResourceLoader

typealias Coordinate = Pair<Int, Int> // Pair(Y,X)

val lines = ResourceLoader.getResourceFile("/day6/day6.txt").readLines()

private val arrowRegex = Regex("<|\\^|>|v")
private var currentMovementDirection = "^"
private lateinit var currentCoordinate: Coordinate
private val coordinatesMovedOver = mutableSetOf<Coordinate>()
private var willMoveOut: Boolean = false

private fun List<String>.isObsticaleInWay(): Boolean {
    return when(currentMovementDirection) {
        "^" -> {
            if (currentCoordinate.first - 1 == -1) {
                willMoveOut = true
                return false
            };
            lines[currentCoordinate.first - 1][currentCoordinate.second] == '#'
        }
        ">" -> {
            if (currentCoordinate.second + 1 == this[0].length) {
                willMoveOut = true
                return false
            }
            lines[currentCoordinate.first][currentCoordinate.second + 1] == '#'
        }
        "v" -> {
            if (currentCoordinate.first + 1 == this.size) {
                willMoveOut = true
                return false
            }
            lines[currentCoordinate.first + 1][currentCoordinate.second] == '#'
        }
        "<" -> {
            if (currentCoordinate.second - 1 == -1) {
                willMoveOut = true
                return  false
            }
            lines[currentCoordinate.first][currentCoordinate.second - 1] == '#'
        }
        else -> throw IllegalStateException("??????")
    }
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
    currentCoordinate = lines.find { it.contains(arrowRegex) }!!.let {
        lines.indexOf(it) to it.indexOf("^")
    }

    while (!willMoveOut) {
        if (lines.isObsticaleInWay()) {
            rotateGuard()
        }
        moveGuard()
    }

    println(coordinatesMovedOver.size)
}
