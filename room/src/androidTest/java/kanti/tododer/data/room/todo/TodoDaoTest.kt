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
}