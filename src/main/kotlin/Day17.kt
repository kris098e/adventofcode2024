import util.ResourceLoader

object Day17 {
    private data class State(
        var a: Long,
        var b: Long,
        var c: Long,
        val inputSequence: List<Int>,
        var instructionPointer: Int = 0,
        val result: MutableList<Int> = mutableListOf<Int>(),
    ) {

        val comboOperands = mapOf(4 to { a }, 5 to { b }, 6 to { c })

        private fun Map<Int, () -> Long>.getOrDefault(key: Int, default: Long): Long {
            return this[key]?.invoke() ?: default
        }

        fun nextInstruction(): Pair<Int, Int>? {
            if (instructionPointer >= inputSequence.lastIndex) {
                return null
            }

            val instruction = inputSequence[instructionPointer]
            val operand = inputSequence[instructionPointer + 1]

            instructionPointer += 2

            return instruction to operand
        }

        fun executeInstruction(instruction: Pair<Int, Int>) {
            val (upcode, operand) = instruction

            when (upcode) {
                0 -> a = a shr comboOperands.getOrDefault(operand, operand.toLong()).toInt()
                1 -> b = b xor operand.toLong()
                2 -> b = comboOperands.getOrDefault(operand, operand.toLong()).mod(8L)
                3 -> if (a != 0.toLong()) instructionPointer = operand
                4 -> b = b.xor(c)
                5 -> result.add(comboOperands.getOrDefault(operand, operand.toLong()).mod(8))
                6 -> b = a shr comboOperands.getOrDefault(operand, operand.toLong()).toInt()
                7 -> c = a shr comboOperands.getOrDefault(operand, operand.toLong()).toInt()
            }
        }
    }

    private val lines = ResourceLoader.useResourceFile("/day17/day17.txt") { lines ->
        val a = lines[0].substringAfter("A: ").toLong()
        val b = lines[1].substringAfter("B: ").toLong()
        val c = lines[2].substringAfter("C: ").toLong()

        val program = lines[4].substringAfter(" ").split(",").map { it.toInt() }

        State(a, b, c, program)
    }

    fun part1() {
        while(true) {
            val instruction = lines.nextInstruction() ?: break
            lines.executeInstruction(instruction)
        }

        println(lines.result.joinToString(","))
    }

    // We can the following pattern: first 8 numbers, lets say array Z
    // then we get 8 different numbers postfixed with Z[0]
    // then we get 8 different numbers postfixed with Z[1]
    // i.e this will repeat 8 times
    // See also for instance: counter: 8$, counter: 64%, counter: 512$, counter: 4096%
    // -- They all have the same pattern of prefixing with a new 4

    // So we could could run the program until we get the last result
    // then continue with the that number times 8 and do run here until we find the second to last result
    // then continue with that number times 8 and do run here until we find the third to last result

    // YES this works! see for instance:
    /**
     * counter: 4 is a 0
     * counter: 4*8 + 5 = 37 is 3,0
     * counter: 37*8 + 3 = 299 is 5,3,0
     * ...
     */
    fun part2() {
        val resultList = mutableListOf<Long>()
        part2Solver(0, 1, lines.inputSequence, lines.inputSequence.size, resultList)

        println(resultList.minOrNull())
    }

    fun part2Solver(currentA: Long, currentProgramLength: Int, inputSequence: List<Int>, finalLength: Int, results: MutableList<Long>) {
        for (i in currentA..currentA + 8) {
            val state = State(i, 0, 0, lines.inputSequence)
            while (true) {
                val instruction = state.nextInstruction() ?: break
                state.executeInstruction(instruction)
            }
            if (state.result == inputSequence.takeLast(currentProgramLength)) {
                if (currentProgramLength == finalLength) {
                    results.add(i)
                } else {
                    part2Solver(i shl 3, currentProgramLength + 1, inputSequence, finalLength, results)
                }
            }
        }
    }

   // fun part2Debug() {
   //     var state = State(117440, 0, 0, lines.inputSequence)
   //     val inputSequence = lines.inputSequence
   //     var counter = 0L

   //     while (state.result != inputSequence && counter < 8.0.pow(12)) {
   //         state = State(counter, 0, 0, inputSequence)
   //         while(true) {
   //             val instruction = state.nextInstruction() ?: break
   //             state.executeInstruction(instruction)
   //         }
   //         buildString {
   //             appendLine("A: ${state.a}")
   //             appendLine("B: ${state.b}")
   //             appendLine("C: ${state.c}")
   //             appendLine("Counter: $counter")
   //             appendLine("Counter mod 8: ${counter % 8}")
   //             appendLine("Result: ${state.result.joinToString(",")}")
   //             appendLine("----------------")
   //             appendLine()
   //         }.also {
   //             File("output.txt").appendText(it)
   //         }
   //         counter++
   //     }

   //     println(counter - 1)
   // }
}