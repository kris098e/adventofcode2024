package util

fun doIf(condition: Boolean, block: () -> Unit) {
    if (condition) block()
}

fun Char.isOneOf(vararg chars: Char): Boolean {
    return chars.contains(this)
}
