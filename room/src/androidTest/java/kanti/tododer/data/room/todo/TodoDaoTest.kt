package kanti.tododer.data.room.todo

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import kanti.tododer.data.room.TododerDatabase
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Test

class TodoDaoTest {

	private val db: TododerDatabase
	private val todoDao: TodoDao

	private val stateNormal = "TodoState:fullClassName=STRING-kanti.tododer.data.model.todo.TodoState.Normal"

	init {
		val context = ApplicationProvider.getApplicationContext<Context>()
		db = Room.inMemoryDatabaseBuilder(context, TododerDatabase::class.java).build()
		todoDao = db.todoDao()
	}

	@After
	fun after() = runTest {
		todoDao.deleteAll()
	}

	@Test
	fun getAllChildren() = runTest {
		todoDao.insert(
			TodoEntity(
				id = 1,
				state = stateNormal
			)
		)
		todoDao.insert(
			TodoEntity(
				id = 2,
				parentId = "Todo-1",
				state = stateNormal
			)
		)
		todoDao.insert(
			TodoEntity(
				id = 3,
				parentId = "Todo-1",
				state = stateNormal
			)
		)
		todoDao.insert(
			TodoEntity(
				id = 4,
				parentId = "Todo-3",
				state = stateNormal
			)
		)
		val expected = arrayOf(
			TodoEntity(id = 2, parentId = "Todo-1", state = stateNormal),
			TodoEntity(id = 3, parentId = "Todo-1", state = stateNormal)
		)

		val children = todoDao.getChildren(parentId = "Todo-1", stateNormal)
		assertArrayEquals(expected, children.toTypedArray())
	}

	@Test
	fun getChildren() = runTest {
		todoDao.insert(
			TodoEntity(
				id = 1,
				state = stateNormal
			)
		)
		todoDao.insert(
			TodoEntity(
				id = 2,
				parentId = "Todo-1",
				state = stateNormal
			)
		)
		todoDao.insert(
			TodoEntity(
				id = 3,
				parentId = "Todo-1",
				state = stateNormal
			)
		)
		todoDao.insert(
			TodoEntity(
				id = 4,
				parentId = "Todo-3",
				state = stateNormal
			)
		)
		val expected = arrayOf(
			TodoEntity(id = 2, parentId = "Todo-1", state = stateNormal),
			TodoEntity(id = 3, parentId = "Todo-1", state = stateNormal)
		)

		val children = todoDao.getChildren(parentId = "Todo-1", stateNormal)
		assertArrayEquals(expected, children.toTypedArray())
	}

	@Test
	fun deleteChildren() = runTest {
		todoDao.insert(
			TodoEntity(
				id = 1,
				state = stateNormal
			)
		)
		todoDao.insert(
			TodoEntity(
				id = 2,
				parentId = "Todo-1",
				state = stateNormal
			)
		)
		todoDao.insert(
			TodoEntity(
				id = 3,
				parentId = "Todo-1",
				state = stateNormal
			)
		)
		todoDao.insert(
			TodoEntity(
				id = 4,
				parentId = "Todo-3",
				state = stateNormal
			)
		)
		val expected = arrayOf(
			TodoEntity(id = 1, state = stateNormal),
			TodoEntity(id = 4, parentId = "Todo-3", state = stateNormal)
		)

		todoDao.deleteChildren(parentId = "Todo-1")
		assertArrayEquals(expected, todoDao.getAll().toTypedArray())
	}

	@Test
	fun getByRowIdNull() = runTest {
		val todo = todoDao.getByRowId(1334L)
		assertNull(todo)
	}

	@Test
	fun getByRowId() = runTest {
		val rowId = todoDao.insert(TodoEntity(id = 1, state = stateNormal))
		val todo = todoDao.getByRowId(rowId)
		assertEquals(
			TodoEntity(id = 1, state = stateNormal),
			todo
		)
	}

	@Test
	fun updateTitle() = runTest {
		val expectedArray = arrayOf(
			TodoEntity(
				id = 1,
				title = "Updated 1",
				state = stateNormal
			),
			TodoEntity(
				id = 2,
				title = "Test 2",
				state = stateNormal
			)
		)
		todoDao.insert(
			TodoEntity(
				id = 1,
				title = "Test 1",
				state = stateNormal
			)
		)
		todoDao.insert(
			TodoEntity(
				id = 2,
				title = "Test 2",
				state = stateNormal
			)
		)

		todoDao.updateTitle(1, "Updated 1")

		assertArrayEquals(expectedArray, todoDao.getAll().toTypedArray())
	}

	@Test
	fun updateRemark() = runTest {
		val expectedArray = arrayOf(
			TodoEntity(
				id = 1,
				remark = "Updated 1",
				state = stateNormal
			),
			TodoEntity(
				id = 2,
				remark = "Test 2",
				state = stateNormal
			)
		)
		todoDao.insert(
			TodoEntity(
				id = 1,
				remark = "Test 1",
				state = stateNormal
			)
		)
		todoDao.insert(
			TodoEntity(
				id = 2,
				remark = "Test 2",
				state = stateNormal
			)
		)

		todoDao.updateRemark(1, "Updated 1")

		assertArrayEquals(expectedArray, todoDao.getAll().toTypedArray())
	}

	@Test
	fun changeDone() = runTest {
		val expectedArray = arrayOf(
			TodoEntity(
				id = 1,
				done = false,
				state = stateNormal
			),
			TodoEntity(
				id = 2,
				done = true,
				state = stateNormal
			)
		)
		todoDao.insert(
			TodoEntity(
				id = 1,
				done = true,
				state = stateNormal
			)
		)
		todoDao.insert(
			TodoEntity(
				id = 2,
				done = false,
				state = stateNormal
			)
		)

		todoDao.changeDone(1)
		todoDao.changeDone(2)

		assertArrayEquals(expectedArray, todoDao.getAll().toTypedArray())
	}

	@Test
	fun getTodoNull() = runTest {
		val todo = todoDao.getTodo(133)
		assertNull(todo)
	}

	@Test
	fun getTodo() = runTest {
		todoDao.insert(TodoEntity(id = 1, state = stateNormal))
		val todo = todoDao.getTodo(1)
		assertEquals(
			TodoEntity(id = 1, state = stateNormal),
			todo
		)
	}

	@Test
	fun deleteList() = runTest {
		todoDao.insert(
			TodoEntity(
				id = 1,
				state = stateNormal
			)
		)
		todoDao.insert(
			TodoEntity(
				id = 2,
				state = stateNormal
			)
		)
		todoDao.insert(
			TodoEntity(
				id = 3,
				state = stateNormal
			)
		)
		todoDao.insert(
			TodoEntity(
				id = 4,
				state = stateNormal
			)
		)
		val expected = arrayOf(
			TodoEntity(id = 1, state = stateNormal),
			TodoEntity(id = 3, state = stateNormal)
		)

		todoDao.delete(listOf(2, 4))
		assertArrayEquals(expected, todoDao.getAll().toTypedArray())
	}
}