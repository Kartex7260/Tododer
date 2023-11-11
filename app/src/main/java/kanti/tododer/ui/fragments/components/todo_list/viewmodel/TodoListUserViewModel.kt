package kanti.tododer.ui.fragments.components.todo_list.viewmodel

import kanti.tododer.data.model.common.Todo
import kanti.tododer.ui.common.viewholder.TodoViewHolder
import kanti.tododer.ui.fragments.components.common.PlanProgressRequest
import kanti.tododer.ui.fragments.components.common.SaveTodoDataRequest
import kotlinx.coroutines.flow.SharedFlow

interface TodoListUserViewModel {

	var todoViewHolderFactory: TodoViewHolder.Factory

	val elementClick: SharedFlow<Todo>

	val taskIsDone: SharedFlow<SaveTodoDataRequest<Boolean>>

	val planProgressRequest: SharedFlow<PlanProgressRequest>

	val deleteTodo: SharedFlow<DeleteTodoRequest>

	val todoItemCreateContextMenu: SharedFlow<TodoItemCreateContextMenuRequest>

	fun sendTodoList(list: List<Todo> = listOf())

}