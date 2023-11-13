package kanti.tododer.ui.fragments.screens.todo_root_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kanti.tododer.data.common.UiStateProcess
import kanti.tododer.data.common.UiState
import kanti.tododer.data.common.toUiState
import kanti.tododer.data.model.common.Todo
import kanti.tododer.data.model.plan.BasePlan
import kanti.tododer.data.model.plan.PlanRepository
import kanti.tododer.data.model.plan.Plan
import kanti.tododer.data.model.plan.getFromRoot
import kanti.tododer.data.model.plan.insertToRoot
import kanti.tododer.data.model.task.TaskRepository
import kanti.tododer.di.ArchiveDataQualifier
import kanti.tododer.di.StandardDataQualifier
import kanti.tododer.domain.todomove.MoveTodoUseCase
import kanti.tododer.domain.progress.ComputePlanProgressUseCase
import kanti.tododer.domain.removewithchildren.RemoveTodoWithProgenyUseCase
import kanti.tododer.domain.todomove.RepositorySet
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoRootListViewModel @Inject constructor(
	@StandardDataQualifier private val standardPlanRepository: PlanRepository,
	@ArchiveDataQualifier private val archivePlanRepository: PlanRepository,
	@StandardDataQualifier private val standardTaskRepository: TaskRepository,
	@ArchiveDataQualifier private val archiveTaskRepository: TaskRepository,
	private val computePlanProgressUseCase: ComputePlanProgressUseCase,
	private val removeTodoWithProgenyUseCase: RemoveTodoWithProgenyUseCase,
	private val moveTodoUseCase: MoveTodoUseCase
) : ViewModel() {

	private val _plansUiStateProcess = UiStateProcess<List<BasePlan>>(listOf())
	private val _plansLiveData = MutableLiveData<UiState<List<BasePlan>>>()
	val plansLiveData: LiveData<UiState<List<BasePlan>>> = _plansLiveData

	private val _newPlanCreated = MutableSharedFlow<UiState<BasePlan>>()
	val newPlanCreated = _newPlanCreated.asSharedFlow()

	init {
		getRootPlans()
	}

	fun toArchive(todo: Todo) {
		viewModelScope.launch(NonCancellable) {
			moveTodoUseCase(
				from = RepositorySet(
					standardTaskRepository,
					standardPlanRepository
				),
				to = RepositorySet(
					archiveTaskRepository,
					archivePlanRepository
				),
				todo = todo
			)
		}
	}

	fun deleteTodo(todo: Todo) {
		viewModelScope.launch {
			removeTodoWithProgenyUseCase(todo)
		}
	}

	fun getRootPlans() {
		_plansLiveData.value = _plansUiStateProcess.process
		viewModelScope.launch {
			val rootPlans = standardPlanRepository.getFromRoot()
			val uiState = rootPlans.toUiState(listOf())
			_plansLiveData.postValue(uiState)
		}
	}

	fun planProgressRequest(plan: BasePlan, callback: MutableLiveData<Float>) {
		viewModelScope.launch {
			computePlanProgressUseCase(plan, callback)
		}
	}

	fun createNewPlan() {
		viewModelScope.launch {
			val planFromDB = standardPlanRepository.insertToRoot()
			_newPlanCreated.emit(planFromDB.toUiState(Plan.Empty))
		}
	}

}