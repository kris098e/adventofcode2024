import util.ResourceLoader
import java.math.BigDecimal

object Day7 {
    private data class Day7(
        val result: BigDecimal,
        val input: List<BigDecimal>,
        var pipeOperatorAllowed: Boolean = false
    ) {

        fun d(): Boolean {
            val results = mutableListOf<BigDecimal>()
            dd(currentResult = input[0], currentInputIndex = 1, results = results)
            return results.any { it == result }
        }

        fun dd(currentInputIndex: Int, currentResult: BigDecimal, results: MutableList<BigDecimal>) {
            if (currentInputIndex == input.size) {
                results.add(currentResult)
                return
            }

            val plusResult = currentResult.plus(input[currentInputIndex])
            val timeResult = currentResult.multiply(input[currentInputIndex])

            dd(currentInputIndex + 1, plusResult, results)
            dd(currentInputIndex + 1, timeResult, results)
            if (pipeOperatorAllowed) {
                val pipeResult = currentResult.toString().plus(input[currentInputIndex]).toBigDecimal()
                dd(currentInputIndex + 1, pipeResult, results)
            }
        }
    }

    private val lines = ResourceLoader.getResourceFile("/day7/day7.txt").readLines().map {
        val split = it.split(":")
        val result = split[0].toBigDecimal()
        val input = split[1].split(" ")
            .filter { it.isNotBlank() }
            .map { it.toBigDecimal() }

        return@map Day7(result, input)
    }

    fun part1() {
        lines.filter { it.d() }.sumOf { it.result }.let(::println)
    }

    fun part2() {
        lines.also { it.forEach { it.pipeOperatorAllowed = true } }.filter { it.d() }.sumOf { it.result }.let(::println)
    }
}
