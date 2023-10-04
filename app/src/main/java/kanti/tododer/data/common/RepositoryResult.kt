package kanti.tododer.data.common

data class RepositoryResult<T>(
	val value: T? = null,
	val type: Type = Type.Success
) {
	sealed class Type(val message: String? = null) {
		data object Success : Type()
//		class NoConnection(message: String? = null) : Type(message)
//		class NoAuthorization(message: String? = null) : Type(message)
		class NotFound(message: String? = null) : Type(message)
		class Fail(message: String? = null) : Type(message)
	}
}