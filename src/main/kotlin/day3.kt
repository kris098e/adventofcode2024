import util.ResourceLoader

object Day3 {
    private val line = ResourceLoader.getResourceFile("/day3.txt").readText()

    private val mulRegex = Regex("mul\\(\\d+,\\d+\\)")
    private val extractNumbersRegex = Regex("\\d+")

    fun part1() {
        val mulMatches = mulRegex.findAll(line)
        mulMatches.sumOf { match ->
            val numbers = extractNumbersRegex.findAll(match.value)
            numbers.map { it.value.toInt() }.reduce { acc, i -> acc * i }
        }
            .let(::println)
    }

    fun part2() {
        val doAndDontRegex = Regex("don't|do|$mulRegex")
        var enabled = true
        doAndDontRegex.findAll(line).sumOf { match ->
            if (match.value == "don't") {
                enabled = false
                return@sumOf 0
            }
            if (match.value == "do") {
                enabled = true
                return@sumOf 0
            }

            if (enabled) {
                val numbers = extractNumbersRegex.findAll(match.value)
                numbers.map { it.value.toInt() }.reduce { acc, i -> acc * i }
            } else {
                0
            }
        }
            .let(::println)
    }
}