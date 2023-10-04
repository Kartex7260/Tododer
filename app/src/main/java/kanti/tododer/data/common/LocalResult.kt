package kanti.tododer.data.common

data class LocalResult<T>(
	val value: T? = null,
	val type: Type = Type.Success
) {
	sealed class Type(val message: String? = null) {
		data object Success : Type()
		class NotFound(message: String? = null) : Type(message)
		class Fail(message: String? = null) : Type(message)
	}
}

fun <T> LocalResult<T>.toRepositoryResult(): RepositoryResult<T> {
	return RepositoryResult(
		value,
		type.toRepositoryType()
	)
}

fun LocalResult.Type.toRepositoryType(): RepositoryResult.Type {
	return when (this) {
		is LocalResult.Type.Success -> RepositoryResult.Type.Success
		is LocalResult.Type.NotFound -> RepositoryResult.Type.NotFound(message)
		is LocalResult.Type.Fail -> RepositoryResult.Type.Fail(message)
	}
}