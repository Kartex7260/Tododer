package kanti.tododer.ui.screens.todo_detail.viewmodel

import kanti.tododer.data.common.RepositoryResult
import kanti.tododer.domain.plan.planwithchildren.PlanWithChildren
import kanti.tododer.domain.task.taskwithchildren.TaskWithChildren
import kanti.tododer.ui.state.TodoElement
import kanti.tododer.ui.state.toTodoElement

data class TodoDetailUiState(
	val todo: TodoElement? = null,
	val todoChildren: List<TodoElement> = listOf(),
	val process: Boolean = false,
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
		class InvalidFullId(message: String? = null) : Type("InvalidFullId", message)
		data object EmptyStack : Type("EmptyStack")
		class Fail(message: String? = null) : Type("Fail", message)

		override fun toString(): String {
			return "TodoDetailUiState.Type.$type"
		}
	}

}

val RepositoryResult<TaskWithChildren>.toTaskTodoDetailUiState: TodoDetailUiState
	get() {
		val todo = value?.task?.toTodoElement
		val children = value?.childTasks?.map { it.toTodoElement } ?: listOf()
		return TodoDetailUiState(
			todo = todo,
			todoChildren = children,
			type = type.toTodoDetailType
		)
	}

val RepositoryResult<PlanWithChildren>.toPlanTodoDetailUiState: TodoDetailUiState
	get() {
		val todo = value?.plan?.toTodoElement
		val childrenPlans = value?.childPlans?.map { it.toTodoElement } ?: listOf()
		val childrenTasks = value?.childTasks?.map { it.toTodoElement } ?: listOf()
		return TodoDetailUiState(
			todo = todo,
			todoChildren = childrenPlans + childrenTasks,
			type = type.toTodoDetailType
		)
	}

private val RepositoryResult.Type.toTodoDetailType: TodoDetailUiState.Type
	get() {
		return when (this) {
			is RepositoryResult.Type.Success -> TodoDetailUiState.Type.Success
			is RepositoryResult.Type.NotFound -> TodoDetailUiState.Type.NotFound(message)
			is RepositoryResult.Type.AlreadyExists -> TodoDetailUiState.Type.AlreadyExists(
				fullId,
				message
			)
			else -> TodoDetailUiState.Type.Fail(message)
		}
	}

val TodoDetailUiState.isNull: Boolean
	get() = todo == null

val TodoDetailUiState.isSuccess: Boolean
	get() = type is TodoDetailUiState.Type.Success

val TodoDetailUiState.isNotFound: Boolean
	get() = type is TodoDetailUiState.Type.NotFound

val TodoDetailUiState.isAlreadyExists: Boolean
	get() = type is TodoDetailUiState.Type.AlreadyExists

val TodoDetailUiState.isInvalidFullId: Boolean
	get() = type is TodoDetailUiState.Type.InvalidFullId

val TodoDetailUiState.isEmptyStack: Boolean
	get() = type is TodoDetailUiState.Type.EmptyStack

val TodoDetailUiState.isFail: Boolean
	get() = type is TodoDetailUiState.Type.Fail