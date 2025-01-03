import util.Grid
import util.Point2D
import util.ResourceLoader

object Day10 {
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

    private fun solver(collectionFactory: () -> MutableCollection<Point2D>) {
        lines.filter { it.value == 0 }.map { (point, _) ->
            val collection = collectionFactory()
            lines.sorroundedByTill9(point, 0, collection)
            return@map collection
        }.sumOf { it.size }.let(::println)
    }

    fun part1() {
        solver { mutableSetOf() }
    }

    fun part2() {
        solver { mutableListOf() }
    }
    object Day10 {

    }
}
