package kanti.tododer.data.model.progress.datasource

import kanti.tododer.data.common.LocalResult
import kanti.tododer.data.common.localTryCatch
import kanti.tododer.data.model.progress.BaseTodoProgress
import javax.inject.Inject

class TodoProgressRoomDataSource @Inject constructor(
	private val todoProgressDao: TodoProgressDao
) : TodoProgressLocalDataSource by DefaultProgressRoomDataSource(todoProgressDao)