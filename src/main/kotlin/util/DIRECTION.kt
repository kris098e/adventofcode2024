package util

enum class DIRECTION(val char: Char) {
    UP('^'),
    RIGHT('>'),
    DOWN('v'),
    LEFT('<'),
    ;

    fun getDirectionPoint(point2D: Point2D): Point2D {
        return when (this) {
            UP -> Point2D(point2D.x, point2D.y - 1)
            RIGHT -> Point2D(point2D.x + 1, point2D.y)
            DOWN -> Point2D(point2D.x, point2D.y + 1)
            LEFT -> Point2D(point2D.x - 1, point2D.y)
        }
    }

    fun turnRight(): DIRECTION {
        return when (this) {
            UP -> RIGHT
            RIGHT -> DOWN
            DOWN -> LEFT
            LEFT -> UP
        }
    }

    fun turnLeft(): DIRECTION {
        return when (this) {
            UP -> LEFT
            RIGHT -> UP
            DOWN -> RIGHT
            LEFT -> DOWN
        }
    }

    companion object {
        fun getDirectionForPoint(point2D: Point2D, toPoint2D: Point2D): DIRECTION {
            return when {
                point2D.x == toPoint2D.x && point2D.y > toPoint2D.y -> UP
                point2D.x == toPoint2D.x && point2D.y < toPoint2D.y -> DOWN
                point2D.x > toPoint2D.x && point2D.y == toPoint2D.y -> LEFT
                point2D.x < toPoint2D.x && point2D.y == toPoint2D.y -> RIGHT
                else -> throw IllegalArgumentException("No direction found")
            }
        }
    }
}