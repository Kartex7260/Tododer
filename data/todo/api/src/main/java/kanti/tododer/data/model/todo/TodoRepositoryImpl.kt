package kanti.tododer.data.model.todo

import kanti.tododer.data.model.FullId
import kanti.tododer.data.model.todo.datasource.local.TodoLocalDataSource
import javax.inject.Inject

class TodoRepositoryImpl @Inject constructor(
	private val localDataSource: TodoLocalDataSource
) : TodoRepository {

	override suspend fun getChildren(fullId: FullId): List<Todo> {
		return localDataSource.getChildren(fullId, TodoState.Normal)
	}

	override suspend fun deleteChildren(fullId: FullId) {
		val children = localDataSource.getAllChildren(fullId)
		localDataSource.delete(children.map { it.id })
	}

	override suspend fun create(
		fullId: FullId,
		title: String,
		remark: String
	): Todo {
		val todo = Todo(
			parentId = fullId,
			title = title,
			remark = remark
		)
		return localDataSource.insert(todo)
	}

	override suspend fun updateTitle(todoId: Int, title: String): Todo {
		return localDataSource.updateTitle(todoId, title)
	}

	override suspend fun updateRemark(todoId: Int, remark: String): Todo {
		return localDataSource.updateRemark(todoId, remark)
	}

	override suspend fun changeDone(todoId: Int): Todo {
		return localDataSource.changeDone(todoId)
	}

	override suspend fun delete(todoIds: List<Int>) {
		localDataSource.delete(todoIds)
	}
}