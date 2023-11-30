package kanti.tododer.domain.todomove

import kanti.tododer.common.Const
import kanti.tododer.data.common.isNotFound
import kanti.tododer.data.common.isSuccess
import kanti.tododer.data.model.task.FakeTaskRepository
import kanti.tododer.data.model.task.Task
import kanti.tododer.data.model.task.TaskImpl
import kanti.tododer.data.model.task.datasource.local.TaskEntity
import kanti.tododer.data.model.task.datasource.local.toTaskEntity
import kanti.tododer.data.model.task.toTask
import kanti.tododer.domain.gettodowithprogeny.task.GetTaskWithProgenyUseCase
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class MoveTaskUseCaseTest {

	private val preFromTask = mutableListOf<TaskEntity>()
	private val fromTasks = mutableListOf<TaskEntity>()
	private val toTasks = mutableListOf<TaskEntity>()
	private val fromRepository = FakeTaskRepository(fromTasks)
	private val toRepository = FakeTaskRepository(toTasks)

	private val fromTasksArray: Array<Task>
		get() = fromTasks.map { it.toTask() }.toTypedArray()
	private val toTasksArray: Array<Task>
		get() = toTasks.map { it.toTask() }.toTypedArray()

	private val useCase = MoveTaskUseCase(
		getTaskWithProgenyUseCase = GetTaskWithProgenyUseCase()
	)

	private val rootId = Const.ROOT_PARENT_ID
	private val task1Id = 1
	private val task1 = TaskImpl(
		id = task1Id,
		parentId = rootId
	)
	private val task11Id = 2
	private val task11 = TaskImpl(
		id = task11Id,
		parentId = task1.fullId
	)
	private val task111Id = 3
	private val task111 = TaskImpl(
		id = task111Id,
		parentId = task11.fullId
	)
	private val task112Id = 4
	private val task112 = TaskImpl(
		id = task112Id,
		parentId = task11.fullId
	)
	private val task12Id = 5
	private val task12 = TaskImpl(
		id = task12Id,
		parentId = task1.fullId
	)

	private val task2Id = 6
	private val task2 = TaskImpl(
		id = task2Id,
		parentId = rootId
	)
	private val task21Id = 7
	private val task21 = TaskImpl(
		id = task21Id,
		parentId = task2.fullId
	)

	init {
		preFromTask.addAll(
			arrayOf(
				task1,
				task11,
				task111,
				task112,
				task12,
				task2,
				task21
			).map { it.toTaskEntity() }
		)
	}

	@BeforeEach
	fun beforeEach() {
		fromTasks.addAll(preFromTask)
	}

	@AfterEach
	fun afterEach() {
		fromTasks.clear()
		toTasks.clear()
	}

	@Test
	@DisplayName("move fail")
	fun returnFail() = runTest {
	    val repRes = useCase(
			fromRepository,
			toRepository,
			TaskImpl(id = 100000)
		)
		assertNull(repRes.value)
		assertTrue(repRes.isNotFound)
		assertArrayEquals(
			arrayOf(task1, task11, task111, task112, task12, task2, task21),
			fromTasksArray
		)
		assertArrayEquals(
			arrayOf(),
			toTasksArray
		)
	}

	@Test
	@DisplayName("move top level 1")
	fun topLevel1() = runTest {
	    val repRes = useCase(
			fromRepository,
			toRepository,
			task1
		)
		assertTrue(repRes.isSuccess)
		assertArrayEquals(
			arrayOf(task2, task21),
			fromTasksArray
		)
		assertArrayEquals(
			arrayOf(task1, task11, task111, task112, task12),
			toTasksArray
		)
	}

	@Test
	@DisplayName("move top level 2")
	fun topLevel2() = runTest {
		val repRes = useCase(
			fromRepository,
			toRepository,
			task2
		)
		assertTrue(repRes.isSuccess)
		assertArrayEquals(
			arrayOf(task1, task11, task111, task112, task12),
			fromTasksArray
		)
		assertArrayEquals(
			arrayOf(task2, task21),
			toTasksArray
		)
	}

	@Test
	@DisplayName("move second level")
	fun secondLevel() = runTest {
		val repRes = useCase(
			fromRepository,
			toRepository,
			task11
		)
		assertTrue(repRes.isSuccess)
		assertArrayEquals(
			arrayOf(task1,  task12, task2, task21),
			fromTasksArray
		)
		assertArrayEquals(
			arrayOf(task11, task111, task112),
			toTasksArray
		)
	}

}