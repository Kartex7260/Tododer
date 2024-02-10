package kanti.tododer.util.log

abstract class BaseLogger : Logger {

    final override var isEnabled: Boolean = true
        private set

    override fun enabled(enabled: Boolean) {
        isEnabled = enabled
    }

    override fun d(tag: String?, msg: String): Int {
        if (isEnabled) {
            return baseD(tag, msg)
        }
        return -1
    }

    protected abstract fun baseD(tag: String?, msg: String): Int
}