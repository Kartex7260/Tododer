package kanti.tododer.data.common

import android.util.Log

private const val LOG_TAG = "tryCatch"

suspend fun <T> localTryCatch(block: suspend () -> LocalResult<T>): LocalResult<T> {
	return try {
		block()
	} catch (th: Throwable) {
		Log.w(LOG_TAG, th.message, th)
		LocalResult(type = LocalResult.Type.Fail(th.message))
	}
}