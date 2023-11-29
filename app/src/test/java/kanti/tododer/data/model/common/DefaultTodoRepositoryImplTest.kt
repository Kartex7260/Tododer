package kanti.tododer.data.model.common

import kanti.tododer.data.common.isAlreadyExists
import kanti.tododer.data.common.isNotFound
import kanti.tododer.data.common.isSuccess
import kanti.tododer.data.model.common.datasource.local.DefaultTodoDaoDataSourceImpl
import kanti.tododer.data.model.plan.Plan
import kanti.tododer.data.model.plan.PlanImpl
import kanti.tododer.data.model.plan.datasource.local.FakePlanDao
import kanti.tododer.data.model.plan.datasource.local.PlanEntity
import kanti.tododer.data.model.plan.datasource.local.toPlanEntity
import kanti.tododer.data.model.plan.toPlan
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class DefaultTodoRepositoryImplTest {

	private val plans: MutableList<PlanEntity> = mutableListOf()
	private val plansArray: Array<Plan>
		get() = plans.map { it.toPlan() }.toTypedArray()
	private val dataSource = DefaultTodoDaoDataSourceImpl(
		FakePlanDao(plans)
	)
	private val repository: TodoRepository<Plan> = DefaultTodoRepositoryImpl(dataSource)

	private val parentId = "root"
	private val id1 = 1
	private val plan1 = PlanImpl(
		id = id1,
		parentId = parentId
	)
	private val id2 = 2
	private val plan2 = PlanImpl(
		id = id2,
		parentId = "wrong"
	)
	private val id3 = 3
	private val plan3 = PlanImpl(
		id = id3,
		parentId = parentId
	)

	@AfterEach
	fun after() {
		plans.clear()
	}

	@Test
	@DisplayName("getTodo(Int): result null 1")
	fun getTodoResultNull1() = runTest {
		val repRes = repository.getTodo(id1)
		Assertions.assertNull(repRes.value)
		Assertions.assertTrue(repRes.isNotFound)
	}

	@Test
	@DisplayName("getTodo(Int): result null 2")
	fun getTodoResultNull2() = runTest {
		plans.add(plan2.toPlanEntity())
		val repRes = repository.getTodo(id1)
		Assertions.assertNull(repRes.value)
		Assertions.assertTrue(repRes.isNotFound)
	}

	@Test
	@DisplayName("getTodo(Int): result 1")
	fun getTodoResult1() = runTest {
		plans.add(plan1.toPlanEntity())
		val repRes = repository.getTodo(id1)
		Assertions.assertNotNull(repRes.value)
		Assertions.assertTrue(repRes.isSuccess)
	}

	@Test
	@DisplayName("getTodo(Int): result 2")
	fun getTodoResult2() = runTest {
		plans.apply {
			add(plan1.toPlanEntity())
			add(plan2.toPlanEntity())
			add(plan3.toPlanEntity())
		}
		val repRes = repository.getTodo(id2)
		Assertions.assertNotNull(repRes.value)
		Assertions.assertTrue(repRes.isSuccess)
	}

	@Test
	@DisplayName("getChildren(String): result empty 1")
	fun getChildrenResultEmpty1() = runTest {
		val children = repository.getChildren(parentId)
		Assertions.assertNotNull(children.value)
		Assertions.assertTrue(children.isSuccess)
		Assertions.assertArrayEquals(
			arrayOf<Plan>(),
			children.value?.toTypedArray()
		)
	}

	@Test
	@DisplayName("getChildren(String): result empty 2")
	fun getChildrenResultEmpty2() = runTest {
		plans.add(plan2.toPlanEntity())
		val children = repository.getChildren(parentId)
		Assertions.assertNotNull(children.value)
		Assertions.assertTrue(children.isSuccess)
		Assertions.assertArrayEquals(
			arrayOf<Plan>(),
			children.value?.toTypedArray()
		)
	}

	@Test
	@DisplayName("getChildren(String): result 1")
	fun getChildrenResult1() = runTest {
		plans.apply {
			add(plan1.toPlanEntity())
			add(plan2.toPlanEntity())
			add(plan3.toPlanEntity())
		}
		val children = repository.getChildren(parentId)
		Assertions.assertNotNull(children.value)
		Assertions.assertTrue(children.isSuccess)
		Assertions.assertArrayEquals(
			arrayOf<Plan>(plan1, plan3),
			children.value?.toTypedArray()
		)
	}

	@Test
	@DisplayName("insert(vararg Todo): insert 1")
	fun insertVararg1() = runTest {
		val repRes = repository.insert(*arrayOf(plan1))
		Assertions.assertTrue(repRes.isSuccess)
		Assertions.assertArrayEquals(
			arrayOf(plan1),
			plansArray
		)
	}

	@Test
	@DisplayName("insert(vararg Todo): insert 2")
	fun insertVararg2() = runTest {
		val repRes = repository.insert(*arrayOf(plan1, plan2, plan3))
		Assertions.assertTrue(repRes.isSuccess)
		Assertions.assertArrayEquals(
			arrayOf(plan1, plan2, plan3),
			plansArray
		)
	}

	@Test
	@DisplayName("insert(vararg Todo): insert 3")
	fun insertVararg3() = runTest {
		val repRes1 = repository.insert(*arrayOf(plan1, plan2))
		val repRes2 = repository.insert(*arrayOf(plan2, plan3))
		Assertions.assertTrue(repRes1.isSuccess)
		Assertions.assertTrue(repRes2.isSuccess)
		Assertions.assertArrayEquals(
			arrayOf(plan1, plan2, plan3),
			plansArray
		)
	}

	@Test
	@DisplayName("insert(Todo): insert 1")
	fun insert1() = runTest {
		val repRes = repository.insert(plan1)
		Assertions.assertEquals(plan1, repRes.value)
		Assertions.assertTrue(repRes.isSuccess)
		Assertions.assertArrayEquals(
			arrayOf(plan1),
			plansArray
		)
	}

	@Test
	@DisplayName("insert(Todo): insert 2")
	fun insert2() = runTest {
		val title = "Test"
		val repRes = repository.insert(PlanImpl(title = title))
		Assertions.assertEquals(PlanImpl(id = 1, title = title), repRes.value)
		Assertions.assertTrue(repRes.isSuccess)
		Assertions.assertArrayEquals(
			arrayOf(PlanImpl(id = 1, title = title)),
			plansArray
		)
	}

	@Test
	@DisplayName("insert(Todo): insert 3")
	fun insert3() = runTest {
		val repResSuc = repository.insert(plan1)
		val repResFal = repository.insert(plan1)

		Assertions.assertEquals(plan1, repResSuc.value)
		Assertions.assertTrue(repResSuc.isSuccess)

		Assertions.assertNull(repResFal.value)
		Assertions.assertTrue(repResFal.isAlreadyExists)

		Assertions.assertArrayEquals(
			arrayOf(plan1),
			plansArray
		)
	}

	@Test
	@DisplayName("update(vararg Todo): update fail")
	fun updateVarargFail() = runTest {
		plans.add(plan1.toPlanEntity())
		val repRes = repository.update(*arrayOf(plan2))
		Assertions.assertTrue(repRes.isSuccess)
		Assertions.assertArrayEquals(
			arrayOf(plan1),
			plansArray
		)
	}

	@Test
	@DisplayName("update(vararg Todo): update 1")
	fun updateVararg1() = runTest {
		val title = "Title"
		plans.add(plan1.toPlanEntity())
		val repRes = repository.update(
			*arrayOf(
				plan1.toPlan(title = title)
			)
		)

		Assertions.assertTrue(repRes.isSuccess)
		Assertions.assertArrayEquals(
			arrayOf(plan1.toPlan(title = title)),
			plansArray
		)
	}

	@Test
	@DisplayName("update(vararg Todo): update 2")
	fun updateVararg2() = runTest {
		val title = "Title"
		val remark = "Foo"
		plans.apply {
			add(plan1.toPlanEntity())
			add(plan2.toPlanEntity())
			add(plan3.toPlanEntity())
		}

		val repRes = repository.update(
			*arrayOf(
				plan1.toPlan(title = title),
				plan2.toPlan(remark = remark)
			)
		)

		Assertions.assertTrue(repRes.isSuccess)
		Assertions.assertArrayEquals(
			arrayOf(
				plan1.toPlan(title = title),
				plan2.toPlan(remark = remark),
				plan3
			),
			plansArray
		)
	}

	@Test
	@DisplayName("update(Todo, (Todo) -> Todo): update fail")
	fun updateFail() = runTest {
		plans.add(plan1.toPlanEntity())
		val repRes = repository.update(plan2)
		Assertions.assertNull(repRes.value)
		Assertions.assertTrue(repRes.isNotFound)
		Assertions.assertArrayEquals(
			arrayOf(plan1),
			plansArray
		)
	}

	@Test
	@DisplayName("update(Todo, (Todo) -> Todo): update 1")
	fun update1() = runTest {
		val title = "Test title"
		plans.add(plan1.toPlanEntity())
		plans.add(plan2.toPlanEntity())
		val repRes = repository.update(plan2) {
			toPlan(title = title)
		}
		Assertions.assertEquals(
			plan2.toPlan(title = title),
			repRes.value
		)
		Assertions.assertTrue(repRes.isSuccess)
		Assertions.assertArrayEquals(
			arrayOf(
				plan1,
				plan2.toPlan(
					title = title
				)
			),
			plansArray
		)
	}

	@Test
	@DisplayName("update(Todo, (Todo) -> Todo): update 2")
	fun update2() = runTest {
		val title = "Test title"
		plans.add(plan1.toPlanEntity())
		plans.add(plan2.toPlanEntity())
		val repRes = repository.update(plan2.toPlan(title = title))
		Assertions.assertEquals(
			plan2.toPlan(title = title),
			repRes.value
		)
		Assertions.assertTrue(repRes.isSuccess)
		Assertions.assertArrayEquals(
			arrayOf(
				plan1,
				plan2.toPlan(
					title = title
				)
			),
			plansArray
		)
	}

	@Test
	@DisplayName("delete(vararg Todo): delete fail")
	fun deleteVarargDelete1() = runTest {
		plans.apply {
			add(plan1.toPlanEntity())
		}
	    val repRes = repository.delete(*arrayOf(plan2))
		Assertions.assertTrue(repRes.isSuccess)

		Assertions.assertArrayEquals(
			arrayOf(plan1),
			plansArray
		)
	}

	@Test
	@DisplayName("delete(vararg Todo): delete 1")
	fun deleteVararg1() = runTest {
		plans.apply {
			add(plan1.toPlanEntity())
			add(plan2.toPlanEntity())
		}
		val repRes = repository.delete(*arrayOf(plan2))
		Assertions.assertTrue(repRes.isSuccess)

		Assertions.assertArrayEquals(
			arrayOf(plan1),
			plansArray
		)
	}

	@Test
	@DisplayName("delete(Todo) delete fail")
	fun deleteFail() = runTest {
	    plans.add(plan1.toPlanEntity())
		val deleted = repository.delete(plan2)
		Assertions.assertFalse(deleted)
		Assertions.assertArrayEquals(
			arrayOf(plan1),
			plansArray
		)
	}

	@Test
	@DisplayName("delete(Todo) delete 1")
	fun delete1() = runTest {
		plans.apply {
			add(plan1.toPlanEntity())
			add(plan2.toPlanEntity())
		}
		val deleted = repository.delete(plan2)
		Assertions.assertTrue(deleted)
		Assertions.assertArrayEquals(
			arrayOf(plan1),
			plansArray
		)
	}

	@Test
	@DisplayName("deleteAll(): fail")
	fun deleteAllFail() = runTest {
	    repository.deleteAll()
	}

	@Test
	@DisplayName("deleteAll(): delete1")
	fun deleteAll1() = runTest {
	    plans.apply {
			add(plan1.toPlanEntity())
			add(plan2.toPlanEntity())
			add(plan3.toPlanEntity())
		}
		repository.deleteAll()
		Assertions.assertArrayEquals(
			arrayOf(),
			plansArray
		)
	}

}