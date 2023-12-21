package kanti.tododer.data.model.todo.datasource.local

import kanti.sl.StateLanguage
import kanti.tododer.data.model.ParentId
import kanti.tododer.data.model.todo.Todo
import kanti.tododer.data.model.todo.TodoState
import kanti.tododer.data.room.todo.TodoDao
import javax.inject.Inject

class TodoRoomDataSource @Inject constructor(
	private val todoDao: TodoDao,
	private val sl: StateLanguage
) : TodoLocalDataSource {

	override suspend fun getChildren(parentId: ParentId, state: TodoState): List<Todo> {
		return todoDao.getChildren(parentId.toString(), state.name).map { it.toTodo(sl) }
	}

	override suspend fun insert(todo: Todo): Todo {
		val rowId = todoDao.insert(todo.toTodoEntity(sl))
		if (rowId == -1L)
			throw IllegalArgumentException("Todo already exist (id = ${todo.id})")
		return todoDao.getByRowId(rowId)?.toTodo(sl)
			?: throw IllegalStateException("Not found todo by rowId=$rowId")
	}

	override suspend fun update(todo: Todo): Todo {
		val id = todo.id
		todoDao.update(listOf(todo.toTodoEntity(sl)))
		return todoDao.getTodo(id)?.toTodo(sl)
			?: throw IllegalArgumentException("Not found todo by id=$id")
	}

	override suspend fun update(todos: List<Todo>) {
		todoDao.update(todos.map { it.toTodoEntity(sl) })
	}

	override suspend fun delete(todos: List<Todo>) {
		todoDao.delete(todos.map { it.toTodoEntity(sl) })
	}
}