package kanti.tododer.data.model.common.result

sealed class GetUiState<T> {

	class Success<T>(
		val value: T
	) : GetUiState<T>()

	class Process<T> : GetUiState<T>()

	class NotFound<T>(
		val id: String
	) : GetUiState<T>()

	class Fail<T>(
		message: String?,
		val th: Throwable? = null
	) : GetUiState<T>() {
		val message: String = message ?: "[Not message]"
	}

	override fun toString(): String {
		return "UiState.${javaClass.simpleName}"
	}

}

val <T> GetRepositoryResult<T>.asUiState: GetUiState<T>
	get() {
	return when (this) {
		is GetRepositoryResult.Success -> {
			GetUiState.Success(value)
		}
		is GetRepositoryResult.NotFound -> {
			GetUiState.NotFound(id)
		}
		is GetRepositoryResult.Fail -> {
			GetUiState.Fail(message, th)
		}
	}
}

val <T> GetUiState<T>.isSuccess: Boolean
	get() = this is GetUiState.Success

val <T> GetUiState<T>.isNotFound: Boolean
	get() = this is GetUiState.NotFound

val <T> GetUiState<T>.isFail: Boolean
	get() = this is GetUiState.Fail
