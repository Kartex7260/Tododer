package kanti.tododer.domain.gettodowithprogeny.task

import kanti.tododer.common.Const
import kanti.tododer.data.common.isNotFound
import kanti.tododer.data.common.isSuccess
import kanti.tododer.data.model.task.FakeTaskRepository
import kanti.tododer.data.model.task.TaskImpl
import kanti.tododer.data.model.task.TaskRepository
import kanti.tododer.data.model.task.datasource.local.TaskEntity
import kanti.tododer.data.model.task.datasource.local.toTaskEntity
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class GetTaskWithProgenyUseCaseTest {

	private val useCase = GetTaskWithProgenyUseCase()

	private val tasks: MutableList<TaskEntity> = mutableListOf()
	private val repository: TaskRepository = FakeTaskRepository(tasks)

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
	private val task12Id = 4
	private val task12 = TaskImpl(
		id = task12Id,
		parentId = task1.fullId
	)

	private val task2Id = 5
	private val task2 = TaskImpl(
		id = task2Id,
		parentId = rootId
	)
	private val task21Id = 6
	private val task21 = TaskImpl(
		id = task21Id,
		parentId = task2.fullId
	)

	init {
		tasks.addAll(
			arrayOf(
				task1,
				task11, task12,
				task111,
				task2,
				task21
			).map { it.toTaskEntity() }
		)
	}

	@Test
	@DisplayName("Fail")
	fun fail1() = runTest {
	    val taskWithProgeny = useCase(repository, TaskImpl(id = 100000))
		assertNull(taskWithProgeny.value)
		assertTrue(taskWithProgeny.isNotFound)
	}

	@Test
	@DisplayName("result top level 1")
	fun topLevel1() = runTest {
	    val taskWithChildren = useCase(repository, task1)
		assertTrue(taskWithChildren.isSuccess)
		assertArrayEquals(
			arrayOf(task1, task11, task111, task12),
			taskWithChildren.value?.tasks?.toTypedArray()
		)
	}

	@Test
	@DisplayName("result top level 2")
	fun topLevel2() = runTest {
		val taskWithChildren = useCase(repository, task2)
		assertTrue(taskWithChildren.isSuccess)
		assertArrayEquals(
			arrayOf(task2, task21),
			taskWithChildren.value?.tasks?.toTypedArray()
		)
	}

	@Test
	@DisplayName("result second level")
	fun secondLevel() = runTest {
	    val taskWithProgeny = useCase(repository, task11)
		assertTrue(taskWithProgeny.isSuccess)
		assertArrayEquals(
			arrayOf(task11, task111),
			taskWithProgeny.value?.tasks?.toTypedArray()
		)
	}

}