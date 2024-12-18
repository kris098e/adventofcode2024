package org.example.util

data class Grid(
    val upperX: Int,
    val upperY: Int,
) {
    fun isPointWithinGrid(point2D: Point2D): Boolean {
        return point2D.x in 0..upperX && point2D.y in 0 .. upperY
    }

    fun isPointWithinGrid(x: Int, y: Int): Boolean {
        return x in 0..upperX && y in 0 .. upperY
    }

    fun ifPointWitinGrid(point2D: Point2D, block: () -> Unit) {
        if (isPointWithinGrid(point2D)) {
            block()
        }
    }

    fun getNeighbours(point2D: Point2D): List<Point2D> {
        return listOf(
            Point2D(point2D.x - 1, point2D.y),
            Point2D(point2D.x + 1, point2D.y),
            Point2D(point2D.x, point2D.y - 1),
            Point2D(point2D.x, point2D.y + 1),
        ).filter { isPointWithinGrid(it) }
    }
}
