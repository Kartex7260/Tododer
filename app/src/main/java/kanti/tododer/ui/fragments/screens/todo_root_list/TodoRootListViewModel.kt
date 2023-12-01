package kanti.tododer.ui.fragments.screens.todo_root_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kanti.tododer.data.model.common.Todo
import kanti.tododer.data.model.common.result.GetRepositoryResult
import kanti.tododer.data.model.common.result.ResultUiState
import kanti.tododer.data.model.common.result.asUiState
import kanti.tododer.data.model.plan.Plan
import kanti.tododer.data.model.plan.PlanRepository
import kanti.tododer.data.model.plan.getFromRoot
import kanti.tododer.data.model.plan.insertToRoot
import kanti.tododer.domain.progress.ComputePlanProgressUseCase
import kanti.tododer.domain.removewithchildren.RemoveTodoWithChildrenUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoRootListViewModel @Inject constructor(
	private val planRepository: PlanRepository,
	private val computePlanProgressUseCase: ComputePlanProgressUseCase,
	private val removeTodoWithChildrenUseCase: RemoveTodoWithChildrenUseCase
) : ViewModel() {

	private val _plansLiveData = MutableLiveData<ResultUiState<List<Plan>>>()
	val plansLiveData: LiveData<ResultUiState<List<Plan>>> = _plansLiveData

	private val _newPlanCreated = MutableSharedFlow<ResultUiState<Plan>>()
	val newPlanCreated = _newPlanCreated.asSharedFlow()

	init {
		getRootPlans()
	}

	fun deleteTodo(todo: Todo) {
		viewModelScope.launch {
			removeTodoWithChildrenUseCase(todo)
		}
	}

	fun getRootPlans() {
		_plansLiveData.value = ResultUiState.Process()
		viewModelScope.launch {
			val rootPlans = planRepository.getFromRoot()
			val uiState = rootPlans.asUiState
			_plansLiveData.postValue(uiState)
		}
	}

	fun planProgressRequest(
		plan: Plan,
		callback: MutableSharedFlow<GetRepositoryResult<Float>>
	) {
		viewModelScope.launch {
			computePlanProgressUseCase(plan, callback)
		}
	}

	fun createNewPlan() {
		viewModelScope.launch {
			val planFromDB = planRepository.insertToRoot()
			_newPlanCreated.emit(planFromDB.asUiState)
		}
	}

}