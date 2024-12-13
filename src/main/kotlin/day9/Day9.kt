package day9

import org.example.util.ResourceLoader

private val line = ResourceLoader.getResourceFile("/day9/day9.txt").readLines().first()

fun part1() {
    val fileBlocks = buildList { for (i in line.indices step 2) add(line[i].digitToInt()) }
    val emptySpaces = buildList { for (i in 1 until line.lastIndex step 2) add(line[i].digitToInt()) }

    val formattedInput = buildMap<Int, Int?> {
        var indexCount = 0
        for (i in fileBlocks.indices) {
            for (j in 0 until fileBlocks[i]) {
                put(indexCount, i)
                indexCount++
            }
            if (i < emptySpaces.size) {
                for (j in 0 until emptySpaces[i]) {
                    put(indexCount, null)
                    indexCount++
                }
            }
        }
    }.toMutableMap()

    var indexOfFirstNull = formattedInput.values.indexOf(null)
    for (i in formattedInput.values.size - 1 downTo 0) {
        if (indexOfFirstNull > i) break
        val tmp = formattedInput[i]
        formattedInput[i] = null
        formattedInput[indexOfFirstNull] = tmp
        indexOfFirstNull = formattedInput.values.indexOf(null)
    }

    formattedInput.values.foldIndexed(0L) { index, acc, value ->
        if (value == null) return@foldIndexed acc
        acc +  value * index
    }.let(::println)
}

