package kanti.tododer.data.room.todo

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kanti.tododer.data.room.TododerDatabase
import kanti.tododer.data.room.plan.PlanEntity
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TodoDaoTest {

    private val db: TododerDatabase
    private val todoDao: TodoDao

    private val stateNormal =
        "TodoState:fullClassName=STRING-kanti.tododer.data.model.todo.TodoState.Normal"

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
    fun getAllChildrenEmpty() = runTest {
        todoDao.insert(
            listOf(
                TodoEntity(
                    id = 1,
                    state = stateNormal
                ),
                TodoEntity(
                    id = 2,
                    parentId = "Todo-1",
                    state = stateNormal
                ),
                TodoEntity(
                    id = 3,
                    parentId = "Todo-1",
                    state = stateNormal
                ),
                TodoEntity(
                    id = 4,
                    parentId = "Todo-3",
                    state = stateNormal
                )
            )
        )
        val expected = arrayOf<TodoEntity>()

        val children = todoDao.getAllChildren(parentId = "Todo-735894581")
        assertArrayEquals(expected, children.toTypedArray())
    }

    @Test
    fun getAllChildren() = runTest {
        todoDao.insert(
            listOf(
                TodoEntity(
                    id = 1,
                    state = stateNormal
                ),
                TodoEntity(
                    id = 2,
                    parentId = "Todo-1",
                    state = stateNormal
                ),
                TodoEntity(
                    id = 3,
                    parentId = "Todo-1",
                    state = stateNormal
                ),
                TodoEntity(
                    id = 4,
                    parentId = "Todo-3",
                    state = stateNormal
                )
            )
        )
        val expected = arrayOf(
            TodoEntity(id = 2, parentId = "Todo-1", state = stateNormal),
            TodoEntity(id = 3, parentId = "Todo-1", state = stateNormal)
        )

        val children = todoDao.getAllChildren(parentId = "Todo-1")
        assertArrayEquals(expected, children.toTypedArray())
    }

    @Test
    fun getAllChildrenCount0() = runTest {
        todoDao.insert(
            listOf(
                TodoEntity(
                    id = 1,
                    state = stateNormal
                ),
                TodoEntity(
                    id = 2,
                    parentId = "Todo-1",
                    state = stateNormal
                ),
                TodoEntity(
                    id = 3,
                    parentId = "Todo-1",
                    state = stateNormal
                ),
                TodoEntity(
                    id = 4,
                    parentId = "Todo-3",
                    state = stateNormal
                )
            )
        )
        val expected = 0L

        val actual = todoDao.getAllChildrenCount(parentId = "Todo-23678534")
        assertEquals(expected, actual)
    }

    @Test
    fun getAllChildrenCount() = runTest {
        todoDao.insert(
            listOf(
                TodoEntity(
                    id = 1,
                    state = stateNormal
                ),
                TodoEntity(
                    id = 2,
                    parentId = "Todo-1",
                    state = stateNormal
                ),
                TodoEntity(
                    id = 3,
                    parentId = "Todo-1",
                    state = stateNormal
                ),
                TodoEntity(
                    id = 4,
                    parentId = "Todo-3",
                    state = stateNormal
                )
            )
        )
        val expected = 2L

        val actual = todoDao.getAllChildrenCount(parentId = "Todo-1")
        assertEquals(expected, actual)
    }

    @Test
    fun getChildrenEmptyId() = runTest {
        todoDao.insert(
            listOf(
                TodoEntity(
                    id = 1,
                    state = stateNormal
                ),
                TodoEntity(
                    id = 2,
                    parentId = "Todo-1",
                    state = stateNormal
                ),
                TodoEntity(
                    id = 3,
                    parentId = "Todo-1",
                    state = stateNormal
                ),
                TodoEntity(
                    id = 4,
                    parentId = "Todo-3",
                    state = stateNormal
                )
            )
        )
        val expected = arrayOf<PlanEntity>()

        val children = todoDao.getChildren(parentId = "Todo-13567647", stateNormal)
        assertArrayEquals(expected, children.toTypedArray())
    }

    @Test
    fun getChildrenEmptyState() = runTest {
        todoDao.insert(
            listOf(
                TodoEntity(
                    id = 1,
                    state = stateNormal
                ),
                TodoEntity(
                    id = 2,
                    parentId = "Todo-1",
                    state = stateNormal
                ),
                TodoEntity(
                    id = 3,
                    parentId = "Todo-1",
                    state = stateNormal
                ),
                TodoEntity(
                    id = 4,
                    parentId = "Todo-3",
                    state = stateNormal
                )
            )
        )
        val expected = arrayOf<PlanEntity>()

        val children = todoDao.getChildren(parentId = "Todo-1", "Foo-foo")
        assertArrayEquals(expected, children.toTypedArray())
    }

    @Test
    fun getChildren() = runTest {
        todoDao.insert(
            listOf(
                TodoEntity(
                    id = 1,
                    state = stateNormal
                ),
                TodoEntity(
                    id = 2,
                    parentId = "Todo-1",
                    state = stateNormal
                ),
                TodoEntity(
                    id = 3,
                    parentId = "Todo-1",
                    state = stateNormal
                ),
                TodoEntity(
                    id = 4,
                    parentId = "Todo-3",
                    state = stateNormal
                )
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
    fun getChildrenCountEmptyId() = runTest {
        todoDao.insert(
            listOf(
                TodoEntity(
                    id = 1,
                    state = stateNormal
                ),
                TodoEntity(
                    id = 2,
                    parentId = "Todo-1",
                    state = stateNormal
                ),
                TodoEntity(
                    id = 3,
                    parentId = "Todo-1",
                    state = stateNormal
                ),
                TodoEntity(
                    id = 4,
                    parentId = "Todo-3",
                    state = stateNormal
                )
            )
        )
        val expected = 0L

        val actual = todoDao.getChildrenCount(parentId = "Todo-1235523", stateNormal)
        assertEquals(expected, actual)
    }

    @Test
    fun getChildrenCountEmptyState() = runTest {
        todoDao.insert(
            listOf(
                TodoEntity(
                    id = 1,
                    state = stateNormal
                ),
                TodoEntity(
                    id = 2,
                    parentId = "Todo-1",
                    state = stateNormal
                ),
                TodoEntity(
                    id = 3,
                    parentId = "Todo-1",
                    state = stateNormal
                ),
                TodoEntity(
                    id = 4,
                    parentId = "Todo-3",
                    state = stateNormal
                )
            )
        )
        val expected = 0L

        val actual = todoDao.getChildrenCount(parentId = "Todo-1", "Foo-foo")
        assertEquals(expected, actual)
    }

    @Test
    fun getChildrenCount() = runTest {
        todoDao.insert(
            listOf(
                TodoEntity(
                    id = 1,
                    state = stateNormal
                ),
                TodoEntity(
                    id = 2,
                    parentId = "Todo-1",
                    state = stateNormal
                ),
                TodoEntity(
                    id = 3,
                    parentId = "Todo-1",
                    state = stateNormal
                ),
                TodoEntity(
                    id = 4,
                    parentId = "Todo-3",
                    state = stateNormal
                )
            )
        )
        val expected = 2L

        val actual = todoDao.getChildrenCount(parentId = "Todo-1", stateNormal)
        assertEquals(expected, actual)
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

        todoDao.changeDone(1, false)
        todoDao.changeDone(2, true)

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
    fun deleteListEmpty() = runTest {
        todoDao.insert(
            listOf(
                TodoEntity(
                    id = 1,
                    state = stateNormal
                ),
                TodoEntity(
                    id = 2,
                    state = stateNormal
                ),
                TodoEntity(
                    id = 3,
                    state = stateNormal
                ),
                TodoEntity(
                    id = 4,
                    state = stateNormal
                )
            )
        )
        val expected = arrayOf(
            TodoEntity(
                id = 1,
                state = stateNormal
            ),
            TodoEntity(
                id = 2,
                state = stateNormal
            ),
            TodoEntity(
                id = 3,
                state = stateNormal
            ),
            TodoEntity(
                id = 4,
                state = stateNormal
            )
        )

        todoDao.delete(listOf(6, 8))
        assertArrayEquals(expected, todoDao.getAll().toTypedArray())
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

    @Test
    fun deleteIfNameIsEmptyFail() = runTest {
        todoDao.insert(
            listOf(
                TodoEntity(1, title = "Test"),
                TodoEntity(2, title = "")
            )
        )
        val expected = 0

        val actual = todoDao.deleteIfNameIsEmpty(1)
        assertEquals(expected, actual)
    }

    @Test
    fun deleteIfNameIsEmptySuccess() = runTest {
        todoDao.insert(
            listOf(
                TodoEntity(1, title = "Test"),
                TodoEntity(2, title = "")
            )
        )
        val expected = 1

        val actual = todoDao.deleteIfNameIsEmpty(2)
        assertEquals(expected, actual)
    }
}