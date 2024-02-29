package kanti.tododer.data.model.todo

import kanti.tododer.data.model.FullId

interface TodoRepository {

	suspend fun getTodo(todoId: Long): Todo?

	suspend fun getChildren(fullId: FullId, state: TodoState? = TodoState.Normal): List<Todo>

	suspend fun getChildrenCount(fullId: FullId, state: TodoState? = TodoState.Normal): Long

	suspend fun deleteChildren(fullId: FullId)

	suspend fun exists(todoId: Long): Boolean

	suspend fun create(
		parentFullId: FullId,
		title: String,
		remark: String
	): Long

	suspend fun updateTitle(todoId: Long, title: String)

	suspend fun updateRemark(todoId: Long, remark: String)

	suspend fun changeDone(todoId: Long, isDone: Boolean)

	suspend fun changeDone(todoIds: List<Long>, isDone: Boolean)

	suspend fun delete(todoIds: List<Long>)

	suspend fun deleteIfNameIsEmptyAndNoChild(todoId: Long): Boolean
}