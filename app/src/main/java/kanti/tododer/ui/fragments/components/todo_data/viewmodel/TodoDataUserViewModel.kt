package kanti.tododer.ui.fragments.components.todo_data.viewmodel

import androidx.lifecycle.LifecycleOwner
import kanti.tododer.data.model.common.Todo
import kanti.tododer.ui.fragments.components.common.PlanProgressRequest
import kanti.tododer.ui.fragments.components.common.SaveTodoDataRequest
import kotlinx.coroutines.flow.SharedFlow

interface TodoDataUserViewModel {

	val taskIsDone: SharedFlow<SaveTodoDataRequest<Boolean>>

	val planProgressRequest: SharedFlow<PlanProgressRequest>

	val saveNewTitle: SharedFlow<SaveTodoDataRequest<String>>

	val saveNewRemark: SharedFlow<SaveTodoDataRequest<String>>

	fun sendTodo(todoElement: Todo? = null)

	fun updateStateView()

}