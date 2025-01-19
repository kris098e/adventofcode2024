package util

import java.io.File

object ResourceLoader {
    fun getResourceFile(path: String): File {
        return File(this::class.java.getResource(path)!!.file)
    }

    fun <R> useResourceFile(path: String, block: (List<String>) -> R): R {
        return block(getResourceFile(path).readLines())
    }

    fun readIntoMapPoint2DChar(path: String): Map<Point2D, Char> {
        return useResourceFile(path) { lines ->
            lines.mapIndexed { y, line ->
                line.mapIndexed { x, char ->
                    Point2D(x, y) to char
                }
            }.flatten().toMap()
        }
    }
}
