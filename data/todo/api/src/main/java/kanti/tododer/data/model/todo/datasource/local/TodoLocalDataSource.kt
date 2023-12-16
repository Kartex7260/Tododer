package kanti.tododer.data.model.todo.datasource.local

import kanti.tododer.data.model.ParentId
import kanti.tododer.data.model.todo.Todo

interface TodoLocalDataSource {

	suspend fun getChildren(parentId: ParentId): List<Todo>

	suspend fun insert(todo: Todo): Todo

	suspend fun update(todo: Todo): Todo

	suspend fun update(todos: List<Todo>)

	suspend fun delete(todos: List<Todo>)
}