package kanti.tododer.data.model.task.datasource.local

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class FakeTaskDaoTest : TaskDaoTest {

	private val test: TaskDaoTest = BaseTaskDaoTest(FakeTaskDao())

	@AfterEach
	override fun afterEach() {
		test.afterEach()
	}

	@Test
	override fun getAll1() {
		test.getAll1()
	}

	@Test
	override fun getAll2() {
		test.getAll2()
	}

	@Test
	override fun getChildren1() {
		test.getChildren1()
	}

	@Test
	override fun getChildren2() {
		test.getChildren2()
	}

	@Test
	override fun getByRowId1() {
		test.getByRowId1()
	}

	@Test
	override fun getByRowId2() {
		test.getByRowId2()
	}

	@Test
	override fun getTask1() {
		test.getTask1()
	}

	@Test
	override fun getTask2() {
		test.getTask2()
	}

	@Test
	override fun insert1() {
		test.insert1()
	}

	@Test
	override fun insert2() {
		test.insert2()
	}

	@Test
	override fun delete1() {
		test.delete1()
	}

	@Test
	override fun delete2() {
		test.delete2()
	}

	@Test
	override fun deleteAll() {
		test.deleteAll()
	}
}