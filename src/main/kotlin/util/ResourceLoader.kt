package org.example.util

import java.io.File

object ResourceLoader {
    fun getResourceFile(path: String): File {
        return File(this::class.java.getResource(path)!!.file)
    }
}
