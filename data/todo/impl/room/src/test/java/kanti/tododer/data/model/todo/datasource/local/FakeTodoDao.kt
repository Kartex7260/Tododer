package kanti.tododer.data.model.todo.datasource.local

import kanti.tododer.data.room.todo.TodoDao
import kanti.tododer.data.room.todo.TodoEntity

class FakeTodoDao(
	private val todos: MutableMap<Int, TodoEntity> = LinkedHashMap()
) : TodoDao {

	override suspend fun getAll(): List<TodoEntity> {
		return todos.values.toList()
	}

	override suspend fun getAllChildren(parentId: String): List<TodoEntity> {
		return todos.values.filter {
			it.parentId == parentId
		}
	}

	override suspend fun getChildren(parentId: String, state: String): List<TodoEntity> {
		return todos.values.filter {
			it.parentId == parentId &&
					it.state.contains(state)
		}
	}

	override suspend fun deleteChildren(parentId: String) {
		todos.forEach { (id, todo) ->
			if (todo.parentId == parentId)
				todos.remove(id)
		}
	}

	override suspend fun insert(todo: TodoEntity): Long {
		val newTodo = if (todo.id == 0) {
			val lastId = todos.keys.lastOrNull() ?: 0
			todo.copy(id = lastId + 1)
		} else {
			if (todos.containsKey(todo.id))
				return -1
			todo
		}
		todos[newTodo.id] = newTodo
		return newTodo.id.toLong()
	}

	override suspend fun getByRowId(rowId: Long): TodoEntity? {
		return todos[rowId.toInt()]
	}

	override suspend fun updateTitle(todoId: Int, title: String) {
		val todo = todos[todoId] ?: return
		val newTodo = todo.copy(title = title)
		todos[todoId] = newTodo
	}

	override suspend fun updateRemark(todoId: Int, remark: String) {
		val todo = todos[todoId] ?: return
		val newTodo = todo.copy(remark = remark)
		todos[todoId] = newTodo
	}

	override suspend fun changeDone(todoId: Int) {
		val todo = todos[todoId] ?: return
		val newTodo = todo.copy(done = !todo.done)
		todos[todoId] = newTodo
	}

	override suspend fun getTodo(id: Int): TodoEntity? {
		return todos[id]
	}

	override suspend fun delete(todoIds: List<Int>) {
		for (todoId in todoIds) {
			todos.remove(todoId)
		}
	}

	override suspend fun deleteAll() {
		todos.clear()
	}
}