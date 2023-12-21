package kanti.tododer.data.model.todo.datasource.local

import kanti.sl.StateLanguage
import kanti.sl.serialize
import kanti.tododer.data.model.ParentId
import kanti.tododer.data.model.ParentType
import kanti.tododer.data.model.todo.Todo
import kanti.tododer.data.model.todo.TodoState
import kanti.tododer.data.room.todo.TodoEntity
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class TodoRoomDataSourceTest {

	private val sl = StateLanguage {  }

	private val stateNormal = sl.serialize(TodoState.Normal as TodoState)

	private val todos: MutableMap<Int, TodoEntity> = LinkedHashMap()
	private val dataSource: TodoLocalDataSource = TodoRoomDataSource(
		todoDao = FakeTodoDao(todos),
		sl = sl
	)

	@AfterEach
	fun afterEach() = runTest {
		todos.clear()
	}

	@Test
	@DisplayName("getChildren(String)")
	fun getChildren() = runTest {
	    todos.putAll(mapOf(
			1 to TodoEntity(id = 1, parentId = "Plan-1", state = stateNormal),
			2 to TodoEntity(id = 2, parentId = "Todo-1", state = stateNormal),
			3 to TodoEntity(id = 3, parentId = "Todo-1", state = stateNormal),
			4 to TodoEntity(id = 4, parentId = "Todo-2", state = stateNormal)
		))
		val expected = arrayOf(
			Todo(id = 2, parentId = ParentId(1, ParentType.Todo)),
			Todo(id = 3, parentId = ParentId(1, ParentType.Todo))
		)

		val children = dataSource.getChildren(ParentId(1, ParentType.Todo))
		assertArrayEquals(expected, children.toTypedArray())
	}

	@Test
	@DisplayName("insert(Todo) error")
	fun insertError() = runTest {
	    todos[1] = TodoEntity(id = 1, parentId = "Plan-1", state = stateNormal)
		try {
			val todo = dataSource.insert(Todo(id = 1, ParentId(1, ParentType.Plan)))
			assertNull(todo)
		} catch (th: Throwable) {
			assertInstanceOf(IllegalArgumentException::class.java, th)
		}
	}

	@Test
	@DisplayName("insert(Todo)")
	fun insert() = runTest {
	    val expectedTodo = Todo(id = 1, parentId = ParentId(1, ParentType.Plan))
		val expectedArray = arrayOf(
			TodoEntity(id = 1, parentId = "Plan-1", state = stateNormal)
		)

		val todo = dataSource.insert(Todo(parentId = ParentId(1, ParentType.Plan)))
		assertEquals(expectedTodo, todo)
		assertArrayEquals(
			expectedArray,
			todos.values.toTypedArray()
		)
	}

	@Test
	@DisplayName("update(Todo) error")
	fun updateError() = runTest {
	    try {
			val todo = dataSource.update(Todo(id = 1, ParentId(1, ParentType.Plan)))
			assertNull(todo)
		} catch (th: Throwable) {
			assertInstanceOf(IllegalArgumentException::class.java, th)
		}
	}

	@Test
	@DisplayName("update(Todo)")
	fun update() = runTest {
	    todos[1] = TodoEntity(id = 1, parentId = "Plan-1", state = stateNormal)
		val expectedTodo = Todo(id = 1, parentId = ParentId(1, ParentType.Plan), done = true)
		val expectedArray = arrayOf(
			TodoEntity(id = 1, parentId = "Plan-1", done = true, state = stateNormal)
		)

		val todo = dataSource.update(Todo(
			id = 1,
			parentId = ParentId(1, ParentType.Plan),
			done = true
		))
		assertEquals(expectedTodo, todo)
		assertArrayEquals(expectedArray, todos.values.toTypedArray())
	}

	@Test
	@DisplayName("update(List<Todo>)")
	fun updateList() = runTest {
	    todos.putAll(mapOf(
			1 to TodoEntity(id = 1, title = "Test 1", parentId = "Plan-1", state = stateNormal),
			2 to TodoEntity(id = 2, title = "Test 2", parentId = "Todo-1", state = stateNormal),
			3 to TodoEntity(id = 3, title = "Test 3", parentId = "Todo-1", state = stateNormal),
			4 to TodoEntity(id = 4, title = "Test 4", parentId = "Todo-3", state = stateNormal)
		))
		val expected = arrayOf(
			TodoEntity(id = 1, title = "Test 1", parentId = "Plan-1", state = stateNormal),
			TodoEntity(id = 2, title = "Updated 1", parentId = "Todo-1", state = stateNormal),
			TodoEntity(id = 3, title = "Test 3", parentId = "Todo-1", state = stateNormal),
			TodoEntity(id = 4, title = "Updated 2", parentId = "Todo-3", state = stateNormal)
		)

		dataSource.update(listOf(
			Todo(id = 2, title = "Updated 1", parentId = ParentId(1, ParentType.Todo)),
			Todo(id = 4, title = "Updated 2", parentId = ParentId(3, ParentType.Todo)),
			Todo(id = 5, title = "Updated 3", parentId = ParentId(3, ParentType.Todo))
		))

		assertArrayEquals(expected, todos.values.toTypedArray())
	}

	@Test
	@DisplayName("delete(List<Todo>)")
	fun deleteList() = runTest {
		todos.putAll(mapOf(
			1 to TodoEntity(id = 1, title = "Test 1", parentId = "Plan-1", state = stateNormal),
			2 to TodoEntity(id = 2, title = "Test 2", parentId = "Todo-1", state = stateNormal),
			3 to TodoEntity(id = 3, title = "Test 3", parentId = "Todo-1", state = stateNormal),
			4 to TodoEntity(id = 4, title = "Test 4", parentId = "Todo-3", state = stateNormal)
		))
		val expected = arrayOf(
			TodoEntity(id = 1, title = "Test 1", parentId = "Plan-1", state = stateNormal),
			TodoEntity(id = 3, title = "Test 3", parentId = "Todo-1", state = stateNormal)
		)

		dataSource.delete(listOf(
			Todo(id = 2, parentId = ParentId(1, ParentType.Todo)),
			Todo(id = 4, parentId = ParentId(3, ParentType.Todo)),
			Todo(id = 5, parentId = ParentId(3, ParentType.Todo))
		))

		assertArrayEquals(expected, todos.values.toTypedArray())
	}
}