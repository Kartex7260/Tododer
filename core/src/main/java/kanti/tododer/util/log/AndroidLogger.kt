package kanti.tododer.util.log

import android.util.Log
import javax.inject.Inject
import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class StandardLog

class AndroidLogger @Inject constructor() : BaseLogger() {

    override fun baseD(tag: String?, msg: String): Int {
        return Log.d(tag, msg)
    }

    override fun baseE(tag: String?, msg: String): Int {
        return Log.e(tag, msg)
    }

    override fun baseE(tag: String?, msg: String?, th: Throwable?): Int {
        return Log.e(tag, msg, th)
    }
}