package day12

import org.example.util.Grid
import org.example.util.Point2D
import org.example.util.ResourceLoader

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

fun part2() {
    val regions = findRegions()
    val sides = regions.map { region ->
        val seenX = mutableSetOf<Int>()
        val seenY = mutableSetOf<Int>()
    }
}
