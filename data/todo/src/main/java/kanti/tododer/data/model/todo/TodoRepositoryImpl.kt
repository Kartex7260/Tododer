package kanti.tododer.data.model.todo

import kanti.tododer.data.model.ParentId
import kanti.tododer.data.model.todo.datasource.local.TodoLocalDataSource
import javax.inject.Inject

class TodoRepositoryImpl @Inject constructor(
	private val localDataSource: TodoLocalDataSource
) : TodoRepository {

	override suspend fun getChildren(parentId: ParentId): List<Todo> {
		return localDataSource.getChildren(parentId)
	}

	override suspend fun deleteChildren(parentId: ParentId) {
		return localDataSource.deleteChildren(parentId)
	}

	override suspend fun insert(todo: Todo): Todo {
		return localDataSource.insert(todo)
	}

	override suspend fun update(todos: List<Todo>) {
		localDataSource.update(todos)
	}

	override suspend fun delete(todos: List<Todo>) {
		localDataSource.delete(todos)
	}
}