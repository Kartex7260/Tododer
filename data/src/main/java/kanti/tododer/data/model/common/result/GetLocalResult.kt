package kanti.tododer.data.model.common.result

sealed class GetLocalResult<T> {

	class Success<T>(
		val value: T
	) : GetLocalResult<T>()

	class NotFound<T>(
		val id: String
	) : GetLocalResult<T>()

	class Fail<T>(
		message: String?,
		val th: Throwable? = null
	) : GetLocalResult<T>() {
		val message: String = message ?: "[Not message]"
	}

	override fun toString(): String {
		return "RepositoryResult.${javaClass.simpleName}"
	}
}

val <T> GetLocalResult<T>.isSuccess: Boolean
	get() = this is GetLocalResult.Success

val <T> GetLocalResult<T>.isNotFound: Boolean
	get() = this is GetLocalResult.NotFound

val <T> GetLocalResult<T>.isFail: Boolean
	get() = this is GetLocalResult.Fail

val <T> GetLocalResult<T>.asRepositoryResult: GetRepositoryResult<T>
	get() {
	return when (this) {
		is GetLocalResult.Success -> {
			GetRepositoryResult.Success(value)
		}
		is GetLocalResult.NotFound -> {
			GetRepositoryResult.NotFound(id)
		}
		is GetLocalResult.Fail -> {
			GetRepositoryResult.Fail(message, th)
		}
	}
}
