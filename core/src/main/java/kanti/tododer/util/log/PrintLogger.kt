package kanti.tododer.util.log

import javax.inject.Inject
import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class PrintLog

class PrintLogger @Inject constructor() : BaseLogger() {

    override fun baseD(tag: String?, msg: String): Int {
        println("DEBUG: $tag $msg")
        return 0
    }

    override fun baseE(tag: String?, msg: String): Int {
        println("ERROR: $tag $msg")
        return 0
    }

    override fun baseE(tag: String?, msg: String?, th: Throwable?): Int {
        println("ERROR: $tag, $msg")
        th?.printStackTrace()
        return 0
    }
}