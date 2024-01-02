package kanti.tododer.data.model.todo

import kanti.tododer.data.model.FullId
import kanti.tododer.data.model.FullIdType
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
			1 to Todo(id = 1, parentId = FullId(1, FullIdType.Plan)),
			2 to Todo(id = 2, parentId = FullId(1, FullIdType.Todo)),
			3 to Todo(id = 3, parentId = FullId(1, FullIdType.Todo)),
			4 to Todo(id = 4, parentId = FullId(3, FullIdType.Todo))
		))

		val children = repository.getChildren(FullId(45456, FullIdType.Plan))
		assertArrayEquals(arrayOf(), children.toTypedArray())
	}

	@Test
	@DisplayName("getChildren(String)")
	fun getChildren() = runTest {
		val expected = arrayOf(
			Todo(id = 2, parentId = FullId(1, FullIdType.Todo)),
			Todo(id = 3, parentId = FullId(1, FullIdType.Todo))
		)

		todos.putAll(mapOf(
			1 to Todo(id = 1, parentId = FullId(1, FullIdType.Plan)),
			2 to Todo(id = 2, parentId = FullId(1, FullIdType.Todo)),
			3 to Todo(id = 3, parentId = FullId(1, FullIdType.Todo)),
			4 to Todo(id = 4, parentId = FullId(3, FullIdType.Todo))
		))

		val children = repository.getChildren(FullId(1, FullIdType.Todo))
		assertArrayEquals(expected, children.toTypedArray())
	}

	@Test
	@DisplayName("create(ParentId, String, String)")
	fun create() = runTest {
	    val expectedArray = arrayOf(
			Todo(id = 1, parentId = FullId(1, FullIdType.Plan)),
			Todo(id = 2, parentId = FullId(1, FullIdType.Plan), title = "Test", remark = "Testable")
		)
		val expectedTodo = Todo(
			id = 2,
			parentId = FullId(1, FullIdType.Plan),
			title = "Test",
			remark = "Testable"
		)

		todos[1] = Todo(id = 1, parentId = FullId(1, FullIdType.Plan))

		val todo = repository.create(FullId(1, FullIdType.Plan), "Test", "Testable")

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
			Todo(id = 1, parentId = FullId(1, FullIdType.Plan), title = "Test 1"),
			Todo(id = 2, parentId = FullId(1, FullIdType.Plan), title = "Updated 1")
		)
		val expectedTodo = Todo(
			id = 2,
			parentId = FullId(1, FullIdType.Plan),
			title = "Updated 1"
		)

	    todos.putAll(mapOf(
			1 to Todo(id = 1, parentId = FullId(1, FullIdType.Plan), title = "Test 1"),
			2 to Todo(id = 2, parentId = FullId(1, FullIdType.Plan), title = "Test 2")
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
			Todo(id = 1, parentId = FullId(1, FullIdType.Plan), remark = "Test 1"),
			Todo(id = 2, parentId = FullId(1, FullIdType.Plan), remark = "Updated 1")
		)
		val expectedTodo = Todo(
			id = 2,
			parentId = FullId(1, FullIdType.Plan),
			remark = "Updated 1"
		)

		todos.putAll(mapOf(
			1 to Todo(id = 1, parentId = FullId(1, FullIdType.Plan), remark = "Test 1"),
			2 to Todo(id = 2, parentId = FullId(1, FullIdType.Plan), remark = "Test 2")
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
			Todo(id = 1, parentId = FullId(1, FullIdType.Plan), done = false),
			Todo(id = 2, parentId = FullId(1, FullIdType.Plan), done = true)
		)
		val expectedTodo1 = Todo(
			id = 1,
			parentId = FullId(1, FullIdType.Plan),
			done = false
		)
		val expectedTodo2 = Todo(
			id = 2,
			parentId = FullId(1, FullIdType.Plan),
			done = true
		)

		todos.putAll(mapOf(
			1 to Todo(id = 1, parentId = FullId(1, FullIdType.Plan), done = true),
			2 to Todo(id = 2, parentId = FullId(1, FullIdType.Plan), done = false)
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
			Todo(id = 1, parentId = FullId(1, FullIdType.Plan))
		)
		todos.putAll(mapOf(
			1 to Todo(id = 1, parentId = FullId(1, FullIdType.Plan)),
			2 to Todo(id = 2, parentId = FullId(1, FullIdType.Todo)),
			3 to Todo(id = 3, parentId = FullId(1, FullIdType.Todo)),
			4 to Todo(id = 4, parentId = FullId(3, FullIdType.Todo))
		))

		repository.delete(listOf(2, 3))

		assertArrayEquals(expected, todos.values.toTypedArray())
	}
}