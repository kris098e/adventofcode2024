import util.ResourceLoader

object Day13 {
    private data class XYValues(val x: Long, val y: Long)
    private data class Game(val buttonA: XYValues, val buttonB: XYValues, val target: XYValues)

    private val games = ResourceLoader.useResourceFile("/day13/day13.txt") { lines ->
        val subtractNums = Regex("\\d+")
        buildList {
            lines.filter { it.isNotBlank() }
                .windowed(3, 3, true) { window ->
                    val (a, b, c) = window
                    val (aX, aY) = subtractNums.findAll(a).map { it.value.toLong() }.toList()
                    val (bX, bY) = subtractNums.findAll(b).map { it.value.toLong() }.toList()
                    val (cX, cY) = subtractNums.findAll(c).map { it.value.toLong() }.toList()
                    add(Game(XYValues(aX, aY), XYValues(bX, bY), XYValues(cX, cY)))
                }
        }
    }

    fun part1() {
        val minPrices = mutableListOf<Int>()
        for (game in games) {
            val (buttonA, buttonB, target) = game
            val gamePrices = buildList<Int> {
                for (a in 100 downTo 1) {
                    for (b in 100 downTo 1) {
                        val x = buttonA.x * a + buttonB.x * b
                        val y = buttonA.y * a + buttonB.y * b
                        if (x == target.x && y == target.y) {
                            add(a*3+b)
                        }
                    }
                }
            }
            gamePrices.minOrNull()?.let { minPrices.add(it) }
        }

        println(minPrices.sum())
    }

    // cramers rule
    fun part2() {
        val newGames = buildList {
            games.forEachIndexed { index, it ->
                add(Game(it.buttonA, it.buttonB, XYValues(it.target.x + 10_000_000_000_000, it.target.y + 10_000_000_000_000)))
            }
        }

        buildList {
            for (game in newGames) {
                val (buttonA, buttonB, target) = game

                val determinant = buttonA.x * buttonB.y - buttonA.y * buttonB.x
                if (determinant == 0L) continue

                val a = (target.x * buttonB.y - target.y * buttonB.x) / determinant
                val b = (buttonA.x * target.y - buttonA.y * target.x) / determinant

                if (a * buttonA.x + b * buttonB.x != target.x || a * buttonA.y + b * buttonB.y != target.y) continue
                add(a*3+b)
            }
        }.let { println(it.sum()) }
    }
}
