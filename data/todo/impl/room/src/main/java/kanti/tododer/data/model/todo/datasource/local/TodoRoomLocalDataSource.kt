package kanti.tododer.data.model.todo.datasource.local

import kanti.tododer.data.model.ParentId
import kanti.tododer.data.model.todo.Todo
import kanti.tododer.data.room.todo.TodoDao
import javax.inject.Inject

class TodoRoomLocalDataSource @Inject constructor(
	private val todoDao: TodoDao
) : TodoLocalDataSource {

	override suspend fun getChildren(parentId: ParentId): List<Todo> {
		return todoDao.getChildren(parentId.toString()).map { it.toTodo() }
	}

	override suspend fun insert(todo: Todo): Todo {
		val rowId = todoDao.insert(todo.toTodoEntity())
		if (rowId == -1L)
			throw IllegalArgumentException("Todo already exist (id = ${todo.id})")
		return todoDao.getByRowId(rowId)?.toTodo()
			?: throw IllegalStateException("Not found todo by rowId=$rowId")
	}

	override suspend fun update(todo: Todo): Todo {
		val id = todo.id
		todoDao.update(listOf(todo.toTodoEntity()))
		return todoDao.getTodo(id)?.toTodo()
			?: throw IllegalStateException("Not found todo by id=$id")
	}

	override suspend fun update(todos: List<Todo>) {
		todoDao.update(todos.map { it.toTodoEntity() })
	}

	override suspend fun delete(todos: List<Todo>) {
		todoDao.delete(todos.map { it.toTodoEntity() })
	}
}