import util.ResourceLoader
import kotlin.math.abs


object Day2 {

    private val lines = ResourceLoader.getResourceFile("/day2.txt").useLines { input ->
        input.asIterable().map { line ->
            line.split(" ").map { it.toInt() }
        }
    }

    private fun List<Int>.checkIncreasing(allowedUnsafe: Boolean = false): Boolean {
        for (i in 0 until this.size - 1) {
            if (this[i] > this[i + 1] || this[i] == this[i + 1] || abs(this[i] - this[i + 1]) > 3) {
                if (allowedUnsafe) {
                    return this.toMutableList().also { it.removeAt(i) }.checkIncreasing() ||
                            this.toMutableList().also { it.removeAt(i + 1) }.checkIncreasing()
                }
                return false
            }
        }
        return true
    }

    private fun List<Int>.checkDecreasing(allowedUnsafe: Boolean = false): Boolean {
        for (i in 0 until this.size - 1) {
            if (this[i] < this[i + 1] || this[i] == this[i + 1] || abs(this[i] - this[i + 1]) > 3) {
                if (allowedUnsafe) {
                    return this.toMutableList().also { it.removeAt(i) }.checkDecreasing() ||
                            this.toMutableList().also { it.removeAt(i + 1) }.checkDecreasing()
                }
                return false
            }
        }
        return true
    }

    fun part1(allowUnsafe: Boolean = false) {
        lines.count { it.checkIncreasing(allowUnsafe) || it.checkDecreasing(allowUnsafe) }
            .let(::println)
    }

    fun part2() {
        part1(true)
    }
}
