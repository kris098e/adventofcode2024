import util.ResourceLoader
import kotlin.math.abs


object Day1 {

    private val linePairs = ResourceLoader.getResourceFile("/day1.txt").useLines { lines ->
        lines.asIterable().map {
            val tmp = it.split("   ")
            return@map tmp[0].toInt() to tmp[1].toInt()
        }
    }

    fun part1() {
        val lowestToHighest = linePairs.let {
            val lowestSorted = it.map { pair -> pair.first }.sortedBy { num -> num }
            val highestSorted = it.map { pair -> pair.second }.sortedBy { num -> num }

            return@let lowestSorted.zip(highestSorted)
        }

        val result = lowestToHighest.sumOf { pair -> abs(pair.second - pair.first) }
        println(result)
    }

    fun part2() {
        linePairs.sumOf { entry ->
            val left = entry.first
            val occurences = linePairs.count { it.second == left }
            left.times(occurences)
        }
    }
}
