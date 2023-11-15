package kanti.tododer.ui.fragments.screens.todo_root_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kanti.tododer.data.common.UiStateProcess
import kanti.tododer.data.common.UiState
import kanti.tododer.data.common.toUiState
import kanti.tododer.data.model.plan.BasePlan
import kanti.tododer.data.model.plan.PlanRepository
import kanti.tododer.data.model.plan.Plan
import kanti.tododer.data.model.plan.getFromRoot
import kanti.tododer.data.model.plan.insertToRoot
import kanti.tododer.data.model.task.TaskRepository
import kanti.tododer.di.StandardDataQualifier
import kanti.tododer.domain.archiving.ArchiveTodoUseCase
import kanti.tododer.domain.progress.ComputePlanProgressUseCase
import kanti.tododer.domain.deletetodowithchildren.DeleteTodoWithProgenyUseCase
import kanti.tododer.domain.todomove.RepositorySet
import kanti.tododer.common.features.ArchiveTodoFeature
import kanti.tododer.common.features.DeleteTodoFeature
import kanti.tododer.common.features.ComputePlanProgressFeature
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoRootListViewModel @Inject constructor(
	@StandardDataQualifier private val standardPlanRepository: PlanRepository,
	@StandardDataQualifier private val standardTaskRepository: TaskRepository,
	override val computePlanProgressUseCase: ComputePlanProgressUseCase,
	override val deleteTodoWithProgenyUseCase: DeleteTodoWithProgenyUseCase,
	override val archiveTodoUseCase: ArchiveTodoUseCase
) : ViewModel(), DeleteTodoFeature, ArchiveTodoFeature, ComputePlanProgressFeature {

	override val coroutineScope: CoroutineScope
		get() = viewModelScope
	override val repositorySet: RepositorySet
		get() = RepositorySet(
			standardTaskRepository,
			standardPlanRepository
		)

	private val _plansUiStateProcess = UiStateProcess<List<BasePlan>>(listOf())
	private val _plansLiveData = MutableLiveData<UiState<List<BasePlan>>>()
	val plansLiveData: LiveData<UiState<List<BasePlan>>> = _plansLiveData

	private val _newPlanCreated = MutableSharedFlow<UiState<BasePlan>>()
	val newPlanCreated = _newPlanCreated.asSharedFlow()

	init {
		getRootPlans()
	}

	fun getRootPlans() {
		_plansLiveData.value = _plansUiStateProcess.process
		coroutineScope.launch {
			val rootPlans = standardPlanRepository.getFromRoot()
			val uiState = rootPlans.toUiState(listOf())
			_plansLiveData.postValue(uiState)
		}
	}

	fun createNewPlan() {
		coroutineScope.launch {
			val planFromDB = standardPlanRepository.insertToRoot()
			_newPlanCreated.emit(planFromDB.toUiState(Plan.Empty))
		}
	}

}