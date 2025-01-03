package util

import kotlin.math.abs

data class Point2D(val x: Int, val y: Int) {
    fun calculate2DDistanceTo(point: Point2D): Distance {
        return Distance(point.x - x, point.y - y)
    }

    fun calculateManhattanDistanceTo(point: Point2D): Int {
        return abs(x - point.x) + abs(y - point.y)
    }

    override fun equals(other: Any?): Boolean {
        if (other !is Point2D) return false
        return x == other.x && y == other.y
    }

    override fun hashCode(): Int {
        var result = x
        result = 31 * result + y
        return result
    }
}

data class Distance(val x: Int, val y: Int)
