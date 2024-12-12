package org.example.util

data class Grid(
    val upperX: Int,
    val upperY: Int,
) {
    fun isPointWithinGrid(point2D: Point2D): Boolean {
        return point2D.x in 0 ..upperX && point2D.y in 0 .. upperY
    }

    fun ifPointWithGrid(point2D: Point2D, block: () -> Unit) {
        if (isPointWithinGrid(point2D)) {
            block()
        }
    }
}
