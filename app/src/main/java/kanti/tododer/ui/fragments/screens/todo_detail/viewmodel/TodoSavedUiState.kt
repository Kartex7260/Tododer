package kanti.tododer.ui.fragments.screens.todo_detail.viewmodel

import kanti.tododer.data.model.common.Todo

sealed class TodoSavedUiState {

	class Success(
		val todo: kanti.tododer.data.model.common.Todo
	) : TodoSavedUiState()

	data object NoTodoInstalled : TodoSavedUiState()

	class Fail(
		message: String?,
		val th: Throwable? = null
	) : TodoSavedUiState() {
		val message: String = message ?: "[Not message]"
	}

}

val TodoSavedUiState.asSuccess: TodoSavedUiState.Success? get() {
	if (this !is TodoSavedUiState.Success)
		return null
	return this
}

val TodoSavedUiState.asFail: TodoSavedUiState.Fail? get() {
	if (this !is TodoSavedUiState.Fail)
		return null
	return this
}

val Result<kanti.tododer.data.model.common.Todo>.toTodoSavedUiState: TodoSavedUiState get() {
	return if (isSuccess) {
		TodoSavedUiState.Success(getOrThrow())
	} else {
		val th = exceptionOrNull()
		TodoSavedUiState.Fail(th?.message, th)
	}
}
