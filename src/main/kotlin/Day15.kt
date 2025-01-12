import util.*


private val tmp =
    ResourceLoader.getResourceFile("/day15/day15.txt").readText().split("\n\n").mapIndexed { index, s ->
        val lines = s.lines()
        if (index == 0) {
            buildMap {
                lines.forEachIndexed { y, line ->
                    line.forEachIndexed { x, c ->
                        put(Point2D(x = x, y = y), c)
                    }
                }
            }
        } else {
            lines.joinToString("")
        }
    }

@Suppress("UNCHECKED_CAST")
private val part1Map = (tmp[0] as Map<Point2D, Char>).toMutableMap()
private val moves = tmp[1] as String

/**
 * worth noting that I thought it would just be easier to start from scratch and reuse logic from part 1 to part 2
 */

object Day15Part1 {
    fun part1() {
        var robotLocation = part1Map.entries.find { it.value == '@' }!!.key
        val grid = Grid(
            upperX = part1Map.keys.maxOf { it.x },
            upperY = part1Map.keys.maxOf { it.y },
        )

        fun checkDirectionCanPush(direction: DIRECTION): Boolean {
            var directionPoint2D = direction.getDirectionPoint(robotLocation)
            while (part1Map[directionPoint2D] == 'O') {
                directionPoint2D = direction.getDirectionPoint(directionPoint2D)
            }
            return part1Map[directionPoint2D] == '.'
        }

        fun pushDirection(direction: DIRECTION) {
            var directionPoint2D = direction.getDirectionPoint(robotLocation)
            while (part1Map[directionPoint2D] == 'O') {
                val nextDirectionPoint2D = direction.getDirectionPoint(directionPoint2D)
                val nextPointChar = part1Map[nextDirectionPoint2D]
                doIf(nextPointChar != '#') {
                    part1Map[nextDirectionPoint2D] = 'O'
                }
                if (nextPointChar == '.') break
                directionPoint2D = nextDirectionPoint2D
            }
        }

        fun push(direction: DIRECTION) {
            if (checkDirectionCanPush(direction)) {
                pushDirection(direction)
                part1Map[robotLocation] = '.'
                robotLocation = direction.getDirectionPoint(robotLocation)
            }
        }

        fun printMap() {
            for (y in 0..grid.upperX) {
                for (x in 0..grid.upperY) {
                    if (robotLocation == Point2D(x, y)) {
                        print('@')
                    } else {
                        print(part1Map[Point2D(x, y)])
                    }
                }
                println()
            }
        }

        val rememberRobotLocation = robotLocation
        part1Map[robotLocation] = '.'
        moves.forEach { moveChar ->
            DIRECTION.entries.find { direction -> direction.char == moveChar }.run { push(this!!) }
        }

        part1Map.entries.sumOf { (point, c) ->
            if (c == 'O') point.y * 100 + point.x else 0
        }.let(::println)

        part1Map[rememberRobotLocation] = '@'
    }
}

object Day15Part2 {
    fun part2() {
        val part2Map = buildMap {
            part1Map.forEach { (point, c) ->
                when (c) {
                    '#' -> {
                        put(Point2D(point.x * 2, point.y), '#')
                        put(Point2D(point.x * 2 + 1, point.y), '#')
                    }

                    'O' -> {
                        put(Point2D(point.x * 2, point.y), '[')
                        put(Point2D(point.x * 2 + 1, point.y), ']')
                    }

                    '.' -> {
                        put(Point2D(point.x * 2, point.y), '.')
                        put(Point2D(point.x * 2 + 1, point.y), '.')
                    }

                    '@' -> {
                        put(Point2D(point.x * 2, point.y), '@')
                        put(Point2D(point.x * 2 + 1, point.y), '.')
                    }

                    else -> error("Unknown char")
                }
                Unit
            }
        }.toMutableMap()

        var robotLocation = part2Map.entries.find { it.value == '@' }!!.key
            .also { part2Map[it] = '.' }

        val grid = Grid(
            upperX = part2Map.keys.maxOf { it.x },
            upperY = part2Map.keys.maxOf { it.y },
        )

        fun printMap() {
            for (y in 0..grid.upperY) {
                for (x in 0..grid.upperX) {
                    if (robotLocation == Point2D(x, y)) {
                        print('@')
                    } else {
                        print(part2Map[Point2D(x, y)])
                    }
                }
                println()
            }
        }

        fun checkUpDownDirectionPush(direction: DIRECTION, currentLocation: Point2D): Boolean {
            val directionPoint2D = direction.getDirectionPoint(currentLocation)
            when (part2Map[directionPoint2D]) {
                '#' -> return false
                '[' -> {
                    val point2DOfCloseBracket = DIRECTION.RIGHT.getDirectionPoint(directionPoint2D)
                    return checkUpDownDirectionPush(direction, point2DOfCloseBracket)
                            && checkUpDownDirectionPush(direction, directionPoint2D)
                }

                ']' -> {
                    val point2DOfOpenBracket = DIRECTION.LEFT.getDirectionPoint(directionPoint2D)
                    return checkUpDownDirectionPush(direction, point2DOfOpenBracket)
                            && checkUpDownDirectionPush(direction, directionPoint2D)
                }

                else -> return true
            }
        }

        fun checkSideDirectionPush(direction: DIRECTION): Boolean {
            var directionPoint2D = robotLocation
            do {
                directionPoint2D = direction.getDirectionPoint(directionPoint2D)
            } while (part2Map[directionPoint2D]!!.isOneOf('[', ']'))
            return part2Map[directionPoint2D] == '.'
        }

        fun checkDirectionCanPush(direction: DIRECTION): Boolean {
            return when (direction) {
                DIRECTION.UP, DIRECTION.DOWN -> checkUpDownDirectionPush(direction, robotLocation)
                DIRECTION.RIGHT, DIRECTION.LEFT -> checkSideDirectionPush(direction)
            }
        }

        fun MutableMap<Point2D, Char>.pushUpDownDirection(direction: DIRECTION, currentLocation: Point2D) {
            val currentChar = part2Map[currentLocation]
            val closingBracketPoint2D = if (currentChar == '[') {
                DIRECTION.RIGHT.getDirectionPoint(currentLocation)
            } else {
                DIRECTION.LEFT.getDirectionPoint(currentLocation)
            }
            val closingBracketChar = part2Map[closingBracketPoint2D]

            part2Map[currentLocation] = '.'
            part2Map[closingBracketPoint2D] = '.'
            put(direction.getDirectionPoint(closingBracketPoint2D), closingBracketChar!!)
            put(direction.getDirectionPoint(currentLocation), currentChar!!)
            if (part2Map[direction.getDirectionPoint(closingBracketPoint2D)]!!.isOneOf('[', ']')) {
                pushUpDownDirection(direction, direction.getDirectionPoint(closingBracketPoint2D))
            }
            if (part2Map[direction.getDirectionPoint(currentLocation)]!!.isOneOf('[', ']')) {
                pushUpDownDirection(direction, direction.getDirectionPoint(currentLocation))
            }
        }

        fun pushSideDirection(direction: DIRECTION) {
            var directionPoint2D = direction.getDirectionPoint(robotLocation)
            val firstChar = part2Map[directionPoint2D]
            val chars = listOf('[', ']')
            var count = chars.indexOf(firstChar)
            while (true) {
                directionPoint2D = direction.getDirectionPoint(directionPoint2D)
                val nextChar = part2Map[directionPoint2D]
                part2Map[directionPoint2D] = chars[count % 2]
                count++

                if (nextChar!!.isOneOf('#', '.')) break
            }
        }


        fun pushDirection(direction: DIRECTION) {
            when (direction) {
                DIRECTION.UP, DIRECTION.DOWN -> {
                    buildMap {
                        pushUpDownDirection(direction, direction.getDirectionPoint(robotLocation))
                    }.entries.forEach { (point, c) ->
                        part2Map[point] = c
                    }
                }

                DIRECTION.RIGHT, DIRECTION.LEFT -> {
                    pushSideDirection(direction)
                }
            }
        }

        fun push(direction: DIRECTION) {
            if (!part2Map[direction.getDirectionPoint(robotLocation)]!!.isOneOf('[', ']')) {
                if (part2Map[direction.getDirectionPoint(robotLocation)] == '#') {
                    return
                } else {
                    part2Map[robotLocation] = '.'
                    robotLocation = direction.getDirectionPoint(robotLocation)
                }
            } else if (checkDirectionCanPush(direction)) {
                pushDirection(direction)
                part2Map[robotLocation] = '.'
                robotLocation = direction.getDirectionPoint(robotLocation)
            }
        }

        moves.forEach { moveChar ->
            DIRECTION.entries.find { direction -> direction.char == moveChar }.run { push(this!!) }
        }

        part2Map.entries.sumOf { (point, c) ->
            if (c == '[') point.y * 100 + point.x else 0
        }.let(::println)
    }
}
