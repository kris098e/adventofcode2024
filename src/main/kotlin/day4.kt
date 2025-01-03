import util.ResourceLoader

object Day4 {
    private val lines = ResourceLoader.getResourceFile("/day4/day4.txt").readLines()

    private fun List<String>.checkWordXmas(startY: Int, startX: Int, dx: Int, dy: Int, target: String): Boolean {
        if (startY + dy * 3 !in indices || startX + dx * 3 !in this[0].indices) return false

        return target == buildString {
            append(this@checkWordXmas[startY][startX])
            append(this@checkWordXmas[startY + dy][startX + dx])
            append(this@checkWordXmas[startY + dy * 2][startX + dx * 2])
            append(this@checkWordXmas[startY + dy * 3][startX + dx * 3])
        }
    }


    fun part1() {
        var count = 0
        val word = "XMAS"
        val grid = listOf(
            Pair(1, 0),
            Pair(-1, 0),
            Pair(0, 1),
            Pair(0, -1),
            Pair(1, 1),
            Pair(-1, -1),
            Pair(1, -1),
            Pair(-1, 1)
        )

        for (y in lines.indices) {
            for (x in lines[y].indices) {
                if (lines[y][x] == 'X') {

                    count += grid.count { (dx, dy) ->
                        lines.checkWordXmas(y, x, dx, dy, word)
                    }
                }
            }
        }

        println(count)
    }

    private fun List<String>.checkWordMas(startX: Int, startY: Int, coordinates: Pair<Pair<Int, Int>, Pair<Int, Int>>, target: String): Boolean {
        val (firstPair, secondPair) = coordinates
        val (dx1, dy1) = firstPair
        val (dx2, dy2) = secondPair
        if (startY + dy1 !in indices || startX + dx1 !in this[0].indices) return false
        if (startY + dy2 !in indices || startX + dx2 !in this[0].indices) return false

        return target == buildString {
            append(this@checkWordMas[startY + dy1][startX + dx1])
            append(this@checkWordMas[startY][startX])
            append(this@checkWordMas[startY + dy2][startX + dx2])
        }
    }

    fun part2() {
        var count = 0
        val word = "MAS"
        val backwardsWord = "SAM"
        val grid = listOf(
            Pair(Pair(1,1), Pair(-1, -1)),
            Pair(Pair(-1, 1), Pair(1, -1)),
        )

        for (y in lines.indices) {
            for (x in lines[y].indices) {
                if (lines[y][x] == 'A') {
                    if ((lines.checkWordMas(x, y, grid[0], target = word) || lines.checkWordMas(x, y, grid[0], target = backwardsWord))
                        && (lines.checkWordMas(x, y, grid[1], target = word) || lines.checkWordMas(x, y, grid[1], target = backwardsWord))) {
                        count++
                    }
                }
            }
        }

        println(count)
    }
}