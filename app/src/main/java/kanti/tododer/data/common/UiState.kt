package kanti.tododer.data.common

typealias Type = RepositoryResult.Type

data class UiState<T>(
	val value: T,
	val process: Boolean = false,
	val type: Type = RepositoryResult.Type.Success
)

class DefaultUiStateHolder<T>(defValue: T) {

	private val process: UiState<T>

	init {
		process = UiState(
			value = defValue,
			process = true
		)
	}

	val Process: UiState<T>
		get() = process
}

fun <T> RepositoryResult<T>.toUiState(defValue: T): UiState<T> {
	return UiState(
		value = value ?: defValue,
		type = type
	)
}