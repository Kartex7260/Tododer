package kanti.tododer.ui.screen.todo_detail.viewmodel

import android.util.Log
import kanti.tododer.ui.common.GroupUiState
import kanti.tododer.ui.common.TodoDataWithGroup
import kanti.tododer.ui.common.TodoUiState
import kanti.tododer.ui.common.TodosUiState
import kanti.tododer.ui.components.todo.TodoData
import kanti.tododer.ui.screen.todo_list.viewmodel.TodoDeletion
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

interface TodoDetailViewModel {

    val todoDetail: StateFlow<TodoData>
    val todoChildren: StateFlow<TodosUiState>

    val childrenTodosDeleted: SharedFlow<List<TodoDeletion>>
    val blankTodoDeleted: SharedFlow<Unit>

    val toNext: SharedFlow<Long>
    val onExit: SharedFlow<TodoData?>

    val groupSelected: SharedFlow<List<TodoDataWithGroup>>

    fun show(todoId: Long)

    fun reshow(todoId: Long?)

    fun createNewTodo(title: String, goTo: Boolean)

    fun setGroup(todoIds: List<Long>, group: String?)

    fun renameTodo(todoId: Long, newTitle: String)

    fun changeTitle(title: String)

    fun changeRemark(remark: String)

    fun changeDoneCurrent(isDone: Boolean)

    fun changeDoneChild(todoId: Long, isDone: Boolean)

    fun deleteCurrent()

    fun deleteChildren(todos: List<TodoData>)

    fun cancelDeleteChildren()

    fun rejectCancelDelete()

    fun switchSelection()

    fun selection(todoId: Long)

    fun selectionOff(): Boolean

    fun setSelect(todoId: Long, selected: Boolean)

    fun setSelect(group: String?, selected: Boolean)

    fun groupSelected()

    fun changeDoneSelected()

    fun deleteSelected()

    fun onStop()

    companion object : TodoDetailViewModel {

        private const val logTag = "TodoDetailViewModel"

        private val coroutineScope = CoroutineScope(Dispatchers.Main)

        private val _todoDetail = MutableStateFlow(TodoData())
        override val todoDetail: StateFlow<TodoData> = _todoDetail.asStateFlow()

        private val _childrenTodoDeleted = MutableSharedFlow<List<TodoDeletion>>()
        override val childrenTodosDeleted: SharedFlow<List<TodoDeletion>> =
            _childrenTodoDeleted.asSharedFlow()

        override val blankTodoDeleted: SharedFlow<Unit> = MutableSharedFlow()

        private val _todoChildren = MutableStateFlow(
            TodosUiState(
                groups = listOf(
                    GroupUiState(
                        name = "Group 1",
                        todos = listOf(
                            TodoUiState(data = TodoData(id = 2, title = "Test 1")),
                            TodoUiState(data = TodoData(id = 3, title = "Test 2")),
                            TodoUiState(data = TodoData(id = 4, title = "Test 3")),
                            TodoUiState(data = TodoData(id = 5, title = "Test 4"))
                        )
                    ),
                    GroupUiState(
                        name = null,
                        todos = listOf(
                            TodoUiState(data = TodoData(id = 6, title = "Test 5")),
                            TodoUiState(data = TodoData(id = 7, title = "Test 6")),
                            TodoUiState(data = TodoData(id = 8, title = "Test 7")),
                            TodoUiState(data = TodoData(id = 9, title = "Test 8")),
                            TodoUiState(data = TodoData(id = 10, title = "Test 9")),
                            TodoUiState(data = TodoData(id = 11, title = "Test 10")),
                            TodoUiState(data = TodoData(id = 12, title = "Test 11")),
                            TodoUiState(data = TodoData(id = 13, title = "Test 12")),
                        )
                    )
                )
            )
        )
        override val todoChildren: StateFlow<TodosUiState> = _todoChildren.asStateFlow()

        override val toNext: SharedFlow<Long> = MutableSharedFlow()
        override val onExit: SharedFlow<TodoData?> = MutableSharedFlow()

        override val groupSelected: SharedFlow<List<TodoDataWithGroup>> = MutableSharedFlow()

        override fun show(todoId: Long) {
            Log.d(logTag, "show(Long = $todoId)")
        }

        override fun reshow(todoId: Long?) {
            Log.d(logTag, "reshow(todoId: Long?)")
        }

        override fun createNewTodo(title: String, goTo: Boolean) {
            Log.d(logTag, "createNewTodo(String = $title, Boolean = $goTo)")
        }

        override fun setGroup(todoIds: List<Long>, group: String?) {
            Log.d(logTag, "setGrgoup(List<Long> = $todoIds, String? = $group)")
        }

        override fun renameTodo(todoId: Long, newTitle: String) {
            Log.d(logTag, "renameTodo(todoId: Long, newTitle: String)")
        }

        override fun changeTitle(title: String) {
            Log.d(logTag, "changeTitle(title: String = $title)")
            _todoDetail.value = _todoDetail.value.copy()
        }

        override fun changeRemark(remark: String) {
            Log.d(logTag, "changeRemark(remark: String = $remark)")
            _todoDetail.value = _todoDetail.value.copy()
        }

        override fun changeDoneCurrent(isDone: Boolean) {
            Log.d(logTag, "changeDoneCurrent(isDone: Boolean = $isDone)")
            _todoDetail.value = _todoDetail.value.copy()
        }

        override fun changeDoneChild(todoId: Long, isDone: Boolean) {
            Log.d(logTag, "changeDoneChild(todoId: Int = $todoId, isDone: Boolean = $isDone)")
        }

        override fun deleteCurrent() {
            Log.d(logTag, "deleteCurrent()")
            coroutineScope.launch {
                _childrenTodoDeleted.emit(
                    listOf(
                        TodoDeletion(
                            TodoData(title = "Current todo"),
                            true
                        )
                    )
                )
            }
        }

        override fun deleteChildren(todos: List<TodoData>) {
            Log.d(logTag, "deleteChild(todoId: Int = $todos)")
            coroutineScope.launch {
                _childrenTodoDeleted.emit(
                    listOf(
                        TodoDeletion(
                            TodoData(title = "Child todo"),
                            false
                        )
                    )
                )
            }
        }

        override fun cancelDeleteChildren() {
            Log.d(logTag, "undoDelete()")
        }

        override fun rejectCancelDelete() {
            Log.d(logTag, "rejectCancelDelete()")
        }

        override fun switchSelection() {
            Log.d(logTag, "switchSelection()")
        }

        override fun selection(todoId: Long) {
            Log.d(logTag, "selection(Long = $todoId)")
        }

        override fun selectionOff(): Boolean {
            Log.d(logTag, "selectionOff(): return false")
            return false
        }

        override fun setSelect(todoId: Long, selected: Boolean) {
            Log.d(logTag, "setSelect(Long = $todoId, Boolean = $selected)")
        }

        override fun setSelect(group: String?, selected: Boolean) {
            Log.d(logTag, "setSelect(String? = $group, Boolean = $selected")
        }

        override fun groupSelected() {
            Log.d(logTag, "groupSelected()")
        }

        override fun changeDoneSelected() {
            Log.d(logTag, "changeDoneSelected()")
        }

        override fun deleteSelected() {
            Log.d(logTag, "deleteSelected()")
        }

        override fun onStop() {
            Log.d(logTag, "onStop()")
        }
    }
}