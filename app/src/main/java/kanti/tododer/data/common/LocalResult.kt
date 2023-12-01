package kanti.tododer.data.common

data class LocalResult<T>(
	val value: T? = null,
	val type: Type = Type.Success
) {
	sealed class Type(val type: String = "", val message: String? = null) {
		data object Success : Type("Success")
		class NotFound(message: String? = null) : Type("NotFound", message)
		class AlreadyExists(
			val fullId: String? = null,
			message: String? = null
		) : Type(
			"AlreadyExists",
			"AlreadyExists${if (fullId != null) " (Id: $fullId): " else ": "}$message"
		)
		class Fail(message: String? = null) : Type("Fail", message)
	}
}

val <T> LocalResult<T>.isNull: Boolean
	get() = value == null

val <T> LocalResult<T>.isSuccess: Boolean
	get() = type is LocalResult.Type.Success

val <T> LocalResult<T>.isNotFound: Boolean
	get() = type is LocalResult.Type.NotFound

val <T> LocalResult<T>.isAlreadyExists: Boolean
	get() = type is LocalResult.Type.AlreadyExists

val <T> LocalResult<T>.isNFail: Boolean
	get() = type is LocalResult.Type.Fail

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
		is LocalResult.Type.AlreadyExists -> RepositoryResult.Type.AlreadyExists(fullId, message)
		is LocalResult.Type.Fail -> RepositoryResult.Type.Fail(message)
	}
}