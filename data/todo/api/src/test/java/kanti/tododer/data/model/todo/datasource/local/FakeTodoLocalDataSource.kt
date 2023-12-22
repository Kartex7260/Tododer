package kanti.tododer.data.model.todo.datasource.local

import kanti.tododer.data.model.ParentId
import kanti.tododer.data.model.todo.Todo
import kanti.tododer.data.model.todo.TodoState
import kanti.tododer.data.model.todo.toParentId
import kanti.tododer.data.model.todo.toTodo

class FakeTodoLocalDataSource(
	private val todos: MutableMap<Int, Todo> = LinkedHashMap()
) : TodoLocalDataSource {

	override suspend fun getAllChildren(parentId: ParentId): List<Todo> {
		return todos.values.filter {
			it.parentId == parentId
		}
	}

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

	override suspend fun updateTitle(todoId: Int, title: String): Todo {
		val todo = requireTodo(todoId)
		val newTodo = todo.toTodo(title = title)
		todos[todoId] = newTodo
		return newTodo
	}

	override suspend fun updateRemark(todoId: Int, remark: String): Todo {
		val todo = requireTodo(todoId)
		val newTodo = todo.toTodo(remark = remark)
		todos[todoId] = newTodo
		return newTodo
	}

	override suspend fun changeDone(todoId: Int): Todo {
		val todo = requireTodo(todoId)
		val newTodo = todo.toTodo(done = !todo.done)
		todos[todoId] = newTodo
		return newTodo
	}

	override suspend fun delete(todoIds: List<Int>) {
		for (todoId in todoIds) {
			val todo = todos.remove(todoId)
			todo?.apply {
				val children = getAllChildren(toParentId())
				delete(children.map { it.id })
			}
		}
	}

	private fun requireTodo(todoId: Int): Todo {
		return todos[todoId] ?: throw IllegalArgumentException("Not found todo by id=$todoId")
	}
}