package kanti.tododer.data.model.todo

import kanti.tododer.data.model.FullId

interface TodoRepository {

	suspend fun getChildren(fullId: FullId): List<Todo>

	suspend fun create(
		fullId: FullId,
		title: String,
		remark: String
	): Todo

	suspend fun updateTitle(todoId: Int, title: String): Todo

	suspend fun updateRemark(todoId: Int, remark: String): Todo

	suspend fun changeDone(todoId: Int): Todo

	suspend fun delete(todoIds: List<Int>)
}