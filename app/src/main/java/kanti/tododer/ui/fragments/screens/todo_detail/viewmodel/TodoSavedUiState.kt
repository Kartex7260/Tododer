package kanti.tododer.ui.fragments.screens.todo_detail.viewmodel

import kanti.tododer.data.common.RepositoryResult
import kanti.tododer.data.model.common.Todo

data class TodoSavedUiState(
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

val TodoSavedUiState.isNull: Boolean get() {
	return todo == null
}

val TodoSavedUiState.isSuccess: Boolean get() {
	return type is TodoSavedUiState.Type.Success
}

val TodoSavedUiState.isNotFound: Boolean get() {
	return type is TodoSavedUiState.Type.NotFound
}

val TodoSavedUiState.isAlreadyExist: Boolean get() {
	return type is TodoSavedUiState.Type.AlreadyExists
}

val TodoSavedUiState.isNoTodoInstalled: Boolean get() {
	return type is TodoSavedUiState.Type.NoTodoInstalled
}

val TodoSavedUiState.isFail: Boolean get() {
	return type is TodoSavedUiState.Type.Fail
}

val RepositoryResult<out Todo>.toTodoSavedUiState: TodoSavedUiState get() {
	return TodoSavedUiState(
		todo = value,
		type = type.toNewTodoCreatedType
	)
}

val RepositoryResult.Type.toNewTodoCreatedType: TodoSavedUiState.Type get() {
	return when(this) {
		is RepositoryResult.Type.Success -> TodoSavedUiState.Type.Success
		is RepositoryResult.Type.NotFound -> TodoSavedUiState.Type.NotFound(message)
		is RepositoryResult.Type.AlreadyExists -> TodoSavedUiState.Type.AlreadyExists(fullId, message)
		is RepositoryResult.Type.Fail -> TodoSavedUiState.Type.Fail(message)
	}
}
