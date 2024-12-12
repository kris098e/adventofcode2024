package org.example.util

import kotlin.math.abs

data class Point2D(val x: Int, val y: Int) {
    fun calculate2DDistanceTo(point: Point2D): Distance {
        return Distance(point.x - x, point.y - y)
    }

    fun calculateManhattanDistanceTo(point: Point2D): Int {
        return abs(x - point.x) + abs(y - point.y)
    }
}

data class Distance(val x: Int, val y: Int)
