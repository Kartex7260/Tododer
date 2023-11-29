package kanti.tododer.domain

import kanti.tododer.common.Const
import kanti.tododer.data.model.plan.FakePlanRepository
import kanti.tododer.data.model.plan.PlanImpl
import kanti.tododer.data.model.plan.datasource.local.PlanEntity
import kanti.tododer.data.model.plan.datasource.local.toPlanEntity
import kanti.tododer.data.model.task.FakeTaskRepository
import kanti.tododer.data.model.task.TaskImpl
import kanti.tododer.data.model.task.datasource.local.TaskEntity
import kanti.tododer.data.model.task.datasource.local.toTaskEntity
import kanti.tododer.data.model.RepositorySet
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class GetForerunnersUseCaseTest {

	private val tasks: MutableList<TaskEntity> = mutableListOf()
	private val plans: MutableList<PlanEntity> = mutableListOf()
	private val repositorySet = RepositorySet(
		taskRepository = FakeTaskRepository(tasks),
		planRepository = FakePlanRepository(plans)
	)
	private val useCase = GetForerunnersUseCase(
		getParentUseCase = GetParentUseCase()
	)

	private val root = Const.ROOT_PARENT_ID

	private val flow11PlanId = 1
	private val flow11Plan = PlanImpl(
		id = flow11PlanId,
		parentId = root
	)
	private val flow12PlanId = 2
	private val flow12Plan = PlanImpl(
		id = flow12PlanId,
		parentId = flow11Plan.fullId
	)
	private val flow13TaskId = 1
	private val flow13Task = TaskImpl(
		id = flow13TaskId,
		parentId = flow12Plan.fullId
	)
	private val flow14TaskId = 2
	private val flow14Task = TaskImpl(
		id = flow14TaskId,
		parentId = flow13Task.fullId
	)

	private val flow22PlanId = 3
	private val flow22Plan = PlanImpl(
		id = flow22PlanId,
		parentId = flow11Plan.fullId
	)
	private val flow23TaskId = 3
	private val flow23Task = TaskImpl(
		id = flow23TaskId,
		parentId = flow22Plan.fullId
	)
	private val flow24TaskId = 4
	private val flow24Task = TaskImpl(
		id = flow24TaskId,
		parentId = flow23Task.fullId
	)

	init {
		tasks.apply {
			add(flow13Task.toTaskEntity())
			add(flow14Task.toTaskEntity())
			add(flow23Task.toTaskEntity())
			add(flow24Task.toTaskEntity())
		}
		plans.apply {
			add(flow11Plan.toPlanEntity())
			add(flow12Plan.toPlanEntity())
			add(flow22Plan.toPlanEntity())
		}
	}

	@Test
	@DisplayName("empty result")
	fun emptyResult() = runTest {
	    val forerunners = useCase(repositorySet, flow11Plan)
		Assertions.assertTrue(forerunners.isEmpty())
	}

	@Test
	@DisplayName("result 1")
	fun result1() = runTest {
	    val forerunners = useCase(repositorySet, flow14Task)
		Assertions.assertArrayEquals(
			arrayOf(flow13Task, flow12Plan, flow11Plan),
			forerunners.toTypedArray()
		)
	}

	@Test
	@DisplayName("result 2")
	fun result2() = runTest {
		val forerunners = useCase(repositorySet, flow24Task)
		Assertions.assertArrayEquals(
			arrayOf(flow23Task, flow22Plan, flow11Plan),
			forerunners.toTypedArray()
		)
	}

}