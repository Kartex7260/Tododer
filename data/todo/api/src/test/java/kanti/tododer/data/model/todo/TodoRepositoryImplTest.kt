package kanti.tododer.data.model.todo

import kanti.tododer.data.model.ParentId
import kanti.tododer.data.model.ParentType
import kanti.tododer.data.model.todo.datasource.local.FakeTodoLocalDataSource
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class TodoRepositoryImplTest {

	private val todos: MutableMap<Int, Todo> = LinkedHashMap()
	private val repository: TodoRepository = TodoRepositoryImpl(
		localDataSource = FakeTodoLocalDataSource(todos)
	)

	@AfterEach
	fun afterEach() = runTest {
		todos.clear()
	}

	@Test
	@DisplayName("getChildren(String) error")
	fun getChildrenError() = runTest {
	    todos.putAll(mapOf(
			1 to Todo(id = 1, parentId = ParentId(1, ParentType.Plan)),
			2 to Todo(id = 2, parentId = ParentId(1, ParentType.Todo)),
			3 to Todo(id = 3, parentId = ParentId(1, ParentType.Todo)),
			4 to Todo(id = 4, parentId = ParentId(3, ParentType.Todo))
		))

		val children = repository.getChildren(ParentId(45456, ParentType.Plan))
		assertArrayEquals(arrayOf(), children.toTypedArray())
	}

	@Test
	@DisplayName("getChildren(String)")
	fun getChildren() = runTest {
		val expected = arrayOf(
			Todo(id = 2, parentId = ParentId(1, ParentType.Todo)),
			Todo(id = 3, parentId = ParentId(1, ParentType.Todo))
		)

		todos.putAll(mapOf(
			1 to Todo(id = 1, parentId = ParentId(1, ParentType.Plan)),
			2 to Todo(id = 2, parentId = ParentId(1, ParentType.Todo)),
			3 to Todo(id = 3, parentId = ParentId(1, ParentType.Todo)),
			4 to Todo(id = 4, parentId = ParentId(3, ParentType.Todo))
		))

		val children = repository.getChildren(ParentId(1, ParentType.Todo))
		assertArrayEquals(expected, children.toTypedArray())
	}

	@Test
	@DisplayName("create(ParentId, String, String)")
	fun create() = runTest {
	    val expectedArray = arrayOf(
			Todo(id = 1, parentId = ParentId(1, ParentType.Plan)),
			Todo(id = 2, parentId = ParentId(1, ParentType.Plan), title = "Test", remark = "Testable")
		)
		val expectedTodo = Todo(
			id = 2,
			parentId = ParentId(1, ParentType.Plan),
			title = "Test",
			remark = "Testable"
		)

		todos[1] = Todo(id = 1, parentId = ParentId(1, ParentType.Plan))

		val todo = repository.create(ParentId(1, ParentType.Plan), "Test", "Testable")

		assertEquals(expectedTodo, todo)
		assertArrayEquals(expectedArray, todos.values.toTypedArray())
	}

	@Test
	@DisplayName("updateTitle(Todo, String) error")
	fun updateTitleError() = runTest {
		try {
			val todo = repository.updateTitle(todoId = 1, title = "Test")
			assertNull(todo)
		} catch (th: Throwable) {
			assertInstanceOf(IllegalArgumentException::class.java, th)
			assertArrayEquals(arrayOf(), todos.values.toTypedArray())
		}
	}

	@Test
	@DisplayName("updateTitle(Todo, String)")
	fun updateTitle() = runTest {
		val expectedArray = arrayOf(
			Todo(id = 1, parentId = ParentId(1, ParentType.Plan), title = "Test 1"),
			Todo(id = 2, parentId = ParentId(1, ParentType.Plan), title = "Updated 1")
		)
		val expectedTodo = Todo(
			id = 2,
			parentId = ParentId(1, ParentType.Plan),
			title = "Updated 1"
		)

	    todos.putAll(mapOf(
			1 to Todo(id = 1, parentId = ParentId(1, ParentType.Plan), title = "Test 1"),
			2 to Todo(id = 2, parentId = ParentId(1, ParentType.Plan), title = "Test 2")
		))
		val todo = repository.updateTitle(todoId = 2, title = "Updated 1")

		assertEquals(expectedTodo, todo)
		assertArrayEquals(expectedArray, todos.values.toTypedArray())
	}

	@Test
	@DisplayName("updateRemark(Todo, String) error")
	fun updateRemarkError() = runTest {
		try {
			val todo = repository.updateRemark(todoId = 1, remark = "Test")
			assertNull(todo)
		} catch (th: Throwable) {
			assertInstanceOf(IllegalArgumentException::class.java, th)
			assertArrayEquals(arrayOf(), todos.values.toTypedArray())
		}
	}

	@Test
	@DisplayName("updateRemark(Todo, String)")
	fun updateRemark() = runTest {
		val expectedArray = arrayOf(
			Todo(id = 1, parentId = ParentId(1, ParentType.Plan), remark = "Test 1"),
			Todo(id = 2, parentId = ParentId(1, ParentType.Plan), remark = "Updated 1")
		)
		val expectedTodo = Todo(
			id = 2,
			parentId = ParentId(1, ParentType.Plan),
			remark = "Updated 1"
		)

		todos.putAll(mapOf(
			1 to Todo(id = 1, parentId = ParentId(1, ParentType.Plan), remark = "Test 1"),
			2 to Todo(id = 2, parentId = ParentId(1, ParentType.Plan), remark = "Test 2")
		))
		val todo = repository.updateRemark(todoId = 2, remark = "Updated 1")

		assertEquals(expectedTodo, todo)
		assertArrayEquals(expectedArray, todos.values.toTypedArray())
	}

	@Test
	@DisplayName("changeDone(Todo) error")
	fun changeDoneError() = runTest {
		try {
			val todo = repository.changeDone(todoId = 1)
			assertNull(todo)
		} catch (th: Throwable) {
			assertInstanceOf(IllegalArgumentException::class.java, th)
			assertArrayEquals(arrayOf(), todos.values.toTypedArray())
		}
	}

	@Test
	@DisplayName("changeDone(Todo)")
	fun changeDone() = runTest {
		val expectedArray = arrayOf(
			Todo(id = 1, parentId = ParentId(1, ParentType.Plan), done = false),
			Todo(id = 2, parentId = ParentId(1, ParentType.Plan), done = true)
		)
		val expectedTodo1 = Todo(
			id = 1,
			parentId = ParentId(1, ParentType.Plan),
			done = false
		)
		val expectedTodo2 = Todo(
			id = 2,
			parentId = ParentId(1, ParentType.Plan),
			done = true
		)

		todos.putAll(mapOf(
			1 to Todo(id = 1, parentId = ParentId(1, ParentType.Plan), done = true),
			2 to Todo(id = 2, parentId = ParentId(1, ParentType.Plan), done = false)
		))
		val todo1 = repository.changeDone(todoId = 1)
		val todo2 = repository.changeDone(todoId = 2)

		assertEquals(expectedTodo1, todo1)
		assertEquals(expectedTodo2, todo2)
		assertArrayEquals(expectedArray, todos.values.toTypedArray())
	}

	@Test
	@DisplayName("delete(List<Todo>)")
	fun delete() = runTest {
		val expected = arrayOf(
			Todo(id = 1, parentId = ParentId(1, ParentType.Plan))
		)
		todos.putAll(mapOf(
			1 to Todo(id = 1, parentId = ParentId(1, ParentType.Plan)),
			2 to Todo(id = 2, parentId = ParentId(1, ParentType.Todo)),
			3 to Todo(id = 3, parentId = ParentId(1, ParentType.Todo)),
			4 to Todo(id = 4, parentId = ParentId(3, ParentType.Todo))
		))

		repository.delete(listOf(2, 3))

		assertArrayEquals(expected, todos.values.toTypedArray())
	}
}