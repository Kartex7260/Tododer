package kanti.tododer.ui.screen.todo_list.viewmodel

import kanti.tododer.ui.components.todo.TodoData

data class TodoDeletion(
    val todoData: TodoData,
    val returnToChild: Boolean
)
