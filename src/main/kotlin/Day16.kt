import util.*
import java.util.PriorityQueue

object Day16 {
    private val lines = ResourceLoader.readIntoMapPoint2DChar("/day16/day16.txt")

    private val startPoint = lines.entries.find { it.value == 'S' }!!
    private val endPoint = lines.entries.find { it.value == 'E' }!!

    fun part1() = solver()
    fun part2() = solver()

    fun solver() {
        val queue = PriorityQueue<Node>(compareBy { it.score })
        queue.add(Node(startPoint.key, DIRECTION.RIGHT, 0, listOf(startPoint.key)))
        val scores = mutableMapOf<Pair<Point2D, DIRECTION>, Int>()
        val finalScores = mutableListOf<Int>()
        val paths = mutableMapOf<Int, MutableSet<Point2D>>()

        while (queue.isNotEmpty()) {
            val node = queue.poll()
            val currentScore = scores[node.point2D to node.direction] ?: Int.MAX_VALUE

            if (currentScore < node.score) {
                continue
            }

            if (node.point2D == endPoint.key) {
                finalScores.add(node.score)
                paths.getOrPut(node.score) { mutableSetOf() }.addAll(node.path)

                continue
            }

            scores[node.point2D to node.direction] = node.score

            val nodeStraight = Node(
                node.direction.getDirectionPoint(node.point2D),
                node.direction,
                node.score + 1,
                node.path + node.direction.getDirectionPoint(node.point2D),
            )

            val nodeRight = Node(
                node.direction.turnRight().getDirectionPoint(node.point2D),
                node.direction.turnRight(),
                node.score + 1001,
                node.path + node.direction.turnRight().getDirectionPoint(node.point2D),
            )

            val nodeLeft = Node(
                node.direction.turnLeft().getDirectionPoint(node.point2D),
                node.direction.turnLeft(),
                node.score + 1001,
                node.path + node.direction.turnLeft().getDirectionPoint(node.point2D),
            )

            for (nodeLoop in listOf(nodeLeft, nodeRight, nodeStraight)) {
                if (lines[nodeLoop.point2D] == '#') {
                    continue
                }

                queue.add(nodeLoop)
            }
        }

        val lowestScore = finalScores.minOrNull()
        println(lowestScore)
        println(paths[lowestScore]!!.size)
    }

    private data class Node(val point2D: Point2D, val direction: DIRECTION, val score: Int, val path: List<Point2D>)
}