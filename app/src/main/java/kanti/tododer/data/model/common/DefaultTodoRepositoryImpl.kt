package kanti.tododer.data.model.common

import kanti.tododer.data.common.RepositoryResult
import kanti.tododer.data.common.toRepositoryResult
import kanti.tododer.data.model.common.datasource.local.TodoLocalDataSource

class DefaultTodoRepositoryImpl<Todo>(
	private val localDataSource: TodoLocalDataSource<Todo>
) : TodoRepository<Todo> where Todo : kanti.tododer.data.model.common.Todo {

	override suspend fun getTodo(id: Int): RepositoryResult<Todo> {
		return localDataSource.getTodo(id).toRepositoryResult()
	}

	override suspend fun getChildren(fid: String): RepositoryResult<List<Todo>> {
		return localDataSource.getChildren(fid).toRepositoryResult()
	}

	override suspend fun insert(vararg todo: Todo): RepositoryResult<Unit> {
		return localDataSource.insert(*todo).toRepositoryResult()
	}

	override suspend fun insert(task: Todo): RepositoryResult<Todo> {
		return localDataSource.insert(task).toRepositoryResult()
	}

	override suspend fun update(vararg todo: Todo): RepositoryResult<Unit> {
		return localDataSource.update(*todo).toRepositoryResult()
	}

	override suspend fun update(
		todo: Todo,
		update: (Todo.() -> Todo)?
	): RepositoryResult<Todo> {
		val updatedPlan = update?.let { todo.it() } ?: todo
		return localDataSource.update(updatedPlan).toRepositoryResult()
	}

	override suspend fun delete(vararg task: Todo): RepositoryResult<Unit> {
		return localDataSource.delete(*task).toRepositoryResult()
	}

	override suspend fun delete(task: Todo): Boolean {
		return localDataSource.delete(task)
	}

	override suspend fun deleteAll() = localDataSource.deleteAll()

}