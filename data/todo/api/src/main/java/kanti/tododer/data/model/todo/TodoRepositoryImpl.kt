package kanti.tododer.data.model.todo

import kanti.tododer.data.model.ParentId
import kanti.tododer.data.model.todo.datasource.local.TodoLocalDataSource
import javax.inject.Inject

class TodoRepositoryImpl @Inject constructor(
	private val localDataSource: TodoLocalDataSource
) : TodoRepository {

	override suspend fun getChildren(parentId: ParentId): List<Todo> {
		return localDataSource.getChildren(parentId, TodoState.Normal)
	}

	override suspend fun create(
		parentId: ParentId,
		title: String,
		remark: String
	): Todo {
		val todo = Todo(
			parentId = parentId,
			title = title,
			remark = remark
		)
		return localDataSource.insert(todo)
	}

	override suspend fun updateTitle(todo: Todo, title: String): Todo {
		return localDataSource.update(todo.toTodo(
			title = title
		))
	}

	override suspend fun updateRemark(todo: Todo, remark: String): Todo {
		return localDataSource.update(todo.toTodo(
			remark = remark
		))
	}

	override suspend fun changeDone(todo: Todo): Todo {
		val reversedDone = !todo.done
		return localDataSource.update(todo.toTodo(
			done = reversedDone
		))
	}

	override suspend fun update(todos: List<Todo>) {
		localDataSource.update(todos)
	}

	override suspend fun delete(todos: List<Todo>) {
		for (todo in todos) {
			val children = getChildren(todo.toParentId())
			delete(children)
		}
		localDataSource.delete(todos)
	}
}