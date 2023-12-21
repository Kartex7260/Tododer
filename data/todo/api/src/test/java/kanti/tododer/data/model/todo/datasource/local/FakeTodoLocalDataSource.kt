package kanti.tododer.data.model.todo.datasource.local

import kanti.tododer.data.model.ParentId
import kanti.tododer.data.model.todo.Todo
import kanti.tododer.data.model.todo.TodoState
import kanti.tododer.data.model.todo.toTodo

class FakeTodoLocalDataSource(
	private val todos: MutableMap<Int, Todo> = LinkedHashMap()
) : TodoLocalDataSource {

	override suspend fun getChildren(parentId: ParentId, state: TodoState): List<Todo> {
		return todos.values.filter {
			it.parentId == parentId && it.state == state
		}
	}

	override suspend fun insert(todo: Todo): Todo {
		val newTodo = if (todo.id == 0) {
			val lastId = todos.keys.lastOrNull() ?: 0
			todo.toTodo(id = lastId + 1)
		} else {
			if (todos.containsKey(todo.id))
				throw IllegalArgumentException("Todo already exist (id = ${todo.id})")
			todo
		}
		todos[newTodo.id] = newTodo
		return newTodo
	}

	override suspend fun update(todo: Todo): Todo {
		if (!todos.containsKey(todo.id))
			throw IllegalArgumentException("Not found todo by id=${todo.id}")
		todos[todo.id] = todo
		return todo
	}

	override suspend fun update(todos: List<Todo>) {
		for (todo in todos) {
			if (!this.todos.containsKey(todo.id))
				continue
			this.todos[todo.id] = todo
		}
	}

	override suspend fun delete(todos: List<Todo>) {
		for (todo in todos) {
			this.todos.remove(todo.id)
		}
	}
}