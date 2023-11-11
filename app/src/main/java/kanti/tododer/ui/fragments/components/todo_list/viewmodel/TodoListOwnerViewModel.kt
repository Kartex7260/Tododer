package kanti.tododer.ui.fragments.components.todo_list.viewmodel

import android.view.ContextMenu
import androidx.lifecycle.LiveData
import kanti.tododer.data.model.common.Todo
import kanti.tododer.data.model.plan.BasePlan
import kanti.tododer.data.model.task.BaseTask
import kanti.tododer.ui.common.viewholder.TodoViewHolder
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface TodoListOwnerViewModel {

	var todoViewHolderFactory: TodoViewHolder.Factory

	val todoList: StateFlow<List<Todo>>

	val todoArchived: SharedFlow<Todo>

	fun deleteTodo(todo: Todo)

	fun elementClick(todo: Todo)

	fun taskIsDone(task: BaseTask, done: Boolean)

	fun progressRequest(plan: BasePlan): LiveData<Float>

	fun todoItemCreateContextMenu(todo: Todo, menu: ContextMenu)

}