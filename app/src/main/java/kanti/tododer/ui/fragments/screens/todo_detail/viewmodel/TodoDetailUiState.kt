package kanti.tododer.ui.fragments.screens.todo_detail.viewmodel

import kanti.tododer.data.model.common.result.GetRepositoryResult
import kanti.tododer.data.model.common.Todo
import kanti.tododer.domain.common.TodoWithChildren

sealed class TodoDetailUiState {

	class Success(
		val todo: kanti.tododer.data.model.common.Todo,
		val todoChildren: List<kanti.tododer.data.model.common.Todo> = listOf(),
	) : TodoDetailUiState()

	class NotFound(
		val id: String
	) : TodoDetailUiState()

	data object Empty : TodoDetailUiState()

	data object Process : TodoDetailUiState()

	data object InvalidFullId : TodoDetailUiState()

	data object EmptyStack : TodoDetailUiState()

	class Fail(
		message: String?,
		val throwable: Throwable? = null
	) : TodoDetailUiState() {
		val message: String = message ?: "[Not message]"
	}

}

val GetRepositoryResult<kanti.tododer.domain.common.TodoWithChildren>.toTodoDetailUiState: TodoDetailUiState get() {
	return when (this) {
		is GetRepositoryResult.Success -> {
			TodoDetailUiState.Success(value.todo, value.childTasks)
		}
		is GetRepositoryResult.NotFound -> {
			TodoDetailUiState.NotFound(id)
		}
		is GetRepositoryResult.Fail -> {
			TodoDetailUiState.Fail(message, th)
		}
	}
}

val TodoDetailUiState.asSuccess: TodoDetailUiState.Success? get() {
	if (this !is TodoDetailUiState.Success)
		return null
	return this
}

val TodoDetailUiState.asNotFound: TodoDetailUiState.NotFound? get() {
	if (this !is TodoDetailUiState.NotFound)
		return null
	return this
}

val TodoDetailUiState.isInvalidFullId: Boolean
	get() = this is TodoDetailUiState.InvalidFullId

val TodoDetailUiState.isEmptyStack: Boolean
	get() = this is TodoDetailUiState.EmptyStack

val TodoDetailUiState.asFail: TodoDetailUiState.Fail? get() {
	if (this !is TodoDetailUiState.Fail)
		return null
	return this
}
