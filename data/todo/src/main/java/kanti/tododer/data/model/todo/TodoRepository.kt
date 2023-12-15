package kanti.tododer.data.model.todo

import kanti.tododer.data.model.ParentId

interface TodoRepository {

	suspend fun getChildren(parentId: ParentId): List<Todo>

	suspend fun deleteChildren(parentId: ParentId)

	suspend fun create(
		parentId: ParentId,
		title: String,
		remark: String
	): Todo

	suspend fun updateTitle(todo: Todo, title: String): Todo

	suspend fun updateRemark(todo: Todo, remark: String): Todo

	suspend fun changeDone(todo: Todo): Todo

	suspend fun update(todos: List<Todo>)

	suspend fun delete(todos: List<Todo>)
}