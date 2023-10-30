package kanti.tododer.ui.fragments.screens.todo_detail.viewmodel

import kanti.tododer.data.common.RepositoryResult
import kanti.tododer.data.common.UiState
import kanti.tododer.data.model.common.Todo

data class NewTodoCreatedUiState(
	val todo: Todo? = null,
	val type: Type = Type.Success
) {

	sealed class Type(val type: String = "", val message: String? = null) {
		data object Success : Type("Success")
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
		data object NoTodoInstalled : Type("NoTodoInstalled")
		class Fail(message: String? = null) : Type("Fail", message)

		override fun toString(): String {
			return "NewTodoCreatedUiState.Type.$type"
		}
	}

}

val NewTodoCreatedUiState.isNull: Boolean get() {
	return todo == null
}

val NewTodoCreatedUiState.isSuccess: Boolean get() {
	return type is NewTodoCreatedUiState.Type.Success
}

val NewTodoCreatedUiState.isNotFound: Boolean get() {
	return type is NewTodoCreatedUiState.Type.NotFound
}

val NewTodoCreatedUiState.isAlreadyExist: Boolean get() {
	return type is NewTodoCreatedUiState.Type.AlreadyExists
}

val NewTodoCreatedUiState.isNoTodoInstalled: Boolean get() {
	return type is NewTodoCreatedUiState.Type.NoTodoInstalled
}

val NewTodoCreatedUiState.isFail: Boolean get() {
	return type is NewTodoCreatedUiState.Type.Fail
}

val RepositoryResult<out Todo>.toNewTodoCreatedUiState: NewTodoCreatedUiState get() {
	return NewTodoCreatedUiState(
		todo = value,
		type = type.toNewTodoCreatedType
	)
}

val RepositoryResult.Type.toNewTodoCreatedType: NewTodoCreatedUiState.Type get() {
	return when(this) {
		is RepositoryResult.Type.Success -> NewTodoCreatedUiState.Type.Success
		is RepositoryResult.Type.NotFound -> NewTodoCreatedUiState.Type.NotFound(message)
		is RepositoryResult.Type.AlreadyExists -> NewTodoCreatedUiState.Type.AlreadyExists(fullId, message)
		is RepositoryResult.Type.Fail -> NewTodoCreatedUiState.Type.Fail(message)
	}
}
