package kanti.tododer.data.model.todo

import kanti.tododer.data.model.ParentId

interface TodoRepository {

	suspend fun getChildren(parentId: ParentId): List<Todo>

	suspend fun deleteChildren(parentId: ParentId)

	suspend fun insert(todo: Todo): Todo

	suspend fun update(vararg todo: Todo)

	suspend fun delete(vararg todo: Todo)
}