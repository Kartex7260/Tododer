package kanti.tododer.data.model.common.result

sealed class ResultUiState<T> {

	class Success<T>(
		val value: T
	) : ResultUiState<T>()

	class Process<T> : ResultUiState<T>()

	class Fail<T>(
		message: String?,
		val th: Throwable? = null
	) : ResultUiState<T>() {
		val message: String = message ?: "[Not message]"
	}

}

val <T> Result<T>.asUiState: ResultUiState<T> get() {
	return if (isSuccess) {
		ResultUiState.Success(getOrThrow())
	} else {
		val th = exceptionOrNull()
		ResultUiState.Fail(
			th?.message,
			th
		)
	}
}
