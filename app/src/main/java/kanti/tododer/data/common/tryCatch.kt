package kanti.tododer.data.common

suspend fun <T> tryCatch(block: suspend () -> LocalResult<T>): LocalResult<T> {
	return try {
		block()
	} catch (th: Throwable) {
		LocalResult(type = LocalResult.Type.Fail(th.message))
	}
}