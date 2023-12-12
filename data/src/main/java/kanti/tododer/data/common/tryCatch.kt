package kanti.tododer.data.common

import kanti.tododer.data.model.common.result.GetLocalResult

suspend fun <T> todoLocalResultTryCatch(block: suspend () -> GetLocalResult<T>): GetLocalResult<T> {
	return try {
		block()
	} catch (th: Throwable) {
		GetLocalResult.Fail(th.message, th)
	}
}

suspend fun <T> todoResultTryCatch(block: suspend () -> Result<T>): Result<T> {
	return try {
		block()
	} catch (th: Throwable) {
		Result.failure(th)
	}
}

suspend fun resultTryCatch(block: suspend () -> Unit): Result<Unit> {
	return try {
		block()
		Result.success(Unit)
	} catch (th: Throwable) {
		Result.failure(th)
	}
}
