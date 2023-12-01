package kanti.tododer.ui.fragments.screens.todo_detail.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kanti.tododer.common.logTag
import kanti.tododer.data.model.common.fullid.FullId
import kanti.tododer.data.model.common.fullid.FullIds
import kanti.tododer.data.model.common.Todo
import kanti.tododer.data.model.common.fullid.asFullId
import kanti.tododer.data.model.common.result.GetRepositoryResult
import kanti.tododer.data.model.common.result.asSuccess
import kanti.tododer.data.model.plan.Plan
import kanti.tododer.data.model.plan.PlanRepository
import kanti.tododer.data.model.plan.toPlan
import kanti.tododer.data.model.task.TaskRepository
import kanti.tododer.data.model.task.Task
import kanti.tododer.data.model.task.toTask
import kanti.tododer.domain.gettodowithchildren.GetPlanWithChildrenUseCase
import kanti.tododer.domain.progress.ComputePlanProgressUseCase
import kanti.tododer.domain.gettodowithchildren.GetTaskWithChildrenUseCase
import kanti.tododer.domain.removewithchildren.RemoveTodoWithChildrenUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.EmptyStackException
import java.util.Stack
import javax.inject.Inject

@HiltViewModel
class TodoDetailViewModel @Inject constructor(
	private val getTaskWithChildren: GetTaskWithChildrenUseCase,
	private val getPlanWithChildren: GetPlanWithChildrenUseCase,
	private val computePlanProgressUseCase: ComputePlanProgressUseCase,
	private val removeTodoWithChildrenUseCase: RemoveTodoWithChildrenUseCase,
	private val taskRepository: TaskRepository,
	private val planRepository: PlanRepository
) : ViewModel() {

	private val stack = Stack<FullId>()
	private var currentFullId: FullId? = null

	private val _todoDetail = MutableStateFlow<TodoDetailUiState>(TodoDetailUiState.Empty)
	val todoDetail = _todoDetail.asStateFlow()

	private val _newTodoCreated = MutableSharedFlow<TodoSavedUiState>()
	val newTodoCreated = _newTodoCreated.asSharedFlow()

	val currentTodoType: Todo.Type? get() {
		return currentFullId?.type
	}

	fun saveTitle(todo: Todo, title: String) {
		viewModelScope.launch {
			val fullId = todo.asFullId
			when (fullId.type) {
				Todo.Type.TASK -> saveTask(fullId.id) {
					toTask(
						title = title
					)
				}
				Todo.Type.PLAN -> savePlan(fullId.id) {
					toPlan(
						title = title
					)
				}
			}
		}
	}

	fun saveRemark(todo: Todo, remark: String) {
		viewModelScope.launch {
			val fullId = todo.asFullId
			when (fullId.type) {
				Todo.Type.TASK -> saveTask(fullId.id) {
					toTask(
						remark = remark
					)
				}
				Todo.Type.PLAN -> savePlan(fullId.id) {
					toPlan(
						remark = remark
					)
				}
			}
		}
	}

	fun deleteTodo() {
		if (currentFullId == null) {
			_todoDetail.value = TodoDetailUiState.Empty
			return
		}
		deleteTodo(currentFullId!!)
		pop()
	}

	fun deleteTodo(todo: Todo) {
		viewModelScope.launch {
			removeTodoWithChildrenUseCase(todo)
		}
	}

	fun createNewPlan() {
		viewModelScope.launch {
			if (currentFullId == null) {
				_newTodoCreated.emit(
					TodoSavedUiState.NoTodoInstalled
				)
			}

			val planFromDB = planRepository.insert(
				Plan(
					parentId = currentFullId!!.fullId
				)
			)
			_newTodoCreated.emit(planFromDB.toTodoSavedUiState)
		}
	}

	fun createNewTask() {
		viewModelScope.launch {
			if (currentFullId == null) {
				_newTodoCreated.emit(
					TodoSavedUiState.NoTodoInstalled
				)
			}

			val taskFromDB = taskRepository.insert(
				Task(
					parentId = currentFullId!!.fullId
				)
			)
			_newTodoCreated.emit(taskFromDB.toTodoSavedUiState)
		}
	}

	fun taskIsDone(task: Task, isDone: Boolean) {
		viewModelScope.launch {
			taskRepository.insert(task.toTask(done = isDone))
		}
	}

	fun planProgressRequest(plan: Plan, callback: MutableSharedFlow<GetRepositoryResult<Float>>) {
		viewModelScope.launch {
			computePlanProgressUseCase(plan, callback)
		}
	}

	fun pop() {
		viewModelScope.launch {
			try {
				currentFullId = stack.pop()
				showTodo(currentFullId!!)
			} catch (th: EmptyStackException) {
				_todoDetail.value = TodoDetailUiState.EmptyStack
			} catch (th: Throwable) {
				_todoDetail.value = TodoDetailUiState.Fail(
					message = th.message,
					throwable = th
				)
			}
		}
	}

	fun showTodo(fullId: String) {
		fun log(mes: String = "") = Log.d(logTag, "showTodo(String = \"$fullId\"): $mes")

		viewModelScope.launch {
			_todoDetail.value = TodoDetailUiState.Process
			log(": coroutine start")
			val parsedFullId = FullIds.parseFullId(fullId)
			log("parsedFullId = $parsedFullId")
			if (parsedFullId == null) {
				_todoDetail.value = TodoDetailUiState.InvalidFullId
				return@launch
			}

			showTodoAndAddToStack(parsedFullId)
		}
	}

	fun showTodo(todo: Todo) {
		_todoDetail.value = TodoDetailUiState.Process
		viewModelScope.launch {
			val fullId = todo.asFullId
			showTodoAndAddToStack(fullId)
		}
	}

	fun reshowTodo() {
		currentFullId?.also { fullId ->
			viewModelScope.launch {
				showTodo(fullId)
			}
		}
	}

	private suspend fun showTodoAndAddToStack(fullId: FullId) {
		fun log(mes: String = "") = Log.d(logTag, "addToStack(FullId = \"$fullId\"): $mes")

		val success = showTodo(fullId)
		if (success) {
			log("showTodo(FullId) return success response")
			currentFullId?.apply {
				log("add currentFullId=$currentFullId to stack")
				stack.push(this)
			}
			currentFullId = fullId
		}
	}

	private suspend fun showTodo(fullId: FullId): Boolean {
		val uiState = when (fullId.type) {
			Todo.Type.PLAN -> getPlan(fullId.id)
			Todo.Type.TASK -> getTask(fullId.id)
		}
		Log.d(logTag, "showTodo(FullId = \"$fullId\"): get uiState=$uiState")
		_todoDetail.value = uiState
		return uiState.asSuccess != null
	}

	private suspend fun getTask(id: Int): TodoDetailUiState {
		val repositoryResult = getTaskWithChildren(id)
		Log.d(logTag, "getTask(Int = \"$id\"): gotten task with children (${repositoryResult})")
		return repositoryResult.toTodoDetailUiState
	}

	private suspend fun getPlan(id: Int): TodoDetailUiState {
		val repositoryResult = getPlanWithChildren(id)
		Log.d(logTag, "getPlan(Int = \"$id\"): gotten plan with children (${repositoryResult})")
		return repositoryResult.toTodoDetailUiState
	}

	private suspend fun saveTask(id: Int, body: Task.() -> Task) {
		val task = taskRepository.getTask(id).asSuccess?.value ?: return
		taskRepository.insert(task.body())
	}

	private suspend fun savePlan(id: Int, body: Plan.() -> Plan) {
		val plan = planRepository.getPlan(id).asSuccess?.value ?: return
		planRepository.insert(plan.body())
	}

}