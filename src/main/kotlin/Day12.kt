import util.DIRECTION
import util.Grid
import util.Point2D
import util.ResourceLoader

object Day12 {
    private val lines = ResourceLoader.useResourceFile("/day12/day12.txt") { lines ->
        buildMap {
            lines.forEachIndexed { y, row ->
                row.forEachIndexed { x, c ->
                    put(Point2D(x, y), c)
                }
            }
        }
    }

    private val grid = Grid(
        upperX = lines.maxOf { it.key.x },
        upperY = lines.maxOf { it.key.y },
    )

    private fun findRegionForPoint(point: Point2D, plant: Char, regionList: MutableSet<Pair<Point2D, Char>>) {
        grid.getNeighbours(point).forEach {
            if (lines[it] == plant && !regionList.contains(it to plant)) {
                regionList.add(it to plant)
                findRegionForPoint(it, plant, regionList)
            }
        }
    }

    private fun findRegions(): List<Set<Pair<Point2D, Char>>> {
        val regions = mutableListOf<MutableSet<Pair<Point2D, Char>>>()
        val pointsVisited = mutableSetOf<Pair<Point2D, Char>>()

        for (entry in lines.entries) {
            val (point, plant) = entry
            if (pointsVisited.contains(point to plant)) continue

            val regionList = mutableSetOf(point to plant)
            findRegionForPoint(point, plant, regionList)
            pointsVisited.addAll(regionList)
            regions.add(regionList)
        }

        return regions
    }

    fun part1() {
        val regions = findRegions()
        // now that we have all regions we calculate how many sides it has
        val perimeterToRegionSize = regions.map { region ->
            val perimeter = region.sumOf { (point, plant) ->
                val neighbours = grid.getNeighbours(point)
                // expected number of neighbours is 4, if less, its hugging the edge, or maybe its even a corner
                neighbours.count { lines[it] != plant } + (4 - neighbours.size)
            }

            perimeter to region.size
        }

        println(perimeterToRegionSize.sumOf { it.first * it.second })
    }

    /**
     * if you are seeing this, using flatMap and using map inside of the flatMap could
     * be replaced by sumOf and count and returning 1 inside the count for each matched condition would be simpler.
     * But a bit of debugging went into this
     */
    fun part2() {
        val regions = findRegions()

        regions.map { region ->
            region to region.flatMap { (point, c) ->

                // find corners
                listOf(DIRECTION.UP, DIRECTION.RIGHT, DIRECTION.DOWN, DIRECTION.LEFT, DIRECTION.UP)
                    .zipWithNext()
                    .map { (d1, d2) ->
                        val d1Point = d1.getDirectionPoint(point)
                        val d2Point = d2.getDirectionPoint(point)

                        val isDirectCorner = (lines[d1Point] != c || lines[d1Point] == null) && (lines[d2Point] != c || lines[d2Point] == null) // missing something about if the the point neighbours are not in the grid
                        if (isDirectCorner) return@map Triple(d1Point to d1, d2Point to d2, 1)

                        val upDownPoint = if (d1 == DIRECTION.UP || d1 == DIRECTION.DOWN) d1Point else d2Point
                        val sidePoint = if (d1 == DIRECTION.UP || d1 == DIRECTION.DOWN) d2Point else d1Point
                        val extraCornerCheck = Point2D(sidePoint.x, upDownPoint.y)
                        if(lines[upDownPoint] != c && lines[sidePoint] == c && lines[extraCornerCheck] == c) return@map Triple(d1Point to d1, d2Point to d2, 2)
                        else return@map Triple(d1Point to d1, d2Point to d2, 0)
                    }
            }
        }.fold(0) { acc, (region, corners) ->
            // println("region: $region, corners: ${corners.filter { it.third != 0 }}, numCorners: ${corners.filter { it.third != 0 }.size}")
            acc + region.size * corners.filter { it.third != 0 }.size
        }.let(::println)
    }
}
