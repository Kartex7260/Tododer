package kanti.tododer.data.model.common.datasource.local

import kanti.tododer.data.common.isAlreadyExists
import kanti.tododer.data.common.isNotFound
import kanti.tododer.data.common.isSuccess
import kanti.tododer.data.model.plan.Plan
import kanti.tododer.data.model.plan.PlanImpl
import kanti.tododer.data.model.plan.datasource.local.FakePlanDao
import kanti.tododer.data.model.plan.datasource.local.FakePlanRoomDataSource
import kanti.tododer.data.model.plan.datasource.local.PlanEntity
import kanti.tododer.data.model.plan.datasource.local.PlanLocalDataSource
import kanti.tododer.data.model.plan.datasource.local.toPlanEntity
import kanti.tododer.data.model.plan.toPlan
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class DefaultTodoDaoDataSourceImplTest {

	private val plans: MutableList<PlanEntity> = mutableListOf()
	private val dataSource: TodoLocalDataSource<Plan> = DefaultTodoDaoDataSourceImpl(
		FakePlanDao(plans)
	)
	private val plansArray: Array<Plan>
		get() = plans.map { it.toPlan() }.toTypedArray()

	private val parentId = "root"

	private val id1 = 1
	private val plan1 = PlanImpl(
		id = id1,
		parentId = parentId
	)
	private val id2 = 2
	private val plan2 = PlanImpl(
		id = id2,
		parentId = "fake_parent_id"
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
	@DisplayName("getTodo(Int): fail 1")
	fun getTodoFail1() = runTest {
		val localResult = dataSource.getTodo(500)
		Assertions.assertNull(localResult.value)
		Assertions.assertTrue(localResult.isNotFound)
	}

	@Test
	@DisplayName("getTodo(Int): fail 2")
	fun getTodoFail2() = runTest {
		plans.add(plan1.toPlanEntity())
		val localResult = dataSource.getTodo(500)
		Assertions.assertNull(localResult.value)
		Assertions.assertTrue(localResult.isNotFound)
	}

	@Test
	@DisplayName("getTodo(Int): 1")
	fun getTodo1() = runTest {
		plans.add(plan1.toPlanEntity())
		val localResult = dataSource.getTodo(id1)
		Assertions.assertTrue(localResult.isSuccess)
		Assertions.assertEquals(
			plan1,
			localResult.value
		)
	}

	@Test
	@DisplayName("getTodo(Int): 2")
	fun getTodo2() = runTest {
		plans.apply {
			add(plan1.toPlanEntity())
			add(plan2.toPlanEntity())
			add(plan3.toPlanEntity())
		}
		val localResult = dataSource.getTodo(id2)
		Assertions.assertTrue(localResult.isSuccess)
		Assertions.assertEquals(
			plan2,
			localResult.value
		)
	}

	@Test
	@DisplayName("getChildren(String): empty result 1")
	fun getChildrenEmptyResult1() = runTest {
		val localResult = dataSource.getChildren(parentId)
		Assertions.assertTrue(localResult.isSuccess)
		Assertions.assertArrayEquals(
			arrayOf<Plan>(),
			localResult.value?.toTypedArray()
		)
	}

	@Test
	@DisplayName("getChildren(String): empty result 2")
	fun getChildrenEmptyResult2() = runTest {
		plans.add(plan2.toPlanEntity())
		val localResult = dataSource.getChildren(parentId)
		Assertions.assertTrue(localResult.isSuccess)
		Assertions.assertArrayEquals(
			arrayOf<Plan>(),
			localResult.value?.toTypedArray()
		)
	}

	@Test
	@DisplayName("getChildren(String): result 1")
	fun getChildrenResult1() = runTest {
		plans.add(plan1.toPlanEntity())
		val localResult = dataSource.getChildren(parentId)
		Assertions.assertTrue(localResult.isSuccess)
		Assertions.assertArrayEquals(
			arrayOf<Plan>(plan1),
			localResult.value?.toTypedArray()
		)
	}

	@Test
	@DisplayName("getChildren(String): result 2")
	fun getChildrenResult2() = runTest {
		plans.apply {
			add(plan1.toPlanEntity())
			add(plan2.toPlanEntity())
			add(plan3.toPlanEntity())
		}
		val localResult = dataSource.getChildren(parentId)
		Assertions.assertTrue(localResult.isSuccess)
		Assertions.assertArrayEquals(
			arrayOf<Plan>(plan1, plan3),
			localResult.value?.toTypedArray()
		)
	}

	@Test
	@DisplayName("insert(vararg Todo): insert 1")
	fun insertVararg1() = runTest {
		val localResult = dataSource.insert(*arrayOf())
		Assertions.assertTrue(localResult.isSuccess)
		Assertions.assertArrayEquals(
			arrayOf(),
			plansArray
		)
	}

	@Test
	@DisplayName("insert(vararg Todo): insert 2")
	fun insertVararg2() = runTest {
		val localResult = dataSource.insert(*arrayOf(plan1))
		Assertions.assertTrue(localResult.isSuccess)
		Assertions.assertArrayEquals(
			arrayOf(plan1),
			plansArray
		)
	}

	@Test
	@DisplayName("insert(vararg Todo): insert 3")
	fun insertVararg3() = runTest {
		val localResult = dataSource.insert(*arrayOf(plan1, plan2, plan3))
		Assertions.assertTrue(localResult.isSuccess)
		Assertions.assertArrayEquals(
			arrayOf(plan1, plan2, plan3),
			plansArray
		)
	}

	@Test
	@DisplayName("insert(vararg Todo): insert 4")
	fun insertVararg4() = runTest {
		val localResult1 = dataSource.insert(*arrayOf(plan1, plan2))
		val localResult2 = dataSource.insert(*arrayOf(plan2, plan3))
		Assertions.assertTrue(localResult1.isSuccess)
		Assertions.assertTrue(localResult2.isSuccess)
		Assertions.assertArrayEquals(
			arrayOf(plan1, plan2, plan3),
			plansArray
		)
	}

	@Test
	@DisplayName("insert(Todo): fail insert 1")
	fun insertFail1() = runTest {
		plans.add(plan1.toPlanEntity())
		val localResult = dataSource.insert(plan1)
		Assertions.assertTrue(localResult.isAlreadyExists)
		Assertions.assertNull(localResult.value)
		Assertions.assertArrayEquals(
			arrayOf(plan1),
			plansArray
		)
	}

	@Test
	@DisplayName("insert(Todo): insert 1")
	fun insert1() = runTest {
		val localResult = dataSource.insert(plan1)
		Assertions.assertTrue(localResult.isSuccess)
		Assertions.assertEquals(plan1, localResult.value)
	}

	@Test
	@DisplayName("insert(Todo): insert 2")
	fun insert2() = runTest {
		val localResult = dataSource.insert(PlanImpl(title = "Test"))
		Assertions.assertTrue(localResult.isSuccess)
		Assertions.assertEquals(
			PlanImpl(id = 1, title = "Test"),
			localResult.value
		)
	}

	@Test
	@DisplayName("update(vararg Todo): update 1")
	fun updateVararg1() = runTest {
		val updated = dataSource.update(*arrayOf(plan1))
		Assertions.assertTrue(updated.isSuccess)
		Assertions.assertArrayEquals(
			arrayOf(),
			plansArray
		)
	}

	@Test
	@DisplayName("update(vararg Todo): update 2")
	fun updateVararg2() = runTest {
		plans.add(plan2.toPlanEntity())
		val updated = dataSource.update(*arrayOf(plan1))
		Assertions.assertTrue(updated.isSuccess)
		Assertions.assertArrayEquals(
			arrayOf(plan2),
			plansArray
		)
	}

	@Test
	@DisplayName("update(vararg Todo): update 3")
	fun updateVararg3() = runTest {
		plans.add(plan1.toPlanEntity())
		val updated = dataSource.update(*arrayOf(
			plan1.toPlan(title = "Test")
		))
		Assertions.assertTrue(updated.isSuccess)
		Assertions.assertArrayEquals(
			arrayOf(plan1.toPlan(title = "Test")),
			plansArray
		)
	}

	@Test
	@DisplayName("update(vararg Todo): update 4")
	fun updateVararg4() = runTest {
		plans.apply {
			add(plan1.toPlanEntity())
			add(plan2.toPlanEntity())
			add(plan3.toPlanEntity())
		}
		val updated = dataSource.update(*arrayOf(
			plan2.toPlan(remark = "Test"),
			plan3.toPlan(title = "Foo")
		))
		Assertions.assertTrue(updated.isSuccess)
		Assertions.assertArrayEquals(
			arrayOf(
				plan1,
				plan2.toPlan(remark = "Test"),
				plan3.toPlan(title = "Foo")
			),
			plansArray
		)
	}

	@Test
	@DisplayName("update(Todo): update 1")
	fun update1() = runTest {
		val updated = dataSource.update(plan1)
		Assertions.assertNull(updated.value)
		Assertions.assertTrue(updated.isNotFound)
	}

	@Test
	@DisplayName("update(Todo): update 2")
	fun update2() = runTest {
		plans.add(plan1.toPlanEntity())
		val updated = dataSource.update(plan1.toPlan(title = "Test"))
		Assertions.assertTrue(updated.isSuccess)
		Assertions.assertEquals(
			plan1.toPlan(title = "Test"),
			updated.value
		)
		Assertions.assertArrayEquals(
			arrayOf(plan1.toPlan(title = "Test")),
			plansArray
		)
	}

	@Test
	@DisplayName("update(Todo): update 3")
	fun update3() = runTest {
		plans.apply {
			add(plan1.toPlanEntity())
			add(plan2.toPlanEntity())
			add(plan3.toPlanEntity())
		}
		val updated = dataSource.update(plan2.toPlan(title = "Test"))
		Assertions.assertTrue(updated.isSuccess)
		Assertions.assertEquals(
			plan2.toPlan(title = "Test"),
			updated.value
		)
		Assertions.assertArrayEquals(
			arrayOf(
				plan1,
				plan2.toPlan(title = "Test"),
				plan3
			),
			plansArray
		)
	}

	@Test
	@DisplayName("delete(vararg Todo): delete 1")
	fun deleteVararg1() = runTest {
		val updated = dataSource.delete(*arrayOf(plan1))
		Assertions.assertTrue(updated.isSuccess)
		Assertions.assertArrayEquals(
			arrayOf(),
			plansArray
		)
	}

	@Test
	@DisplayName("delete(vararg Todo): delete 2")
	fun deleteVararg2() = runTest {
		plans.add(plan2.toPlanEntity())
		val updated = dataSource.delete(*arrayOf(plan1))
		Assertions.assertTrue(updated.isSuccess)
		Assertions.assertArrayEquals(
			arrayOf(plan2),
			plansArray
		)
	}

	@Test
	@DisplayName("delete(vararg Todo): delete 3")
	fun deleteVararg3() = runTest {
		plans.add(plan1.toPlanEntity())
		val updated = dataSource.delete(*arrayOf(plan1))
		Assertions.assertTrue(updated.isSuccess)
		Assertions.assertArrayEquals(
			arrayOf(),
			plansArray
		)
	}

	@Test
	@DisplayName("delete(vararg Todo): delete 4")
	fun deleteVararg4() = runTest {
		plans.apply {
			add(plan1.toPlanEntity())
			add(plan2.toPlanEntity())
			add(plan3.toPlanEntity())
		}
		val updated = dataSource.delete(*arrayOf(
			plan2,
			plan3
		))
		Assertions.assertTrue(updated.isSuccess)
		Assertions.assertArrayEquals(
			arrayOf(
				plan1
			),
			plansArray
		)
	}

	@Test
	@DisplayName("delete(Todo): delete 1")
	fun delete1() = runTest {
		val deleted = dataSource.delete(plan1)
		Assertions.assertFalse(deleted)
	}

	@Test
	@DisplayName("delete(Todo): delete 2")
	fun delete2() = runTest {
		plans.add(plan1.toPlanEntity())
		val deleted = dataSource.delete(plan1)
		Assertions.assertTrue(deleted)
		Assertions.assertArrayEquals(
			arrayOf(),
			plansArray
		)
	}

	@Test
	@DisplayName("delete(Todo): delete 3")
	fun delete3() = runTest {
		plans.apply {
			add(plan1.toPlanEntity())
			add(plan2.toPlanEntity())
			add(plan3.toPlanEntity())
		}
		val deleted = dataSource.delete(plan2)
		Assertions.assertTrue(deleted)
		Assertions.assertArrayEquals(
			arrayOf(
				plan1,
				plan3
			),
			plansArray
		)
	}

	@Test
	@DisplayName("deleteAll(): delete 1")
	fun deleteAll1() = runTest {
		dataSource.deleteAll()
	}

	@Test
	@DisplayName("deleteAll(): delete 2")
	fun deleteAll2() = runTest {
		plans.apply {
			add(plan1.toPlanEntity())
			add(plan2.toPlanEntity())
			add(plan3.toPlanEntity())
		}
		dataSource.deleteAll()
		Assertions.assertArrayEquals(
			arrayOf(),
			plansArray
		)
	}

}