package day9

import org.example.util.ResourceLoader

private val line = ResourceLoader.getResourceFile("/day9/day9.txt").readLines().first()

fun part1() {
    val fileBlocks = buildList { for (i in line.indices step 2) add(line[i].digitToInt()) }
    val emptySpaces = buildList { for (i in 1 until line.lastIndex step 2) add(line[i].digitToInt()) }

    val formattedInput = buildList {
        for (i in fileBlocks.indices) {
            for (j in 0 until fileBlocks[i]) add(i)
            if (i < emptySpaces.size) {
                for (j in 0 until emptySpaces[i]) add(null)
            }
        }
    }.toMutableList()

    var indexOfFirstNull = formattedInput.indexOf(null)
    for (i in formattedInput.size - 1 downTo 0) {
        if (indexOfFirstNull > i) break
        val tmp = formattedInput[i]
        formattedInput[i] = null
        formattedInput[indexOfFirstNull] = tmp
        indexOfFirstNull = formattedInput.indexOf(null)
    }

    formattedInput.foldIndexed(0L) { index, acc, value ->
        if (value == null) return@foldIndexed acc
        acc + value * index
    }.let(::println)
}

private fun List<Any?>.findRangesOfNulls(): List<Pair<Int, Int>> {
    val ranges = mutableListOf<Pair<Int, Int>>()
    var start = -1
    for (i in indices) {
        if (this[i] == null) {
            if (start == -1) start = i
        } else {
            if (start != -1) {
                ranges.add(start to i)
                start = -1
            }
        }
    }
    return ranges
}

fun part2() {
    val fileBlocks = buildList<Int?> { for (i in line.indices step 2) add(line[i].digitToInt()) }.toMutableList()
    val emptySpaces = buildList { for (i in 1 until line.lastIndex step 2) add(line[i].digitToInt()) }

    val formattedInput = buildList {
        for (i in fileBlocks.indices) {
            for (j in 0 until fileBlocks[i]!!) add(i)
            if (i < emptySpaces.size) {
                for (j in 0 until emptySpaces[i]) add(null)
            }
        }
    }.toMutableList()

    for (i in fileBlocks.lastIndex downTo 0) {
        val numSpaces = fileBlocks[i]
        val nullRanges = formattedInput.findRangesOfNulls()
        for (j in nullRanges) {
            val rangeSize = j.second - j.first
            if (j.first > formattedInput.indexOf(i)) break // the null range is after the current file block
            if (rangeSize >= numSpaces!!) {
                // clean up before moving
                for (k in formattedInput.indices) {
                    if (formattedInput[k] == i) {
                        formattedInput[k] = null
                    }
                }

                // move indices
                for (k in j.first until j.first + numSpaces) {
                    formattedInput[k] = i
                }
                fileBlocks[i] = null
                break
            }
        }
    }
    formattedInput.foldIndexed(0L) { index, acc, value ->
        if (value == null) return@foldIndexed acc
        acc + value * index
    }.let(::println)
}