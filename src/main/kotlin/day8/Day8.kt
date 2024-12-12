package day8

import org.example.util.Distance
import org.example.util.Grid
import org.example.util.Point2D
import org.example.util.ResourceLoader

private val input = ResourceLoader.getResourceFile(("/day8/day8.txt")).readLines()

private val formattedInput = input.flatMapIndexed { y, row ->
    row.mapIndexedNotNull { x, char ->
        if (char != '.') char to Point2D(x, y) else null
    }
}.groupBy({ it.first }, { it.second })

fun part1() {
    val seenPoints = mutableSetOf<Point2D>()
    val maxX = input[0].length - 1
    val maxY = input.size - 1

    val grid = Grid(
        upperX = maxX,
        upperY = maxY,
    )

    formattedInput.forEach { (_char, points2D) ->
        for (i in points2D.indices) {
            val lockedPoint2D = points2D[i]
            for (j in points2D.indices) {
                if (i == j) continue
                val point2D = points2D[j]
                val distance = lockedPoint2D.calculate2DDistanceTo(point2D)
                val doubleDistance = Distance(distance.x * 2, distance.y * 2)
                val newPoint = Point2D(lockedPoint2D.x + doubleDistance.x, lockedPoint2D.y + doubleDistance.y)
                grid.ifPointWitinGrid(newPoint) {
                    seenPoints.add(newPoint)
                }
            }
        }
    }
    println(seenPoints.size)
}

fun part2() {
    val seenPoints = mutableSetOf<Point2D>()
    val maxX = input[0].length - 1
    val maxY = input.size - 1

    val grid = Grid(
        upperX = maxX,
        upperY = maxY,
    )


    formattedInput.forEach { (_char, points2D) ->
        if (points2D.size == 1) {
            return@forEach
        }
        for (i in points2D.indices) {
            val lockedPoint2D = points2D[i]
            for (j in points2D.indices) {
                if (i == j) continue
                val point2D = points2D[j]
                val distance = lockedPoint2D.calculate2DDistanceTo(point2D)
                var count = 1
                do {
                    val newPoint2D = Point2D(
                        lockedPoint2D.x + (distance.x * count),
                        lockedPoint2D.y + (distance.y * count)
                    )
                    grid.ifPointWitinGrid(newPoint2D) {
                        seenPoints.add(newPoint2D)
                    }
                    count++
                } while (grid.isPointWithinGrid(newPoint2D))
            }
        }
    }
    println(seenPoints.size)
}
