package kanti.tododer.data.common

data class RepositoryResult<T>(
	val value: T? = null,
	val type: Type = Type.SuccessLocal
) {
	sealed class Type(val type: String = "", val message: String? = null) {
//		data object SuccessRemote : Type("SuccessRemote")
		data object SuccessLocal : Type("SuccessLocal")
//		class NoConnection(message: String? = null) : Type("NoConnection", message)
//		class NoAuthorization(message: String? = null) : Type("NoAuthorization", message)
		class NotFound(message: String? = null) : Type("NotFound", message)
		class AlreadyExists(
			val fullId: String? = null,
			message: String? = null
		) : Type(
			"AlreadyExists",
			"AlreadyExists${if (fullId != null) " (Id: $fullId): " else ": "}$message"
		)
		class Fail(message: String? = null) : Type("Fail", message)

		override fun toString(): String {
			return "RepositoryResult.Type.$type"
		}
	}
}

val <T> RepositoryResult<T>.isNull: Boolean
	get() = value == null

val <T> RepositoryResult<T>.isSuccess: Boolean
	get() = type is RepositoryResult.Type.SuccessLocal

val <T> RepositoryResult<T>.isNotFound: Boolean
	get() = type is RepositoryResult.Type.NotFound

val <T> RepositoryResult<T>.isAlreadyExists: Boolean
	get() = type is RepositoryResult.Type.AlreadyExists

val <T> RepositoryResult<T>.isFail: Boolean
	get() = type is RepositoryResult.Type.Fail
