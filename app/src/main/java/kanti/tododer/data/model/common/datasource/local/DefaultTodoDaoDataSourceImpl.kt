package kanti.tododer.data.model.common.datasource.local

import kanti.tododer.data.common.LocalResult
import kanti.tododer.data.common.localTryCatch

class DefaultTodoDaoDataSourceImpl<Todo>(
	private val planDao: TodoDao<Todo>
) : TodoLocalDataSource<Todo> where Todo : kanti.tododer.data.model.common.Todo {

	override suspend fun getTodo(id: Int): LocalResult<Todo> {
		return localTryCatch {
			val planFromDS = planDao.getTodo(id)
			LocalResult(
				value = planFromDS,
				type = if (planFromDS == null) {
					LocalResult.Type.NotFound(id.toString())
				} else {
					LocalResult.Type.Success
				}
			)
		}
	}

	override suspend fun getChildren(fid: String): LocalResult<List<Todo>> {
		return localTryCatch {
			val children = planDao.getChildren(fid)
			LocalResult(
				value = children
			)
		}
	}

	override suspend fun insert(vararg todo: Todo): LocalResult<Unit> {
		return localTryCatch {
			planDao.insert(*todo)
			LocalResult()
		}
	}

	override suspend fun insert(todo: Todo): LocalResult<Todo> {
		return localTryCatch {
			val planRowId = planDao.insert(todo)
			if (planRowId == -1L) {
				return@localTryCatch LocalResult(
					type = LocalResult.Type.AlreadyExists(todo.fullId)
				)
			}
			val planFromDB = planDao.getByRowId(planRowId)!!
			LocalResult(planFromDB)
		}
	}

	override suspend fun update(vararg todo: Todo): LocalResult<Unit> {
		return localTryCatch {
			planDao.update(*todo)
			LocalResult()
		}
	}

	override suspend fun update(todo: Todo): LocalResult<Todo> {
		return localTryCatch {
			val updated = planDao.update(todo)
			if (!updated) {
				return@localTryCatch LocalResult(
					type = LocalResult.Type.NotFound(todo.fullId)
				)
			}
			val planFromDB = planDao.getTodo(todo.id)!!
			LocalResult(planFromDB)
		}
	}

	override suspend fun delete(vararg todo: Todo): LocalResult<Unit> {
		return localTryCatch {
			planDao.delete(*todo)
			LocalResult()
		}
	}

	override suspend fun delete(todo: Todo): Boolean {
		return try {
			planDao.delete(todo)
		} catch (th: Throwable) {
			false
		}
	}

	override suspend fun deleteAll() {
		planDao.deleteAll()
	}

}