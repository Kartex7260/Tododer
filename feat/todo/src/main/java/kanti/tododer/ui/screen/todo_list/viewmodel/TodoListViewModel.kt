package kanti.tododer.ui.screen.todo_list.viewmodel

import kanti.tododer.data.model.plan.Plan
import kanti.tododer.data.model.plan.PlanType
import kanti.tododer.ui.common.GroupUiState
import kanti.tododer.ui.common.TodoDataWithGroup
import kanti.tododer.ui.common.TodoUiState
import kanti.tododer.ui.common.TodosUiState
import kanti.tododer.ui.components.todo.TodoData
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

interface TodoListViewModel {

    val currentPlan: StateFlow<TodoListUiState>

    val todosDeleted: SharedFlow<List<TodoDeletion>>
    val blankTodoDeleted: SharedFlow<Unit>

    val goToTodo: SharedFlow<Long>

    val groupingSelection: SharedFlow<List<TodoDataWithGroup>>

    fun updateUiState(deletedTodoId: Long?) {}

    fun createNewTodo(title: String, goTo: Boolean) {}

    fun setGroup(todoIds: List<Long>, group: String?) {}

    fun setGroupExpand(group: String?, expand: Boolean) {}

    fun setGroupDone(group: String?, isDone: Boolean) {}

    fun renameGroup(group: String?) {}

    fun ungroup(group: String) {}

    fun deleteGroup(group: String?) {}

    fun renamePlan(newTitle: String) {}

    fun renameTodo(todoId: Long, newTitle: String) {}

    fun changeDone(todoId: Long, isDone: Boolean) {}

    fun deleteTodos(todos: List<TodoData>) {}

    fun cancelDelete() {}

    fun rejectCancelChance() {}

    fun switchSelection() {}

    fun selection(todoId: Long) {}

    fun selection(group: String?) {}

    fun selectionOff(): Boolean { return false }

    fun setSelect(todoId: Long, selected: Boolean) {}

    fun setSelect(group: String?, selected: Boolean) {}

    fun groupingSelection() {}

    fun changeDoneSelected() {}

    fun deleteSelected() {}

    fun onStop() {}

    companion object : TodoListViewModel {

        private val _children = MutableStateFlow(
            TodoListUiState(
                plan = Plan(1, title = "Test", type = PlanType.Custom),
                children = TodosUiState(
                    groups = listOf(
                        GroupUiState(
                            name = "Group 1",
                            expand = true,
                            todos = listOf(
                                TodoUiState(data = TodoData(1, "Test 1")),
                                TodoUiState(data = TodoData(2, "Test 2")),
                                TodoUiState(data = TodoData(3, "Test 3"))
                            )
                        ),
                        GroupUiState(
                            name = "Group 2",
                            expand = true,
                            todos = listOf(
                                TodoUiState(data = TodoData(4, "Test 4")),
                                TodoUiState(data = TodoData(5, "Test 5")),
                                TodoUiState(data = TodoData(6, "Test 6")),
                                TodoUiState(data = TodoData(7, "Test 7")),
                                TodoUiState(data = TodoData(8, "Test 8"))
                            )
                        ),
                        GroupUiState(
                            name = null,
                            expand = true,
                            todos = listOf(
                                TodoUiState(data = TodoData(9, "Test 9")),
                                TodoUiState(data = TodoData(10, "Test 10")),
                                TodoUiState(data = TodoData(11, "Test 11")),
                                TodoUiState(data = TodoData(12, "Test 12")),
                                TodoUiState(data = TodoData(13, "Test 13")),
                                TodoUiState(data = TodoData(14, "Test 14"))
                            )
                        )
                    )
                )
            )
        )
        override val currentPlan = _children.asStateFlow()

        private val _todoDeleted = MutableSharedFlow<List<TodoDeletion>>()
        override val todosDeleted: SharedFlow<List<TodoDeletion>> = _todoDeleted.asSharedFlow()

        override val blankTodoDeleted: SharedFlow<Unit> = MutableSharedFlow()

        private val _newTodoCreated = MutableSharedFlow<Long>()
        override val goToTodo: SharedFlow<Long> = _newTodoCreated.asSharedFlow()

        override val groupingSelection: SharedFlow<List<TodoDataWithGroup>> = MutableSharedFlow()
    }
}