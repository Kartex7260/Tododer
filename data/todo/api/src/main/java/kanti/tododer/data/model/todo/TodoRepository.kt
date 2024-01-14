package kanti.tododer.data.model.todo

import kanti.tododer.data.model.FullId

interface TodoRepository {

	suspend fun getChildren(fullId: FullId): List<Todo>

	suspend fun deleteChildren(fullId: FullId)

	suspend fun create(
		parentFullId: FullId,
		title: String,
		remark: String
	): Long

	suspend fun updateTitle(todoId: Long, title: String)

	suspend fun updateRemark(todoId: Long, remark: String)

	suspend fun changeDone(todoId: Long)

	suspend fun delete(todoIds: List<Long>)
}