package util

enum class DIRECTION {
    UP,
    RIGHT,
    DOWN,
    LEFT,
    ;

    fun getDirectionPoint(point2D: Point2D): Point2D {
        return when (this) {
            UP -> Point2D(point2D.x, point2D.y - 1)
            RIGHT -> Point2D(point2D.x + 1, point2D.y)
            DOWN -> Point2D(point2D.x, point2D.y + 1)
            LEFT -> Point2D(point2D.x - 1, point2D.y)
        }
    }
}