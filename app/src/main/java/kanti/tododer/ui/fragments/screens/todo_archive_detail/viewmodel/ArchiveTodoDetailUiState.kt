package kanti.tododer.ui.fragments.screens.todo_archive_detail.viewmodel

import kanti.tododer.data.common.RepositoryResult
import kanti.tododer.data.model.common.Todo
import kanti.tododer.domain.common.TodoWithChildren
import kanti.tododer.ui.fragments.screens.todo_detail.viewmodel.TodoDetailUiState

data class ArchiveTodoDetailUiState(
	val todo: Todo? = null,
	val todoChildren: List<Todo> = listOf(),
	val process: Boolean = false,
	val type: Type = Type.SuccessLocal
) {

	sealed class Type(val type: String = "", val message: String? = null) {
		data object Empty : Type("Empty")
		data object SuccessLocal : Type("Success")
		//		class NoConnection(message: String? = null) : Type("NoConnection", message)
//		class NoAuthorization(message: String? = null) : Type("NoAuthorization", message)
		class NotFound(message: String? = null) : Type("NotFound", message)
		class AlreadyExists(
			val fullId: String? = null,
			message: String? = null
		) : Type(
			"AlreadyExists",
			"${if (fullId != null) "(Id: $fullId): " else ""}$message"
		)
		class InvalidFullId(
			val fullId: String? = null,
			message: String? = null
		) : Type(
			"InvalidFullId",
			"${if (fullId != null) "(Id: $fullId): " else ""}$message"
		)
		data object EmptyStack : Type("EmptyStack")
		class Fail(message: String? = null) : Type("Fail", message)

		override fun toString(): String {
			return "TodoDetailUiState.Type.$type"
		}
	}

	companion object {

		val Empty = ArchiveTodoDetailUiState(type = ArchiveTodoDetailUiState.Type.Empty)

	}

}

val RepositoryResult<TodoWithChildren>.toArchiveTodoDetailUiState: ArchiveTodoDetailUiState
	get() {
		val children = value?.children ?: listOf()
		return ArchiveTodoDetailUiState(
			todo = value?.todo,
			todoChildren = children,
			type = type.toArchiveTodoDetailType
		)
	}

private val RepositoryResult.Type.toArchiveTodoDetailType: ArchiveTodoDetailUiState.Type
	get() {
		return when (this) {
			is RepositoryResult.Type.SuccessLocal -> ArchiveTodoDetailUiState.Type.SuccessLocal
			is RepositoryResult.Type.NotFound -> ArchiveTodoDetailUiState.Type.NotFound(message)
			is RepositoryResult.Type.AlreadyExists -> ArchiveTodoDetailUiState.Type.AlreadyExists(
				fullId,
				message
			)
			else -> ArchiveTodoDetailUiState.Type.Fail(message)
		}
	}

val ArchiveTodoDetailUiState.isNull: Boolean
	get() = todo == null

val ArchiveTodoDetailUiState.isSuccess: Boolean
	get() = type is ArchiveTodoDetailUiState.Type.SuccessLocal

val ArchiveTodoDetailUiState.isNotFound: Boolean
	get() = type is ArchiveTodoDetailUiState.Type.NotFound

val ArchiveTodoDetailUiState.isAlreadyExists: Boolean
	get() = type is ArchiveTodoDetailUiState.Type.AlreadyExists

val ArchiveTodoDetailUiState.isInvalidFullId: Boolean
	get() = type is ArchiveTodoDetailUiState.Type.InvalidFullId

val ArchiveTodoDetailUiState.isEmptyStack: Boolean
	get() = type is ArchiveTodoDetailUiState.Type.EmptyStack

val ArchiveTodoDetailUiState.isFail: Boolean
	get() = type is ArchiveTodoDetailUiState.Type.Fail
