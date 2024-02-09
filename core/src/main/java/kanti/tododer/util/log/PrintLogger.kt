package kanti.tododer.util.log

import javax.inject.Inject
import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class PrintLog

class PrintLogger @Inject constructor() : Logger {

    override fun d(tag: String?, msg: String): Int {
        println("DEBUG: $tag $msg")
        return 0
    }
}