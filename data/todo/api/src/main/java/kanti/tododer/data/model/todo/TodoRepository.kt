package kanti.tododer.data.model.todo

import kanti.tododer.data.model.ParentId

interface TodoRepository {

	suspend fun getChildren(parentId: ParentId): List<Todo>

	suspend fun create(
		parentId: ParentId,
		title: String,
		remark: String
	): Todo

	suspend fun updateTitle(todoId: Int, title: String): Todo

	suspend fun updateRemark(todoId: Int, remark: String): Todo

	suspend fun changeDone(todoId: Int): Todo

	suspend fun delete(todoIds: List<Int>)
}