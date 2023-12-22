package kanti.tododer.data.model.todo.datasource.local

import kanti.tododer.data.model.ParentId
import kanti.tododer.data.model.todo.Todo
import kanti.tododer.data.model.todo.TodoState

interface TodoLocalDataSource {

	suspend fun getAllChildren(parentId: ParentId): List<Todo>

	suspend fun getChildren(parentId: ParentId, state: TodoState): List<Todo>

	suspend fun insert(todo: Todo): Todo

	suspend fun updateTitle(todoId: Int, title: String): Todo

	suspend fun updateRemark(todoId: Int, remark: String): Todo

	suspend fun changeDone(todoId: Int): Todo

	suspend fun delete(todoIds: List<Int>)
}