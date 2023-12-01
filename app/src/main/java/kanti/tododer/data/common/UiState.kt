package kanti.tododer.data.common

typealias Type = RepositoryResult.Type

data class UiState<T>(
	val value: T,
	val process: Boolean = false,
	val type: Type = RepositoryResult.Type.Success
)

class UiStateProcess<T>(defValue: T) {

	var process: UiState<T>
		private set

	init {
		process = UiState(
			value = defValue,
			process = true
		)
	}
}

fun <T> RepositoryResult<T>.toUiState(defValue: T): UiState<T> {
	return UiState(
		value = value ?: defValue,
		type = type
	)
}

val <T> UiState<T>.isNull: Boolean
	get() = value == null

val <T> UiState<T>.isSuccess: Boolean
	get() = type is RepositoryResult.Type.Success

val <T> UiState<T>.isNotFound: Boolean
	get() = type is RepositoryResult.Type.NotFound

val <T> UiState<T>.isAlreadyExists: Boolean
	get() = type is RepositoryResult.Type.AlreadyExists

val <T> UiState<T>.isFail: Boolean
	get() = type is RepositoryResult.Type.Fail
