package day10

import org.example.util.Grid
import org.example.util.Point2D
import org.example.util.ResourceLoader

// map of points to their value
private val lines = ResourceLoader.useResourceFile("/day10/day10.txt") { lines ->
    buildMap {
        lines.flatMapIndexed { y, row ->
            row.mapIndexed { x, char ->
                put(Point2D(x, y), char.digitToInt())
            }
        }
    }
}

private val grid = Grid(
    upperX = lines.maxOf { it.key.x },
    upperY = lines.maxOf { it.key.y },
)

private fun Map<Point2D, Int>.sorroundedByTill9(point: Point2D, pointValue: Int, point9sSeen: MutableCollection<Point2D>) {
    if (pointValue == 9) {
        point9sSeen.add(point)
        return
    }
    return grid.getNeighbours(point).filter {
        this[it]?.let { it == pointValue + 1 } ?: false
    }.forEach{
        sorroundedByTill9(it, pointValue + 1, point9sSeen)
    }
}

fun part1() {
    lines.filter { it.value == 0 }.map { (point, _) ->
        val point9sSeen = mutableSetOf<Point2D>()
        lines.sorroundedByTill9(point, 0, point9sSeen)
        return@map point9sSeen
    }.sumOf { it.size }.let(::println)
}

fun part2() {
    lines.filter { it.value == 0 }.map { (point, _) ->
        val point9sSeen = mutableListOf<Point2D>()
        lines.sorroundedByTill9(point, 0, point9sSeen)
        return@map point9sSeen
    }.sumOf { it.size }.let(::println)
}
