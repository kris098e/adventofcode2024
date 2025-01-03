import util.ResourceLoader
import kotlin.math.roundToInt

object Day5 {
    private data class RulesAndUpdates(
        val beforeToAfter: Map<Int, List<Int>>,
        val updates: List<List<Int>>,
    ) {
        fun getOrder(isCorrectOrdering: Boolean): List<List<Int>> {
            return rulesAndUpdates.updates.filter { update ->
                update.toMutableList().isCorrectOrdering(isCorrectOrdering)
            }
        }

        fun MutableList<Int>.isCorrectOrdering(isCorrectOrdering: Boolean): Boolean {
            val seen = mutableSetOf<Int>()

            this.forEach { num ->
                val mustBeAfter = beforeToAfter[num]
                if (mustBeAfter != null) { // there is a rule that `num` must be before some update
                    if (mustBeAfter.any { seen.contains(it) }) return !isCorrectOrdering
                }
                seen.add(num)
            }

            return isCorrectOrdering
        }
    }

    private val rulesAndUpdates = ResourceLoader.getResourceFile("/day5/day5.txt").useLines { input ->
        val rulesAndUpdates = input.asIterable().map { line ->
            line.split("|")
        }

        val rules = rulesAndUpdates.filter { it.size == 2 }.map { it[0].toInt() to it[1].toInt() }
        val lines = rulesAndUpdates.filter { it.size == 1 && it[0].isNotBlank() }
            .map { it[0] }
            .map { it.split(",") }
            .map { it.map { string -> string.toInt() } }

        val beforeToAfter = rules.fold(mutableMapOf<Int, List<Int>>()) { acc, pair ->
            val (mustBeBefore, mustBeAfter) = pair
            return@fold acc.also {
                acc[mustBeBefore] = acc.getOrDefault(mustBeBefore, emptySet()) + mustBeAfter
            }
        }

        return@useLines RulesAndUpdates(beforeToAfter, lines)
    }

    fun part1() {
        rulesAndUpdates.getOrder(isCorrectOrdering = true).sumOf { updates ->
            val middle = updates.size.div(2.0).roundToInt()
            return@sumOf updates[middle - 1] // 0 indexed
        }.let(::println)
    }

    private fun MutableList<Int>.sortByPushback(sortedTillIndex: Int, indexToPushBack: Int) {
        with(rulesAndUpdates) {
            val subList = this@sortByPushback.subList(0, sortedTillIndex + 1)
            if (subList.isCorrectOrdering(true)) return
            else {
                val tmp = this@sortByPushback[indexToPushBack - 1]
                this@sortByPushback[indexToPushBack - 1] = this@sortByPushback[indexToPushBack]
                this@sortByPushback[indexToPushBack] = tmp
                this@sortByPushback.sortByPushback(sortedTillIndex, indexToPushBack - 1)
            }
        }
    }

    fun part2() {
        val incorrectOrderUpdates = rulesAndUpdates.getOrder(isCorrectOrdering = false)

        val sortedIncorrects = incorrectOrderUpdates.map {
            val mutableUpdate = it.toMutableList()

            mutableUpdate.forEachIndexed { index, i ->
                mutableUpdate.sortByPushback(index, index)
            }

            return@map mutableUpdate
        }


        sortedIncorrects.sumOf { updates ->
            val middle = updates.size.div(2.0).roundToInt()
            return@sumOf updates[middle - 1]
        }.let(::println)
    }
}