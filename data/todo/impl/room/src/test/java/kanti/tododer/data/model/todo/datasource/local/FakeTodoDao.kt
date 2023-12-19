package kanti.tododer.data.model.todo.datasource.local

import kanti.tododer.data.room.todo.TodoDao
import kanti.tododer.data.room.todo.TodoEntity

class FakeTodoDao(
	val todos: MutableMap<Int, TodoEntity> = LinkedHashMap()
) : TodoDao {

	override suspend fun getAll(): List<TodoEntity> {
		return todos.values.toList()
	}

	override suspend fun getChildren(parentId: String): List<TodoEntity> {
		return todos.values.filter { it.parentId == parentId }
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

	override suspend fun update(todos: List<TodoEntity>) {
		for (todo in todos) {
			if (this.todos.containsKey(todo.id)) {
				this.todos[todo.id] = todo
			}
		}
	}

	override suspend fun getTodo(id: Int): TodoEntity? {
		return todos[id]
	}

	override suspend fun delete(todos: List<TodoEntity>) {
		for (todo in todos) {
			this.todos.remove(todo.id)
		}
	}

	override suspend fun deleteAll() {
		todos.clear()
	}
}