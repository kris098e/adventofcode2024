import util.Point2D
import util.ResourceLoader

object Day14 {
    private data class MoveDirection(val x: Int, val y: Int)
    private data class Robot(var point: Point2D, val direction: MoveDirection)

    private val lines = ResourceLoader.useResourceFile("/day14/day14.txt") { lines ->
        lines.map { s ->
            val (p, v) = s.split(" ")
            val (x, y) = p.substringAfter("=").let { it.substringBefore(",").toInt() to it.substringAfter(",").toInt() }
            val (dx, dy) = v.substringAfter("=").let { it.substringBefore(",").toInt() to it.substringAfter(",").toInt() }

            Robot(Point2D(x, y), MoveDirection(dx, dy))
        }
    }

    private val spaceX = 101
    private val spaceY = 103

    private fun quadrantRobots(): Int {
        val topLeftQuadrant = lines.filter { it.point.x < spaceX / 2 && it.point.y < spaceY / 2 }
        val topRightQuadrant = lines.filter { it.point.x > spaceX / 2 && it.point.y < spaceY / 2 }
        val bottomLeftQuadrant = lines.filter { it.point.x < spaceX / 2 && it.point.y > spaceY / 2 }
        val bottomRightQuadrant = lines.filter { it.point.x > spaceX / 2 && it.point.y > spaceY / 2 }

        return topLeftQuadrant.size * topRightQuadrant.size * bottomLeftQuadrant.size * bottomRightQuadrant.size
    }

    fun part1() {
        //lines.forEach(::println)
        for (i in 0..99) {
            lines.forEach {
                var newX = it.point.x + it.direction.x
                var newY = it.point.y + it.direction.y

                if (newX < 0) newX += spaceX else newX %= spaceX
                if (newY < 0) newY += spaceY else newY %= spaceY
                it.point = Point2D(newX, newY)
            }
        }
        lines.forEach(::println)
        println(quadrantRobots())
    }

    // manually inspect the output, should be fine, nothing much is outputted since I require a 5 robots in a row
    // which does not happen as often.
    // My confirmed answer is 7858
    fun part2() {
        for (i in 1..10000) {
            lines.forEach {
                var newX = it.point.x + it.direction.x
                var newY = it.point.y + it.direction.y

                if (newX < 0) newX += spaceX else newX %= spaceX
                if (newY < 0) newY += spaceY else newY %= spaceY
                it.point = Point2D(newX, newY)
            }
            //if (i != 7858) continue

            var contains5InARow = false
            for (x in 0..<spaceX) {
                for (y in 0..<spaceY) {
                    if (buildList { for(k in y..y+5) add(Point2D(x, k)) }.all { p -> lines.any{ it.point == p } }) {
                        contains5InARow = true
                        break
                    }
                }
            }

            if (contains5InARow) {
                println("after $i seconds")
                for (x in 0..<spaceX) {
                    for (y in 0..<spaceY) {
                        val point = Point2D(x, y)
                        if (lines.any { it.point == point }) {
                            print("#")
                        } else {
                            print(".")
                        }
                    }
                    println()
                }
            }
        }
    }
}