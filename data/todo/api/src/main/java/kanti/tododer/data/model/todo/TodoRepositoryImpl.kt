package kanti.tododer.data.model.todo

import kanti.tododer.data.model.FullId
import kanti.tododer.data.model.FullIdType
import kanti.tododer.data.model.todo.datasource.local.TodoLocalDataSource
import kanti.tododer.util.log.Logger
import kanti.tododer.util.log.StandardLog
import javax.inject.Inject

class TodoRepositoryImpl @Inject constructor(
    private val localDataSource: TodoLocalDataSource,
    @StandardLog private val logger: Logger
) : TodoRepository {

    override suspend fun getTodo(todoId: Long): Todo? {
        val result = localDataSource.getTodo(todoId)
        logger.d(LOG_TAG, "getTodo(Long = $todoId): return $result")
        return result
    }

    override suspend fun getChildren(fullId: FullId, state: TodoState?): List<Todo> {
        val result = localDataSource.getChildren(fullId, state)
        logger.d(
            LOG_TAG,
            "getChildren(FullId = $fullId, TodoState? = $state): return count(${result.size})"
        )
        return result
    }

    override suspend fun getChildrenCount(fullId: FullId, state: TodoState?): Long {
        val result = localDataSource.getChildrenCount(fullId, state)
        logger.d(
            LOG_TAG,
            "getChildrenCount(FullId = $fullId, TodoState? = $state): return $result"
        )
        return result
    }

    override suspend fun deleteChildren(fullId: FullId) {
        val children = localDataSource.getAllChildren(fullId).map { it.id }
        logger.d(
            LOG_TAG,
            "deleteChildren(FullId = $fullId): delete (${children.size}) children"
        )
        delete(children)
    }

    override suspend fun exists(todoId: Long): Boolean {
        val result = getTodo(todoId) != null
        logger.d(LOG_TAG, "exists(Long = $todoId): return $result")
        return result
    }

    override suspend fun create(
        parentFullId: FullId,
        title: String,
        remark: String
    ): Long {
        val todo = Todo(
            parentId = parentFullId,
            title = title,
            remark = remark
        )
        val result = localDataSource.insert(todo)
        logger.d(
            LOG_TAG,
            "create(FullId = $parentFullId, String = $title, String = $remark): return $result"
        )
        return result
    }

    override suspend fun updateTitle(todoId: Long, title: String) {
        localDataSource.updateTitle(todoId, title)
        logger.d(LOG_TAG, "updateTitle(Long = $todoId, String = $title)")
    }

    override suspend fun updateRemark(todoId: Long, remark: String) {
        localDataSource.updateRemark(todoId, remark)
        logger.d(LOG_TAG, "updateRemark(Long = $todoId, String = $remark)")
    }

    override suspend fun changeDone(todoId: Long, isDone: Boolean) {
        localDataSource.changeDone(todoId, isDone)
        logger.d(LOG_TAG, "changeDone(Long = $todoId, Boolean = $isDone)")
    }

    override suspend fun changeDone(todoIds: List<Long>, isDone: Boolean) {
        localDataSource.changeDone(todoIds, isDone)
        logger.d(LOG_TAG, "changeDone(List<Long> = count(${todoIds.size}), Boolean = $isDone)")
    }

    override suspend fun delete(todoIds: List<Long>) {
        for (todoId in todoIds) {
            val fullId = FullId(todoId, FullIdType.Todo)
            val children = getChildren(fullId)
            delete(children.map { it.id })
        }
        localDataSource.delete(todoIds)
        logger.d(LOG_TAG, "delete(List<Long> = count(${todoIds.size}))")
    }

    override suspend fun deleteIfNameIsEmptyAndNoChild(todoId: Long): Boolean {
        val fullId = FullId(todoId, FullIdType.Todo)
        val childrenCount = localDataSource.getChildrenCount(fullId, null)
        val result = if (childrenCount != 0L) false
        else localDataSource.deleteIfNameIsEmpty(todoId)
        logger.d(LOG_TAG, "deleteIfNameIsEmptyAndNoChild(Long = $todoId): return $result")
        return result
    }

    companion object {

        private const val LOG_TAG = "TodoRepositoryImpl"
    }
}