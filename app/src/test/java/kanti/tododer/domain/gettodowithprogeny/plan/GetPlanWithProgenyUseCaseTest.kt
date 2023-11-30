package kanti.tododer.domain.gettodowithprogeny.plan

import kanti.tododer.common.Const
import kanti.tododer.data.common.isNotFound
import kanti.tododer.data.common.isSuccess
import kanti.tododer.data.model.RepositorySet
import kanti.tododer.data.model.plan.FakePlanRepository
import kanti.tododer.data.model.plan.PlanImpl
import kanti.tododer.data.model.plan.datasource.local.PlanEntity
import kanti.tododer.data.model.plan.datasource.local.toPlanEntity
import kanti.tododer.data.model.task.FakeTaskRepository
import kanti.tododer.data.model.task.TaskImpl
import kanti.tododer.data.model.task.datasource.local.TaskEntity
import kanti.tododer.data.model.task.datasource.local.toTaskEntity
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class GetPlanWithProgenyUseCaseTest {

	private val useCase = GetPlanWithProgenyUseCase()
	private val tasks = mutableListOf<TaskEntity>()
	private val plans = mutableListOf<PlanEntity>()
	private val repositorySet = RepositorySet(
		taskRepository = FakeTaskRepository(tasks),
		planRepository = FakePlanRepository(plans)
	)

	private val rootId = Const.ROOT_PARENT_ID
	private val plan1Id = 1
	private val plan1 = PlanImpl(
		id = plan1Id,
		parentId = rootId
	)
	private val plan11Id = 2
	private val plan11 = PlanImpl(
		id = plan11Id,
		parentId = plan1.fullId
	)
	private val task111Id = 1
	private val task111 = TaskImpl(
		id = task111Id,
		parentId = plan11.fullId
	)
	private val task112Id = 2
	private val task112 = TaskImpl(
		id = task112Id,
		parentId = plan11.fullId
	)
	private val plan12Id = 3
	private val plan12 = PlanImpl(
		id = plan12Id,
		parentId = plan1.fullId
	)
	private val task121Id = 3
	private val task121 = TaskImpl(
		id = task121Id,
		parentId = plan12.fullId
	)

	private val plan2Id = 4
	private val plan2 = PlanImpl(
		id = plan2Id,
		parentId = rootId
	)
	private val task21Id = 4
	private val task21 = TaskImpl(
		id = task21Id,
		parentId = plan2.fullId
	)

	init {
		plans.apply {
			add(plan1.toPlanEntity())
			add(plan11.toPlanEntity())
			add(plan12.toPlanEntity())
			add(plan2.toPlanEntity())
		}
		tasks.apply {
			add(task111.toTaskEntity())
			add(task112.toTaskEntity())
			add(task121.toTaskEntity())
			add(task21.toTaskEntity())
		}
	}

	@Test
	@DisplayName("return fail")
	fun returnFail() = runTest {
	    val repRes = useCase(repositorySet, PlanImpl(id = 10000))
		assertNull(repRes.value)
		assertTrue(repRes.isNotFound)
	}

	@Test
	@DisplayName("return top level 1")
	fun topLevel1() = runTest {
	    val repRes = useCase(repositorySet, plan1)
		assertTrue(repRes.isSuccess)
		assertArrayEquals(
			arrayOf(plan1, plan11, plan12),
			repRes.value?.plans?.toTypedArray()
		)
		assertArrayEquals(
			arrayOf(task111, task112, task121),
			repRes.value?.tasks?.toTypedArray()
		)
	}

	@Test
	@DisplayName("return top level 2")
	fun topLevel2() = runTest {
		val repRes = useCase(repositorySet, plan2)
		assertTrue(repRes.isSuccess)
		assertArrayEquals(
			arrayOf(plan2),
			repRes.value?.plans?.toTypedArray()
		)
		assertArrayEquals(
			arrayOf(task21),
			repRes.value?.tasks?.toTypedArray()
		)
	}

	@Test
	@DisplayName("return second level")
	fun secondLevel() = runTest {
	    val repRes = useCase(repositorySet, plan11)
		assertTrue(repRes.isSuccess)
		assertArrayEquals(
			arrayOf(plan11),
			repRes.value?.plans?.toTypedArray()
		)
		assertArrayEquals(
			arrayOf(task111, task112),
			repRes.value?.tasks?.toTypedArray()
		)
	}

}