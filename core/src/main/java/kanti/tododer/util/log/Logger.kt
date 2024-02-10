package kanti.tododer.util.log

interface Logger {

    val isEnabled: Boolean

    fun enabled(enabled: Boolean)

    fun d(tag: String?, msg: String): Int
}