package kanti.tododer.ui.fragments.screens.todo_root_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kanti.tododer.data.common.UiStateProcess
import kanti.tododer.data.common.UiState
import kanti.tododer.data.common.toUiState
import kanti.tododer.data.model.plan.IPlanRepository
import kanti.tododer.data.model.plan.Plan
import kanti.tododer.data.model.plan.getFromRoot
import kanti.tododer.data.model.plan.insertToRoot
import kanti.tododer.domain.progress.ComputePlanProgressUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoRootListViewModel @Inject constructor(
	private val planRepository: IPlanRepository,
	private val computePlanProgressUseCase: ComputePlanProgressUseCase
) : ViewModel() {

	private val _plansUiStateProcess = UiStateProcess<List<Plan>>(listOf())
	private val _plansLiveData = MutableLiveData<UiState<List<Plan>>>()
	val plansLiveData: LiveData<UiState<List<Plan>>> = _plansLiveData

	private val _newPlanCreated = MutableSharedFlow<UiState<Plan>>()
	val newPlanCreated = _newPlanCreated.asSharedFlow()

	init {
		getRootPlans()
	}

	fun getRootPlans() {
		_plansLiveData.value = _plansUiStateProcess.process
		viewModelScope.launch {
			val rootPlans = planRepository.getFromRoot()
			val uiState = rootPlans.toUiState(listOf())
			_plansLiveData.postValue(uiState)
		}
	}

	fun planProgressRequest(plan: Plan, callback: MutableLiveData<Float>) {
		viewModelScope.launch {
			computePlanProgressUseCase(plan, callback)
		}
	}

	fun createNewPlan() {
		viewModelScope.launch {
			val planFromDB = planRepository.insertToRoot()
			_newPlanCreated.emit(planFromDB.toUiState(Plan.Empty))
		}
	}

}