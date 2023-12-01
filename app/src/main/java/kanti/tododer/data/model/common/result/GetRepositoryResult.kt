package kanti.tododer.data.model.common.result

sealed class GetRepositoryResult<T> {
	class Success<T>(
		val value: T
	) : GetRepositoryResult<T>()

	class NotFound<T>(
		val id: String
	) : GetRepositoryResult<T>()

	class Fail<T>(
		message: String?,
		val th: Throwable? = null
	) : GetRepositoryResult<T>() {
		val message: String = message ?: "[Not message]"
	}

	override fun toString(): String {
		return "RepositoryResult.${javaClass.simpleName}"
	}
}


val <T> GetRepositoryResult<T>.asSuccess: GetRepositoryResult.Success<T>? get() {
	if (this !is GetRepositoryResult.Success)
		return null
	return this
}

val <T> GetRepositoryResult<T>.asNotFound: GetRepositoryResult.NotFound<T>? get() {
	if (this !is GetRepositoryResult.NotFound)
		return null
	return this
}

val <T> GetRepositoryResult<T>.asFail: GetRepositoryResult.Fail<T>? get() {
	if (this !is GetRepositoryResult.Fail)
		return null
	return this
}


val <T> GetRepositoryResult<T>.isSuccess: Boolean
	get() = this is GetRepositoryResult.Success

val <T> GetRepositoryResult<T>.isNotFound: Boolean
	get() = this is GetRepositoryResult.NotFound

val <T> GetRepositoryResult<T>.isFail: Boolean
	get() = this is GetRepositoryResult.Fail

fun <From, To> GetRepositoryResult<From>.toAnotherGenericType(
	converter: ((From) -> To)? = null
): GetRepositoryResult<To> {
	return when (this) {
		is GetRepositoryResult.Success -> {
			if (converter == null)
				throw IllegalStateException(
					"If the result is successful, the converter must not be null"
				)
			GetRepositoryResult.Success(converter(value))
		}
		is GetRepositoryResult.NotFound -> {
			return GetRepositoryResult.NotFound(id)
		}
		is GetRepositoryResult.Fail -> {
			return GetRepositoryResult.Fail(message, th)
		}

	}
}
